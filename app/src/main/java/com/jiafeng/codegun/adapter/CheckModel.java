package com.jiafeng.codegun.adapter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangshuquan on 2018/3/2.
 */

/**
 * 订单model
 */
public class CheckModel implements Parcelable {
    public String companyNo;  //公司编号
    public String checkId;    //盘点单id
    public String sheetNo;    //单号
    public String shopName;    //门店
    public String storeName;    //盘点柜台
    public String storeId;    //柜台id(多个以逗号分隔开)
    public String checkNum;    //实盘数量
    public int sheetStatus;  //1：进行中；2：正在生产盘点结果数据;3:已结束
    public String createTime;   //创建时间

    public Boolean isCompile() {
        return sheetStatus == 1;
    }

    public CheckModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.companyNo);
        dest.writeString(this.checkId);
        dest.writeString(this.sheetNo);
        dest.writeString(this.shopName);
        dest.writeString(this.storeName);
        dest.writeString(this.storeId);
        dest.writeString(this.checkNum);
        dest.writeInt(this.sheetStatus);
        dest.writeString(this.createTime);
    }

    protected CheckModel(Parcel in) {
        this.companyNo = in.readString();
        this.checkId = in.readString();
        this.sheetNo = in.readString();
        this.shopName = in.readString();
        this.storeName = in.readString();
        this.storeId = in.readString();
        this.checkNum = in.readString();
        this.sheetStatus = in.readInt();
        this.createTime = in.readString();
    }

    public static final Creator<CheckModel> CREATOR = new Creator<CheckModel>() {
        @Override
        public CheckModel createFromParcel(Parcel source) {
            return new CheckModel(source);
        }

        @Override
        public CheckModel[] newArray(int size) {
            return new CheckModel[size];
        }
    };
}
