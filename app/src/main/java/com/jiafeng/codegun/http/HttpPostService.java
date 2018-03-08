package com.jiafeng.codegun.http;

import com.jiafeng.codegun.adapter.CheckModel;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by yangshuquan on 2018/3/5.
 */

public interface HttpPostService {

    @POST("AppFiftyToneGraph/videoLink")
    Observable<RetrofitEntity> getAllVedioBy(@Body boolean once_no);

    @POST("updataCheck.do")
    @FormUrlEncoded
    Observable<CheckModel> upLoad(@Field("platformName") String platformName,
                                  @Field("versionCode") String versionCode,
                                  @Field("build") String build);
}
