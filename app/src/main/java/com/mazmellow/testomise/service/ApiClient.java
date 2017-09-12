package com.mazmellow.testomise.service;

import android.content.Context;

import com.mazmellow.testomise.R;
import com.mazmellow.testomise.model.CharityModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Maz on 5/3/16 AD.
 */
public class ApiClient {

    private Context context;
    private ArrayList<Call<?>> callArrayList;

    public ApiClient(Context context) {
        this.context = context;
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
        Timber.i("Request CharityModel List");

        ApiService service = new HttpServiceFactory(context).getApiService();
        if (service == null) {
            if (listenner != null) listenner.onFail(context.getString(R.string.please_connect_internet));
            return;
        }

        Call<List<CharityModel>> call = service.requestCharityList();
        setCall(call);
        call.enqueue(new Callback<List<CharityModel>>() {
            @Override
            public void onResponse(Call<List<CharityModel>> call, Response<List<CharityModel>> response) {
                List<CharityModel> baseModel = null;
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
            public void onFailure(Call<List<CharityModel>> call, Throwable t) {
                Timber.w("Call Failure");
                removeCall(call);
                if (listenner != null) listenner.onFail(t.getMessage());
            }
        });
    }

    public interface ApiClientListenner {
        void onSuccess(Object response);
        void onFail(String message);
    }
}
