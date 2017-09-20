package com.mazmellow.testomise.presenter;

import com.mazmellow.testomise.model.RequestModel;
import com.mazmellow.testomise.service.ApiClient;

import co.omise.android.models.Token;

/**
 * Created by Maz on 9/2/16 AD.
 */
public class CharityPresenter extends BasePresenter {

    public static final int TYPE_CHARITY_LIST = 0;
    public static final int TYPE_CHARITY_DONATE = 1;

    private RequestModel requestModel;

    public CharityPresenter(int type){
        setType(type);
    }

    public void setDonateData(Token token, String name, int amount){
        requestModel = new RequestModel();
        requestModel.setToken(token.id);
        requestModel.setName(name);
        requestModel.setAmount(amount);
    }

    @Override
    public void requestData(){
        super.requestData();
        if(apiClient == null) return;

        switch (type) {
            case TYPE_CHARITY_LIST: {
                apiClient.requestCharityList(new ApiClient.ApiClientListenner() {
                    @Override
                    public void onSuccess(Object model) {
                        if(mvpView!=null) mvpView.showResult(model, type);
                    }

                    @Override
                    public void onFail(String message) {
                        if(mvpView!=null) mvpView.showError(message);
                    }
                });
            }
            break;

            case TYPE_CHARITY_DONATE: {
                if(requestModel==null) return;

                apiClient.requestCharityDonate(requestModel, new ApiClient.ApiClientListenner() {
                    @Override
                    public void onSuccess(Object model) {
                        requestModel = null;
                        if(mvpView!=null) mvpView.showResult(model, type);
                    }

                    @Override
                    public void onFail(String message) {
                        requestModel = null;
                        if(mvpView!=null) mvpView.showError(message);
                    }
                });
            }
            break;
        }

    }
}
