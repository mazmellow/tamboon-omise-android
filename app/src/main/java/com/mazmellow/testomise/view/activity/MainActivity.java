package com.mazmellow.testomise.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.mazmellow.testomise.Constants;
import com.mazmellow.testomise.R;
import com.mazmellow.testomise.eventbus.OpenCharityEvent;
import com.mazmellow.testomise.model.CharityModel;
import com.mazmellow.testomise.presenter.CharityPresenter;
import com.mazmellow.testomise.util.DialogUtil;
import com.mazmellow.testomise.util.RootUtil;
import com.mazmellow.testomise.view.adapter.CharityAdapter;
import com.mazmellow.testomise.view.fragment.RecyclePullRefreshFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.mazmellow.testomise.presenter.CharityPresenter.TYPE_CHARITY_LIST;

public class MainActivity extends BaseActivity {

    private RecyclePullRefreshFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);

        //TODO: Notify user if device is rooted.
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
        showDonate(event.getModel());
    }

    private void showDonate(CharityModel charityModel) {
        if(charityModel==null) return;

        Intent intent = new Intent(this, DonateActivity.class);
        intent.putExtra(Constants.BUNDLE_CHARITY, charityModel);
        startActivity(intent);
    }

    public RecyclePullRefreshFragment getFragment() {
        return fragment;
    }
}
