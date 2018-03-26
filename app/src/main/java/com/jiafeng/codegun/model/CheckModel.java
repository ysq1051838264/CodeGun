package com.jiafeng.codegun.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by yangshuquan on 2018/3/2.
 */

/**
 * 订单model
 */
public class CheckModel extends RealmObject implements Parcelable {
    public String companyNo;  //公司编号
    public String id;    //盘点单id
    public String sheetNo;    //单号
    public String shopName;    //门店
    public String storeName;    //盘点柜台
    public String storeId;    //柜台id(多个以逗号分隔开)
    public String checkNum = "0";    //实盘数量
    public int sheetStatus = 0;  //1：进行中；2：正在生产盘点结果数据;3:已结束
    public String createTime;   //创建时间

    public String tids;
    public String guids;

    public Boolean isCompile() {
        return sheetStatus == 3;
    }

    public CheckModel() {

    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public int getSheetStatus() {
        return sheetStatus;
    }

    public void setSheetStatus(int sheetStatus) {
        this.sheetStatus = sheetStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.companyNo);
        dest.writeString(this.id);
        dest.writeString(this.sheetNo);
        dest.writeString(this.shopName);
        dest.writeString(this.storeName);
        dest.writeString(this.storeId);
        dest.writeString(this.checkNum);
        dest.writeInt(this.sheetStatus);
        dest.writeString(this.createTime);
        dest.writeString(this.tids);
        dest.writeString(this.guids);
    }

    protected CheckModel(Parcel in) {
        this.companyNo = in.readString();
        this.id = in.readString();
        this.sheetNo = in.readString();
        this.shopName = in.readString();
        this.storeName = in.readString();
        this.storeId = in.readString();
        this.checkNum = in.readString();
        this.sheetStatus = in.readInt();
        this.createTime = in.readString();
        this.tids = in.readString();
        this.guids = in.readString();
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

    public String getTids() {
        return tids;
    }

    public void setTids(String tids) {
        this.tids = tids;
    }

    public String getGuids() {
        return guids;
    }

    public void setGuids(String guids) {
        this.guids = guids;
    }
}
