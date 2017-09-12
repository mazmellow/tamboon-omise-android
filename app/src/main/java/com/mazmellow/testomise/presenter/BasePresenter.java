package com.mazmellow.testomise.presenter;

import android.content.Context;

import com.mazmellow.testomise.service.ApiClient;
import com.mazmellow.testomise.view.MvpView;

/**
 * Created by Maz on 9/4/16 AD.
 */
public class BasePresenter implements Presenter<MvpView> {

    public MvpView mvpView;
    public Context context;
    public ApiClient apiClient;

    public int type;
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void attachView(MvpView view) {
        this.mvpView = view;
    }

    @Override
    public void detachView() {
        this.mvpView = null;
        if(apiClient !=null) apiClient.cancelAllCalls();
    }

    @Override
    public void requestData(){
        if(mvpView==null) return;
        if(type == -1) return;

        if(context==null) {
            context = mvpView.getViewContext();
            apiClient = new ApiClient(context);
        }
    }
}
