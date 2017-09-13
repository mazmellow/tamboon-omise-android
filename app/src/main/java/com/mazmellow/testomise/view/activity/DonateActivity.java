package com.mazmellow.testomise.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.mazmellow.testomise.model.ResponseModel;
import com.mazmellow.testomise.presenter.CharityPresenter;
import com.mazmellow.testomise.util.KeyStoreManager;

import java.security.GeneralSecurityException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.omise.android.CardNumber;
import co.omise.android.Client;
import co.omise.android.TokenRequest;
import co.omise.android.TokenRequestListener;
import co.omise.android.models.Token;
import co.omise.android.ui.CreditCardEditText;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class DonateActivity extends BaseActivity {

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
    private CharityPresenter charityPresenter;

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
            if(!TextUtils.isEmpty(imageUrl)) Glide.with(this).load(imageUrl).placeholder(R.drawable.placeholder).into(imgCharity);
        }

        edtUsername.addTextChangedListener(getValidateFormTextWatcher());
        edtAmount.addTextChangedListener(getValidateFormTextWatcher());
        edtCardNumber.addTextChangedListener(getValidateFormTextWatcher());
        edtCvv.addTextChangedListener(getValidateFormTextWatcher());

        edtUsername.setOnFocusChangeListener(getHideKeyboardOnFocusListenner(edtUsername));
        edtAmount.setOnFocusChangeListener(getHideKeyboardOnFocusListenner(edtUsername));
        edtCardNumber.setOnFocusChangeListener(getHideKeyboardOnFocusListenner(edtUsername));
        edtCvv.setOnFocusChangeListener(getHideKeyboardOnFocusListenner(edtUsername));

        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.month_array, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpMonth.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.year_array, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnExpYear.setAdapter(yearAdapter);


        charityPresenter = new CharityPresenter(CharityPresenter.TYPE_CHARITY_DONATE);
        charityPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        charityPresenter.detachView();
    }

    private void validateForm() {
        String userName = edtUsername.getText().toString();
        String amount = edtAmount.getText().toString();
        String cardNumber = edtCardNumber.getText().toString();
        String cvv = edtCvv.getText().toString();

        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cvv)) {
            btnSubmit.setEnabled(false);
            return;
        }

        if(CardNumber.normalize(cardNumber).length()!=16){
            btnSubmit.setEnabled(false);
            return;
        }

        if(cvv.length()!=3){
            btnSubmit.setEnabled(false);
            return;
        }

        btnSubmit.setEnabled(true);
    }

    @OnClick(R.id.btnSubmit)
    public void onClickSubmit() {
        String name = edtUsername.getText().toString();
        String amount = edtAmount.getText().toString();
        String number = edtCardNumber.getText().toString();
        String cvv = edtCvv.getText().toString();
        String expMonth = (String)spnExpMonth.getSelectedItem();
        String expYear = (String)spnExpYear.getSelectedItem();

        name = name.toUpperCase();
        number = CardNumber.normalize(number);
        if(expMonth.startsWith("0")) expMonth = expMonth.substring(1);
        int month = Integer.parseInt(expMonth);
        int year = Integer.parseInt(expYear);
        int donateAmount = Integer.parseInt(amount);

        requestOmisePayment(number, name, month, year, cvv, donateAmount);
    }

    private void requestOmisePayment(String number, final String name, int month, int year, String cvv, final int donateAmount){
        showLoading();
        try {
            String key = KeyStoreManager.getValue(KeyStoreManager.OMISE_PKEY);
            Client client = new Client(key);

            TokenRequest request = new TokenRequest();
            request.number = number;
            request.name = name;
            request.expirationMonth = month;
            request.expirationYear = year;
            request.securityCode = cvv;

            client.send(request, new TokenRequestListener() {
                @Override
                public void onTokenRequestSucceed(TokenRequest request, Token token) {
                    charityPresenter.setDonateData(token, name, donateAmount);
                    charityPresenter.requestData();
                }

                @Override
                public void onTokenRequestFailed(TokenRequest request, Throwable throwable) {
                    hideLoading();
                    showAlert(throwable.getMessage());
                }
            });

        }catch (GeneralSecurityException e){
            hideLoading();
            showAlert(e.getMessage());
        }
    }

    @Override
    public void showResult(Object result, int type) {
        super.showResult(result, type);

        ResponseModel responseModel = (ResponseModel) result;
        if(responseModel.isSuccess()){
            startActivity(new Intent(this, SuccessActivity.class));
            finish();
        }else{
            showAlert(getString(R.string.failed_donate));
        }
    }

    private TextWatcher getValidateFormTextWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateForm();
            }
        };
    }
}
