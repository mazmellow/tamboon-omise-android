package com.mazmellow.testomise.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maz on 8/29/16 AD.
 */
public abstract class BaseRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public abstract RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent, int viewType);

    public static final int VIEW_ITEM = 0;

    public ArrayList mArrayList;

    public BaseRecycleAdapter() {
        mArrayList = new ArrayList();
    }

    public void replace(List arrayListFrom) {
        mArrayList.clear();
        addDatas(arrayListFrom);
    }

    public void addDatas(List arrayListFrom) {
        for (int i = 0; i < arrayListFrom.size(); i++) {
            mArrayList.add(arrayListFrom.get(i));
        }
    }

    public ArrayList getDatas() {
        return mArrayList;
    }

    public void clearDatas() {
        mArrayList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = getItemViewHolder(parent, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position>=mArrayList.size()) return;
    }

    @Override
    public int getItemCount() {
        if (mArrayList != null && mArrayList.size()>0) {
            return mArrayList.size();
        }else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_ITEM;
    }

}
