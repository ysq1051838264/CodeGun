package com.jiafeng.codegun.http;

import com.jiafeng.codegun.model.CompanyInfo;

/**
 * 直接请求返回数据格式
 * Created by WZG on 2016/7/16.
 */
public class RetrofitEntity {
    private int ret;
    private String msg;

    public CompanyInfo getAreaInfo() {
        return areaInfo;
    }

    private CompanyInfo areaInfo;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setAreaInfo(CompanyInfo areaInfo) {
        this.areaInfo = areaInfo;
    }
}
