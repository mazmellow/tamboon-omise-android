package com.mazmellow.testomise.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mazmellow.testomise.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class SuccessActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnOk)
    public void onClickOk(){
        finish();
    }
}
