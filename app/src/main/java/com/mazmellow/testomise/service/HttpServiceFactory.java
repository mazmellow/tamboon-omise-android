package com.mazmellow.testomise.service;

import android.content.Context;

import com.mazmellow.testomise.BuildConfig;
import com.mazmellow.testomise.util.ConnectionUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class HttpServiceFactory {

    private Context context;
    private Configuration configuration;

    public HttpServiceFactory(Context context) {
        this.context = context;
        configuration = new Configuration();
    }

    public ApiService getApiService() {
        if (context==null || !ConnectionUtil.hasInternet(context)) return null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configuration.getServerUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();

        return retrofit.create(ApiService.class);
    }

    private OkHttpClient getClient() {

        //TODO: Not Logging when Release app.
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES);

        //TODO: Handle Certificate Pinning.
        builder.sslSocketFactory(PubKeyManager.getSslSocketFactory(configuration.getKey()), new PubKeyManager(configuration.getKey()))
                .hostnameVerifier(PubKeyManager.getHostnameVerifier(configuration.getHostName()));

        return builder.build();
    }
}
