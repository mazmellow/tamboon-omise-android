package com.mazmellow.testomise.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class CharityModel implements Parcelable{
    private int id;
    private String name, logo_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(logo_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected CharityModel(Parcel in){
        id = in.readInt();
        name = in.readString();
        logo_url = in.readString();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        @Override
        public Object createFromParcel(Parcel in) {
            return new CharityModel(in);
        }

        @Override
        public CharityModel[] newArray(int i) {
            return new CharityModel[i];
        }
    };
}
