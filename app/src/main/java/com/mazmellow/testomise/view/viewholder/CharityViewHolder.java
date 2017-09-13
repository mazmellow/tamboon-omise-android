package com.mazmellow.testomise.view.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mazmellow.testomise.R;
import com.mazmellow.testomise.eventbus.OpenCharityEvent;
import com.mazmellow.testomise.model.CharityModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class CharityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    @Bind(R.id.imgThumb) ImageView imgThumb;
    @Bind(R.id.txtTitle) TextView txtTitle;

    private Context context;
    private CharityModel charityModel;

    public CharityViewHolder(View view, Context context) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
        view.setOnClickListener(this);
    }

    public void bind(CharityModel model) {
        charityModel = model;

        String name = charityModel.getName();
        if(!TextUtils.isEmpty(name)) txtTitle.setText(name);

        String imageUrl = charityModel.getLogo_url();
        if(!TextUtils.isEmpty(imageUrl)) Glide.with(context).load(imageUrl).placeholder(R.drawable.placeholder).into(imgThumb);
    }

    @Override
    public void onClick(View v) {
        EventBus.getDefault().post(new OpenCharityEvent(charityModel));
    }
}
