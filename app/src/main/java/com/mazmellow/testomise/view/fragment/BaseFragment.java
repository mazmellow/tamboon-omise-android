package com.mazmellow.testomise.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.mazmellow.testomise.view.MvpView;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class BaseFragment extends Fragment implements MvpView {

    public void showAlert(String message) {
        Activity activity = getActivity();
        if(activity==null || activity.isFinishing()) return;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showResult(Object result, int type) {

    }

    @Override
    public void showError(String message) {
        showAlert(message);
    }

    @Override
    public Context getViewContext() {
        return getActivity();
    }
}