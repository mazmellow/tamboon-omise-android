package com.mazmellow.testomise.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mazmellow.testomise.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

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
        if (context==null || !checkInternet(context)) return null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(configuration.getServerUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient())
                .build();

        return retrofit.create(ApiService.class);
    }

    private OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if(BuildConfig.DEBUG) interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES);

        //For Handle Certificate Pinning.
        builder.sslSocketFactory(PubKeyManager.getSslSocketFactory(configuration.getKey()), new PubKeyManager(configuration.getKey()))
                .hostnameVerifier(PubKeyManager.getHostnameVerifier(configuration.getHostName()));

        return builder.build();
    }

    private boolean checkInternet(Context context) {

        boolean hasInternet = true;

        if (context == null) hasInternet = false;

        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null || !i.isConnected() || !i.isAvailable()) hasInternet = false;

        if (!hasInternet) Timber.w(" -------- NO INTERNET ------- ");

        return hasInternet;
    }
}
