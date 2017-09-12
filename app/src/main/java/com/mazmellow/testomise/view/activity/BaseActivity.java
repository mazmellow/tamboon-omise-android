package com.mazmellow.testomise.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.mazmellow.testomise.R;
import com.mazmellow.testomise.util.DialogUtil;
import com.mazmellow.testomise.view.MvpView;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class BaseActivity extends AppCompatActivity implements MvpView {

    private ProgressDialog progressDialog;

    public void showAlert(String message) {
        DialogUtil.showAlertOneButton(this, getString(R.string.error), message, null);
    }

    @Override
    public void showLoading() {
        if (!isFinishing() && progressDialog == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(BaseActivity.this);
                    progressDialog.setMessage(getString(R.string.please_waiting));
                    try {
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                    }catch (Exception e){}
                }
            });
        }
    }

    public void hideLoading() {
        if(!isFinishing() && progressDialog!=null && progressDialog.isShowing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }catch (Exception e){}
                }
            });
        }
    }

    @Override
    public void showResult(Object result, int type) {
        hideLoading();
    }

    @Override
    public void showError(String message) {
        hideLoading();
        showAlert(message);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

}
