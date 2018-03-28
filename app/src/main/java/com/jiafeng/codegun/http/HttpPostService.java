package com.jiafeng.codegun.http;

import com.jiafeng.codegun.model.CheckModel;
import com.jiafeng.codegun.model.StoreList;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yangshuquan on 2018/3/5.
 */

public interface HttpPostService {

    //测试
    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedioBy(@Body boolean once_no);

    //8.升级
    @POST("updataCheck.do")
    @FormUrlEncoded
    Observable<StoreList> updataCheck(@Field("platformName") String platformName,
                                      @Field("versionCode") String versionCode,
                                      @Field("appType") String appType);

    //1.校验
    @POST("checkDeviceInfo.do")
    @FormUrlEncoded
    Observable<StoreList> checkDeviceInfo(@Field("sn") String sn);


    //2.获取门店
    @POST("getAssistInfo.do")
    @FormUrlEncoded
    Observable<StoreList> getAssistInfo(@Field("companyNo") String companyNo, @Field("sn") String sn);

    //3.获取盘点列表
    @POST("getGoodsCheckList.do")
    @FormUrlEncoded
    Observable<StoreList> getGoodsCheckList(@Field("sn") String sn, @Field("pageSize") String pageSize, @Field("pageNo") String pageNo);

    //4.根据ID查询盘点单详情
    @POST("getGoodsCheckDtl.do")
    @FormUrlEncoded
    Observable<StoreList> getGoodsCheckDtl(@Field("companyNo") String companyNo, @Field("id") String id);

    //7.	删除盘点单
    @POST("deleteGoodsCheck.do")
    @FormUrlEncoded
    Observable<StoreList> deleteGoodsCheck(@Field("companyNo") String companyNo,
                                           @Field("sheetId") String sheetId,
                                           @Field("sn") String sn);

    //5.5.	创建盘点单
    @POST("createGoodsCheck.do")
    @FormUrlEncoded
    Observable<StoreList> createGoodsCheck(@Field("companyNo") String companyNo,
                                           @Field("sn") String sn,
                                           @Field("shopAreaCode") String shopAreaCode,
                                           @Field("storeId") String storeId,
                                           @Field("remarks") String remarks);

    //6.	提交盘点单
    @POST("submitGoodsCheck.do")
    @FormUrlEncoded
    Observable<StoreList> submitGoodsCheck(@Field("companyNo") String companyNo,
                                           @Field("sn") String sn,
                                           @Field("tidStr") String tidStr,
                                           @Field("sheetId") String sheetId,
                                           @Field("guidStr") String guidStr);

}
