package com.mazmellow.testomise.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mazmellow.testomise.BuildConfig;
import com.mazmellow.testomise.R;
import com.mazmellow.testomise.model.Charity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Maz on 5/3/16 AD.
 */
public class ApiClient {

    private Context context;
    private Configuration configuration;
    private ArrayList<Call<?>> callArrayList;

    public ApiClient(Context context) {
        this.context = context;
        configuration = new Configuration();
        callArrayList = new ArrayList<>();
    }

    private void setCall(Call<?> call) {
        callArrayList.add(call);
    }

    private void removeCall(Call<?> call) {
        callArrayList.remove(call);
    }

    public void cancelAllCalls() {
        try {
            for (Call<?> call : callArrayList) {
                call.cancel();
                removeCall(call);
            }
        }catch (Exception e) {
            Timber.e("Exception: "+e.getMessage());
        }
    }

    public void requestCharityList(final ApiClientListenner listenner) {
        String tag = "Request Charity List";
        Timber.i(tag);

        ApiService service = getApiService();
        if (service == null) {
            if (listenner != null) listenner.onFail(context.getString(R.string.please_connect_internet));
            return;
        }

        Call<List<Charity>> call = service.requestCharityList();
        setCall(call);
        call.enqueue(new Callback<List<Charity>>() {
            @Override
            public void onResponse(Call<List<Charity>> call, Response<List<Charity>> response) {
                List<Charity> baseModel = null;
                if(response.isSuccessful()){
                    baseModel = response.body();
                }else{
                    ResponseBody responseBody = response.errorBody();
                    try {
                        String error = responseBody.string();
                        Timber.e("Response Body: %s", error);
                        if (listenner != null) listenner.onFail(error);
                        removeCall(call);
                        return;
                    }catch (Exception e){}
                }
                if (baseModel == null) {
                    Timber.e("Response is Null");
                    if (listenner != null) listenner.onFail(context.getString(R.string.error_server));
                    removeCall(call);
                    return;
                }

                Timber.i("Response Success");
                if (listenner != null) listenner.onSuccess(baseModel);
                removeCall(call);
            }

            @Override
            public void onFailure(Call<List<Charity>> call, Throwable t) {
                Timber.w("Call Failure");
                removeCall(call);
                if (listenner != null) listenner.onFail(t.getMessage());
            }
        });
    }

    private ApiService getApiService() {
        if (!checkInternet(context)) return null;

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

        builder.sslSocketFactory(PubKeyManager.getSslSocketFactory(configuration.getKey()), new PubKeyManager(configuration.getKey()))
                .hostnameVerifier(PubKeyManager.getHostnameVerifier(configuration.getHostName()));

        return builder.build();
    }

    public interface ApiClientListenner {
        void onSuccess(Object response);
        void onFail(String message);
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
