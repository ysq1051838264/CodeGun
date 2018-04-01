package com.jiafeng.codegun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangshuquan on 2018/3/2.
 */

public class CheckDetailModel implements Parcelable {
    public String barCode;  //条码号
    public String oldBarCode;   //条码号
    public String goodsName;  //商品名称
    public String labelPrice;  //标价
    public String goldWeight;  //金重

    public CheckDetailModel() {
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOldBarCode() {
        return oldBarCode;
    }

    public void setOldBarCode(String oldBarCode) {
        this.oldBarCode = oldBarCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getLabelPrice() {
        return labelPrice;
    }

    public void setLabelPrice(String labelPrice) {
        this.labelPrice = labelPrice;
    }

    public String getGoldWeight() {
        return goldWeight;
    }

    public void setGoldWeight(String goldWeight) {
        this.goldWeight = goldWeight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.barCode);
        dest.writeString(this.oldBarCode);
        dest.writeString(this.goodsName);
        dest.writeString(this.labelPrice);
        dest.writeString(this.goldWeight);
    }

    protected CheckDetailModel(Parcel in) {
        this.barCode = in.readString();
        this.oldBarCode = in.readString();
        this.goodsName = in.readString();
        this.labelPrice = in.readString();
        this.goldWeight = in.readString();
    }

    public static final Creator<CheckDetailModel> CREATOR = new Creator<CheckDetailModel>() {
        @Override
        public CheckDetailModel createFromParcel(Parcel source) {
            return new CheckDetailModel(source);
        }

        @Override
        public CheckDetailModel[] newArray(int size) {
            return new CheckDetailModel[size];
        }
    };
}
