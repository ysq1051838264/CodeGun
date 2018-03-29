package com.jiafeng.codegun.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangshuquan on 2018/3/29.
 */

public class APPInfo implements Parcelable {

    String versionCode;
    String forceUpdate;
    String url;
    String size;
    String modify;

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionCode);
        dest.writeString(this.forceUpdate);
        dest.writeString(this.url);
        dest.writeString(this.size);
        dest.writeString(this.modify);
    }

    public APPInfo() {
    }

    protected APPInfo(Parcel in) {
        this.versionCode = in.readString();
        this.forceUpdate = in.readString();
        this.url = in.readString();
        this.size = in.readString();
        this.modify = in.readString();
    }

    public static final Creator<APPInfo> CREATOR = new Creator<APPInfo>() {
        @Override
        public APPInfo createFromParcel(Parcel source) {
            return new APPInfo(source);
        }

        @Override
        public APPInfo[] newArray(int size) {
            return new APPInfo[size];
        }
    };
}
