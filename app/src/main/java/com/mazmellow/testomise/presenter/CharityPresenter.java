package com.mazmellow.testomise.presenter;

import com.mazmellow.testomise.service.ApiClient;

/**
 * Created by Maz on 9/2/16 AD.
 */
public class CharityPresenter extends BasePresenter {

    public static final int TYPE_CHARITY_LIST = 0;

    public CharityPresenter(int type){
        setType(type);
    }

    @Override
    public void requestData(){
        super.requestData();

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
        }

    }
}
