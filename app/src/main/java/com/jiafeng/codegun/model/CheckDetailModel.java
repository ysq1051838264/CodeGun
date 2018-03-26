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

    public CheckDetailModel() {
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
    }

    protected CheckDetailModel(Parcel in) {
        this.barCode = in.readString();
        this.oldBarCode = in.readString();
        this.goodsName = in.readString();
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
}
