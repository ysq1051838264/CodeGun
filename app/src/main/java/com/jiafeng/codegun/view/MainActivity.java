package com.jiafeng.codegun.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiafeng.codegun.adapter.MyDividerItemDecoration;
import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.ShopAdapter;
import com.jiafeng.codegun.adapter.ShopModel;
import com.jiafeng.codegun.util.OkManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TextView mangerTv;
    ShopAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mangerTv = findViewById(R.id.mangerTv);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ShopAdapter(getData());
        final OkManager manager = OkManager.getInstance();

        mangerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "下周开发：", Toast.LENGTH_SHORT).show();
            }
        });

        mAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("mobileNum", "13900001111");
//                map.put("type", "1");
//                manager.sendComplexForm(Api.SmsValidateCode, map, new OkManager.Fun4() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        Log.i("ysq", jsonObject.toString());
//                    }
//                });


                final String[] items = {"店员1", "店员2", "店员3"};
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("请选择店员")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(
                                        MainActivity.this)
                                        .setTitle("你选择了:" + items[which])
                                        .setMessage("是否进入盘点操作")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 这里是你点击确定之后可以进行的操作
                                                startActivity(new Intent(MainActivity.this, ScanCodeActivity.class));
                                            }
                                        })
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 这里点击取消之后可以进行的操作

                                            }
                                        }).show();
                            }
                        }).show();
            }
        });

        String mac = getLocalMacAddress();
        String imei = getIMEI(this);
        Toast.makeText(this, "mac是：" + mac, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<ShopModel> getData() {
        ArrayList<ShopModel> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            ShopModel shopModel = new ShopModel();
            shopModel.shopImage = "https://t12.baidu.com/it/u=1354128359,3793938699&fm=173&s=C3948C6450513FC016A85A850300908C&w=480&h=270&img.JPEG";
            shopModel.shopInfo = "广东省深圳市罗湖区";
            shopModel.shopName = "佳峰集团";
            data.add(shopModel);
        }

        return data;
    }


    public String getIMEI(Context context) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
        }

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        return imei;
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
