package com.jiafeng.codegun.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.jiafeng.codegun.R;
import com.jiafeng.codegun.http.BaseRetrofit;
import com.jiafeng.codegun.http.HttpPostService;
import com.jiafeng.codegun.http.RetrofitEntity;
import com.jiafeng.codegun.util.ShareHelper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData();
    }

    private void initData() {
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPhone();
            }
        });
    }

    private void requestPhone() {
        if (PermissionsUtil.hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            //有访问的权限
            jump();
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //用户授予了访问的权限
                    jump();
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    //用户拒绝了访问的申请
                }
            }, new String[]{Manifest.permission.READ_PHONE_STATE});
        }
    }

    public void jump() {
        String mac = getLocalMacAddress();
        String sn = getSerialNumber();
        String imei = getIMEI(LoginActivity.this);

        //Toast.makeText(this, "sn是：" + sn +"   imei"+imei, Toast.LENGTH_SHORT).show();

        ShareHelper shareHelper = ShareHelper.initInstance(this);
        shareHelper.setString(ShareHelper.KEY_IMEI, imei);
        shareHelper.setString(ShareHelper.KEY_MAC, mac);
        shareHelper.setString(ShareHelper.KEY_SN, sn);

//        startActivity(new Intent(LoginActivity.this, CheckListActivity.class));
//        finish();

        Retrofit retrofit =  BaseRetrofit.getInstance();

        final ProgressDialog pd = new ProgressDialog(this);
        HttpPostService apiService = retrofit.create(HttpPostService.class);
        Observable<RetrofitEntity> observable = apiService.checkDeviceInfo(sn);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RetrofitEntity>() {
                            @Override
                            public void onCompleted() {
                                if (pd != null && pd.isShowing()) {
                                    pd.dismiss();
                                }

                                startActivity(new Intent(LoginActivity.this, CheckListActivity.class));
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (pd != null && pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }

                            @Override
                            public void onNext(RetrofitEntity retrofitEntity) {
                                ShareHelper shareHelper = ShareHelper.initInstance(LoginActivity.this);
                                shareHelper.setString(ShareHelper.KEY_COMPANY_NO, retrofitEntity.getAreaInfo().getCompanyNo());
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                pd.show();
                            }
                        }

                );
    }


    public String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        }
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    private String getSerialNumber(){
        String serial = null;
        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serial;
    }


    public String getLocalMacAddress() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

}
