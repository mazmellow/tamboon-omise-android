package com.mazmellow.testomise.view;

import android.content.Context;

public interface MvpView {

    //TODO: Interface View for define state of view
    void showResult(Object result, int type); //Hide Loading, Hide Message, Show List
    void showError(String message); //Hide Loading, Show Message, Hide List
    void showLoading(); //Show Loading, Hide Message , Hide List

    Context getViewContext();
}
