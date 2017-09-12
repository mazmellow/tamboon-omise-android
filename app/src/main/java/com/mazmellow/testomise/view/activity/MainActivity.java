package com.mazmellow.testomise.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.mazmellow.testomise.R;
import com.mazmellow.testomise.eventbus.OpenCharityEvent;
import com.mazmellow.testomise.presenter.CharityPresenter;
import com.mazmellow.testomise.util.DialogUtil;
import com.mazmellow.testomise.util.RootUtil;
import com.mazmellow.testomise.view.adapter.CharityAdapter;
import com.mazmellow.testomise.view.fragment.RecyclePullRefreshFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import co.omise.android.models.Token;
import co.omise.android.ui.CreditCardActivity;

import static com.mazmellow.testomise.presenter.CharityPresenter.TYPE_CHARITY_LIST;

public class MainActivity extends BaseActivity {

    private static final String OMISE_PKEY = "pkey_test_599zublv8mxs6vg394a";
    private static final int REQUEST_CC = 100;

    private RecyclePullRefreshFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);

        checkDeviceRoot();
    }

    private void checkDeviceRoot() {
        if (!RootUtil.isDeviceRooted()) {
            setFragment();
            return;
        }

        DialogUtil.showAlert(this, getString(R.string.root_detected_title), getString(R.string.root_detected_message), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setFragment();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
    }

    private void setFragment() {
        fragment = RecyclePullRefreshFragment.newInstance(2);
        fragment.setPresenter((new CharityPresenter(TYPE_CHARITY_LIST)));
        fragment.setAdapter(new CharityAdapter());
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Subscribe
    public void onEvent(OpenCharityEvent event){
        showCreditCardForm();
    }

    private void showCreditCardForm() {
        Intent intent = new Intent(this, CreditCardActivity.class);
        intent.putExtra(CreditCardActivity.EXTRA_PKEY, OMISE_PKEY);
        startActivityForResult(intent, REQUEST_CC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CC:
                if (resultCode == CreditCardActivity.RESULT_CANCEL) {
                    return;
                }

                Token token = data.getParcelableExtra(CreditCardActivity.EXTRA_TOKEN_OBJECT);
                // process your token here.

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
