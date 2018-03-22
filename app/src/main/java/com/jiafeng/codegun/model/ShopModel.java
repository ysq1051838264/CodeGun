package com.jiafeng.codegun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangshuquan on 2018/3/2.
 */

/**
 * 已经舍弃   3-22
 */

public class ShopModel implements Parcelable {
    public String shopName;
    public String shopInfo;
    public String shopImage;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shopName);
        dest.writeString(this.shopInfo);
        dest.writeString(this.shopImage);
    }

    public ShopModel() {
    }

    protected ShopModel(Parcel in) {
        this.shopName = in.readString();
        this.shopInfo = in.readString();
        this.shopImage = in.readString();
    }

    public static final Parcelable.Creator<ShopModel> CREATOR = new Parcelable.Creator<ShopModel>() {
        @Override
        public ShopModel createFromParcel(Parcel source) {
            return new ShopModel(source);
        }

        @Override
        public ShopModel[] newArray(int size) {
            return new ShopModel[size];
        }
    };
}
