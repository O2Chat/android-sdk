package com.example.signalrtestandroid.retrofit;

import android.content.Context;

import com.example.signalrtestandroid.commons.Common;
import com.example.signalrtestandroid.commons.TokenAuthenticator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {

    public String URL = "";
    private ApiClient instance;
    // Keep your services here, build them in buildRetrofit method later
    private WebService webService;
    private static Common commons;

    public ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
            commons = new Common();
        }
        return instance;
    }

    // Build retrofit once when creating a single instance
    public ApiClient(Context context) {
        // Implement a method to build your retrofit
        Retrofit(new Common().getBaseUrlChat(context),context);
    }

    // Retrofit Class use Live Service
    public void Retrofit(String ip, Context context) {
        Retrofit retrofit;
        Gson gson = new GsonBuilder().setLenient().create();

        OkHttpClient.Builder httpClientBuilder =
                new OkHttpClient.Builder().
                        readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(new ApiInterceptor(context))
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .authenticator(new TokenAuthenticator(context));

        if(commons!=null){
            commons.initSSL(httpClientBuilder,context);
        }

        // Live Server Https use
        retrofit = new Retrofit.Builder()
                .baseUrl(ip + "api/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientBuilder.build())
                .build();
        webService = retrofit.create(WebService.class);

    } // close the Retrofit Static Methord

    public WebService getWebService() {
        return webService;
    }

}
