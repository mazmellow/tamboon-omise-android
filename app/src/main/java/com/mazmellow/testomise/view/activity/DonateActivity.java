package com.mazmellow.testomise.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mazmellow.testomise.Constants;
import com.mazmellow.testomise.R;
import com.mazmellow.testomise.model.CharityModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.omise.android.ui.CreditCardEditText;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class DonateActivity extends BaseActivity {

    private static final String OMISE_PKEY = "pkey_test_599zublv8mxs6vg394a";

    @Bind(R.id.imgCharity) ImageView imgCharity;
    @Bind(R.id.txtCharityName) TextView txtCharityName;
    @Bind(R.id.edtUsername) EditText edtUsername;
    @Bind(R.id.edtAmount) EditText edtAmount;
    @Bind(R.id.edtCardNumber) CreditCardEditText edtCardNumber;
    @Bind(R.id.spnExpMonth) Spinner spnExpMonth;
    @Bind(R.id.spnExpYear) Spinner spnExpYear;
    @Bind(R.id.edtCvv) EditText edtCvv;
    @Bind(R.id.btnSubmit) Button btnSubmit;

    private CharityModel charityModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if(intent!=null){
            charityModel = intent.getParcelableExtra(Constants.BUNDLE_CHARITY);
        }

        if(charityModel!=null){
            String name = charityModel.getName();
            if(!TextUtils.isEmpty(name))txtCharityName.setText(name);

            String imageUrl = charityModel.getLogo_url();
            if(!TextUtils.isEmpty(imageUrl)) Glide.with(this).load(imageUrl).into(imgCharity);
        }

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpMonth.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.year_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpYear.setAdapter(yearAdapter);
    }

    private void validateForm() {

    }

    @OnClick(R.id.btnSubmit)
    public void onClickSubmit() {

    }


}
