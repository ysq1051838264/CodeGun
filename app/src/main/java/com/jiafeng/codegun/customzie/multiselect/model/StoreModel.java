package com.jiafeng.codegun.customzie.multiselect.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HZF on 2017/5/16.
 */

public class StoreModel implements Parcelable {

    private String storeName;
    private boolean isChecked;
    private String areaCode;
    private String id;

    public StoreModel(String storeName, boolean isChecked) {
        this.storeName = storeName;
        this.isChecked = isChecked;
    }

    public StoreModel(String storeName, boolean isChecked, String id) {
        this.storeName = storeName;
        this.isChecked = isChecked;
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.storeName);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.areaCode);
        dest.writeString(this.id);
    }

    protected StoreModel(Parcel in) {
        this.storeName = in.readString();
        this.isChecked = in.readByte() != 0;
        this.areaCode = in.readString();
        this.id = in.readString();
    }

    public static final Creator<StoreModel> CREATOR = new Creator<StoreModel>() {
        @Override
        public StoreModel createFromParcel(Parcel source) {
            return new StoreModel(source);
        }

        @Override
        public StoreModel[] newArray(int size) {
            return new StoreModel[size];
        }
    };

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static Creator<StoreModel> getCREATOR() {
        return CREATOR;
    }
}
