package com.example.o2chatsdk.retrofit;
import android.content.Context;

import com.example.o2chatsdk.R;
import com.example.o2chatsdk.commons.TokenAuthenticator;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class NetworkClient {

    public static OkHttpClient createHttpClient(Context context) throws Exception {
        // Load the custom certificate from assets or raw folder
      //  InputStream certificateInputStream = context.getAssets().open("o2chat_io.crt");  // Adjust file path as needed
        InputStream certificateInputStream = context.getResources().openRawResource(R.raw.o2chat_io);

        // Create an OkHttpClient.Builder
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new ApiInterceptor(context))  // Your custom interceptors
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))  // Logging
                .authenticator(new TokenAuthenticator(context));  // Your custom authenticator

        // Create KeyStore and load the custom certificate
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);  // Initialize empty keystore
        keyStore.setCertificateEntry("ca", loadCertificate(certificateInputStream));  // Load certificate into keystore

        // Initialize TrustManagerFactory with the KeyStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Get the TrustManagers (used to validate the server's SSL certificates)
        javax.net.ssl.TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        // Create an SSLContext that uses the custom TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());

        // Set up SSL in the OkHttpClient
        httpClientBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                .hostnameVerifier((hostname, session) -> true);  // Optionally disable hostname verification (not recommended for production)

        // Return the configured OkHttpClient
        return httpClientBuilder.build();
    }

    // Utility method to load a certificate from InputStream
    private static java.security.cert.X509Certificate loadCertificate(InputStream inputStream) throws Exception {
        java.security.cert.CertificateFactory certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        return (java.security.cert.X509Certificate) certificateFactory.generateCertificate(inputStream);
    }
}
