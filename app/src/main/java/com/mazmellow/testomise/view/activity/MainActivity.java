package com.mazmellow.testomise.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mazmellow.testomise.R;
import com.mazmellow.testomise.presenter.CharityPresenter;
import com.mazmellow.testomise.util.DialogUtil;
import com.mazmellow.testomise.util.RootUtil;
import com.mazmellow.testomise.view.adapter.CharityAdapter;
import com.mazmellow.testomise.view.fragment.RecyclePullRefreshFragment;

import org.greenrobot.eventbus.EventBus;

import static com.mazmellow.testomise.presenter.CharityPresenter.TYPE_CHARITY_LIST;

public class MainActivity extends BaseActivity {

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
            next();
            return;
        }

        DialogUtil.showAlert(this, getString(R.string.root_detected_message), "", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                next();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
    }

    private void next() {
        fragment = RecyclePullRefreshFragment.newInstance(2);
        fragment.setPresenter((new CharityPresenter(TYPE_CHARITY_LIST)));
        fragment.setAdapter(new CharityAdapter());
        setFragment(fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragment!=null) fragment.refresh();
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}
