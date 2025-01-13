package com.arittek.signalrtestandroid.commons;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
public class SSLHelper {

    // Load the certificate and create an OkHttpClient
    public static OkHttpClient createSslClientFromLibrary(InputStream certificateInputStream) throws Exception {
        // Create an empty KeyStore
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);

        // Add your certificate to the KeyStore
        keyStore.setCertificateEntry("ca", loadCertificate(certificateInputStream));

        // Initialize TrustManagerFactory with the KeyStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Get TrustManagers (used to validate SSL certificates)
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        // Set up SSLContext to use the custom TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagers, new SecureRandom());

        // Create and return an OkHttpClient that uses the custom SSLContext
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                .hostnameVerifier((hostname, session) -> true)  // Optional: disable hostname verification for testing
                .build();
    }

    // Utility method to load the certificate from an InputStream
    private static java.security.cert.X509Certificate loadCertificate(InputStream inputStream) throws Exception {
        java.security.cert.CertificateFactory certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509");
        return (java.security.cert.X509Certificate) certificateFactory.generateCertificate(inputStream);
    }
}