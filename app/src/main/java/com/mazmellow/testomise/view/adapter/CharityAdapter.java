package com.mazmellow.testomise.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mazmellow.testomise.R;
import com.mazmellow.testomise.model.Charity;
import com.mazmellow.testomise.view.viewholder.CharityViewHolder;

/**
 * Created by Maz on 8/29/16 AD.
 */
public class CharityAdapter extends BaseRecycleAdapter {

    public CharityAdapter(){
        super();
    }

    public RecyclerView.ViewHolder getItemViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_charity, parent, false);
        return new CharityViewHolder(itemView, parent.getContext());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position>=mArrayList.size()) return;

        if(holder instanceof CharityViewHolder){
            CharityViewHolder viewHolder = (CharityViewHolder) holder;
            Charity model = (Charity) mArrayList.get(position);
            viewHolder.setIsRecyclable(false);
            viewHolder.bind(model);
        }
    }
}
