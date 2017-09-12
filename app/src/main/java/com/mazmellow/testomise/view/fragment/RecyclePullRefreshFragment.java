package com.mazmellow.testomise.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mazmellow.testomise.R;
import com.mazmellow.testomise.presenter.Presenter;
import com.mazmellow.testomise.util.DialogUtil;
import com.mazmellow.testomise.view.MvpView;
import com.mazmellow.testomise.view.activity.BaseActivity;
import com.mazmellow.testomise.view.adapter.BaseRecycleAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Maz on 8/24/16 AD.
 */
public class RecyclePullRefreshFragment extends BaseFragment {

    private static final String NUM_COLUMN = "NUM_COLUMN";

    @Bind(R.id.rvFeed) RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private GridLayoutManager glm;
    private BaseRecycleAdapter mAdapter;
    private BaseActivity mActivity;
    private int mNumColumn;
    private Presenter<MvpView> presenter;

    public static RecyclePullRefreshFragment newInstance(int numColumn) {
        RecyclePullRefreshFragment fragment = new RecyclePullRefreshFragment();
        Bundle args = new Bundle();
        args.putInt(NUM_COLUMN, numColumn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null) {
            mNumColumn = getArguments().getInt(NUM_COLUMN);
        }

        View view = inflater.inflate(R.layout.list_pullrefresh, container, false);
        ButterKnife.bind(this, view);
        mActivity = (BaseActivity)getActivity();

        if(mNumColumn<1) mNumColumn = 1;

        mRecyclerView.setHasFixedSize(false);
        glm = new GridLayoutManager(mActivity, mNumColumn){
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(mAdapter!=null) mRecyclerView.setAdapter(mAdapter);

        TypedValue typed_value = new TypedValue();
        mActivity.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, typed_value, true);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, getResources().getDimensionPixelSize(typed_value.resourceId));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }

    public void refresh(){
        mAdapter.clearDatas();
        showLoading();
        request();
    }

    @Override
    public void onDestroyView() {
        if(presenter!=null) presenter.detachView();
        super.onDestroyView();
    }

    private void request() {
        if(presenter==null) return;
        presenter.requestData();
    }

    @Override
    public void showError(String message) {
        final Activity activity = getActivity();
        if(activity==null || activity.isFinishing() || activity.isDestroyed()) return;

        DialogUtil.showAlertOneButton(activity, getString(R.string.error), message, null);
    }

    @Override
    public void showResult(Object result, int type) {
        mSwipeRefreshLayout.setRefreshing(false);

        List<?> results = (List<?>) result;
        if(results.size()>0){
            if(mAdapter!=null) mAdapter.replace(results);
            mRecyclerView.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.GONE);
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void setAdapter(BaseRecycleAdapter adapter) {
        mAdapter = adapter;
        if (mRecyclerView!=null) mRecyclerView.setAdapter(mAdapter);
    }

    public void setPresenter(Presenter<MvpView> presenter) {
        this.presenter = presenter;
        this.presenter.attachView(this);
    }
}
