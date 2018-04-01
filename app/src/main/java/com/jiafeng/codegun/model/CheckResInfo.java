package com.jiafeng.codegun.model;

import java.util.List;

/**
 * Created by yangshuquan on 2018/3/24.
 */

public class CheckResInfo {
    String bookNum;
    String pyNum;
    String pkNum;
    String ypNum;
    String wzNum;
    String spNum;
    String wzLabelStr;
    List<CheckDetailModel> pklist;  //盘亏列表
    List<CheckDetailModel> pylist;

    public String getPyNum() {
        return pyNum;
    }

    public void setPyNum(String pyNum) {
        this.pyNum = pyNum;
    }

    public String getPkNum() {
        return pkNum;
    }

    public void setPkNum(String pkNum) {
        this.pkNum = pkNum;
    }

    public String getWzNum() {
        return wzNum;
    }

    public void setWzNum(String wzNum) {
        this.wzNum = wzNum;
    }

    public String getSpNum() {
        return spNum;
    }

    public void setSpNum(String spNum) {
        this.spNum = spNum;
    }

    public String getWzLabelStr() {
        return wzLabelStr;
    }

    public void setWzLabelStr(String wzLabelStr) {
        this.wzLabelStr = wzLabelStr;
    }

    public List<CheckDetailModel> getPklist() {
        return pklist;
    }

    public void setPklist(List<CheckDetailModel> pklist) {
        this.pklist = pklist;
    }

    public List<CheckDetailModel> getPylist() {
        return pylist;
    }

    public void setPylist(List<CheckDetailModel> pylist) {
        this.pylist = pylist;
    }

    public String getBookNum() {
        return bookNum;
    }

    public void setBookNum(String bookNum) {
        this.bookNum = bookNum;
    }

    public String getYpNum() {
        return ypNum;
    }

    public void setYpNum(String ypNum) {
        this.ypNum = ypNum;
    }
}
