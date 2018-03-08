package com.jiafeng.codegun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangshuquan on 2018/3/5.
 */

public class VersionData implements Parcelable {
    public String message;
    public String downloadUrl;
    public boolean isForce;
    public boolean needShow;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeString(this.downloadUrl);
        dest.writeByte(this.isForce ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needShow ? (byte) 1 : (byte) 0);
    }

    public VersionData() {
    }

    protected VersionData(Parcel in) {
        this.message = in.readString();
        this.downloadUrl = in.readString();
        this.isForce = in.readByte() != 0;
        this.needShow = in.readByte() != 0;
    }

    public static final Parcelable.Creator<VersionData> CREATOR = new Parcelable.Creator<VersionData>() {
        @Override
        public VersionData createFromParcel(Parcel source) {
            return new VersionData(source);
        }

        @Override
        public VersionData[] newArray(int size) {
            return new VersionData[size];
        }
    };
}


