package com.jiafeng.codegun.model;

import java.util.List;

/**
 * Created by yangshuquan on 2018/3/22.
 */

public class StoreList {
    List<StoreModel> storeInfo;
    List<CheckModel> goodscheck;
    ShopInfo shopInfo;
    SheetInfo sheetInfo;
    CompanyInfo areaInfo;
    String timestamp;
    String msg;
    String ret;

    public List<CheckModel> getGoodscheck() {
        return goodscheck;
    }

    public void setGoodscheck(List<CheckModel> goodscheck) {
        this.goodscheck = goodscheck;
    }


    public Boolean isSuccess() {
        return ret.equals("0");
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public void setStoreInfo(List<StoreModel> storeInfo) {
        this.storeInfo = storeInfo;
    }

    public List<StoreModel> getStoreInfo() {
        return storeInfo;
    }

    public SheetInfo getSheetInfo() {
        return sheetInfo;
    }

    public void setSheetInfo(SheetInfo sheetInfo) {
        this.sheetInfo = sheetInfo;
    }

    public CompanyInfo getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(CompanyInfo areaInfo) {
        this.areaInfo = areaInfo;
    }
}
