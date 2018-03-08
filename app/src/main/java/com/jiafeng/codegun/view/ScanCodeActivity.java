package com.jiafeng.codegun.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.BRMicro.Tools;
import com.handheld.uhfr.UHFRManager;
import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckModel;
import com.jiafeng.codegun.adapter.EPCadapter;
import com.jiafeng.codegun.adapter.EpcDataModel;
import com.jiafeng.codegun.util.OkManager;
import com.jiafeng.codegun.util.Util;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScanCodeActivity extends AppCompatActivity {
    private TextView tvTagSum;//tag sum text view
    private Button btnStart;//inventory button
    private Button btnUpload;
    private View back;
    public static UHFRManager mUhfrManager;//uhf
    private boolean isRunning = false;
    private boolean isStart = false;
    private Set<String> epcSet = null; //store different EPC
    private List<EpcDataModel> listEpc = null;//EPC list
    private Map<String, Integer> mapEpc = null; //store EPC position
    private EPCadapter adapter;//epc list adapter
    private ListView lvEpc;
    private long lastTime = 0L;// record play sound time

    private boolean isMulti = false;// multi mode flag
    private int allCount = 0;// inventory count
    String epc = "";
    String tid = "";
    private boolean keyControl = true;

    static String NEW_CHECK_MODEL = "new_check_model";

    CheckModel model;
    TextView sheetNo;
    TextView shopName;
    TextView storeName;

    public static Intent getCallIntent(Context context, CheckModel model) {
        return new Intent(context, ScanCodeActivity.class)
                .putExtra(NEW_CHECK_MODEL, model);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inventory);

        model = getIntent().getParcelableExtra(NEW_CHECK_MODEL);

        if (mUhfrManager == null) {
            mUhfrManager = UHFRManager.getIntance(this);// Init Uhf module
            if (mUhfrManager != null)
                mUhfrManager.setRegion(Reader.Region_Conf.valueOf(1));
        }

        Util.initSoundPool(this);//Init sound pool

        initView();
        initData();
    }

    private void initData() {
        sheetNo.setText(model.sheetNo);
        shopName.setText(model.shopName);
        storeName.setText(model.storeName);

        if (model.checkNum != null) {
            tvTagSum.setText(model.checkNum);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopScan();
                finish();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runInventory();
            }
        });
        final OkManager manager = OkManager.getInstance();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("mobileNum", "13900001111");
//                map.put("type", "1");
//                manager.sendComplexForm(Api.SmsValidateCode, map, new OkManager.Fun4() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        Log.i("ysq", jsonObject.toString());
//                    }
//                });

                stopScan();

                new AlertDialog.Builder(
                        ScanCodeActivity.this)
                        .setTitle("本次盘点情况如下")
                        .setMessage("提交盘点单数据，生产盘点结果")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // 这里是你点击确定之后可以进行的操作
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    private void initView() {
        lvEpc = findViewById(R.id.listView_epc);
        btnStart = findViewById(R.id.button_start);
        btnUpload = findViewById(R.id.button_save);
        tvTagSum = findViewById(R.id.textView_tag);
        back = findViewById(R.id.back);
        sheetNo = findViewById(R.id.sheetNo);
        shopName = findViewById(R.id.shopName);
        storeName = findViewById(R.id.storeName);
    }

    private void runInventory() {
        if (keyControl) {
            keyControl = false;
            if (!isStart) {
                mUhfrManager.setCancleInventoryFilter();
                isRunning = true;
                if (isMulti) {
                    mUhfrManager.setFastMode();
                    mUhfrManager.asyncStartReading();
                } else {
                    mUhfrManager.setCancleFastMode();
                }
                new Thread(inventoryTask).start();
                btnStart.setText(getResources().getString(R.string.stop_inventory_epc));
//            Log.e("inventoryTask", "start inventory") ;
                isStart = true;
            } else {
                if (isMulti)
                    mUhfrManager.asyncStopReading();
                else
                    mUhfrManager.stopTagInventory();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isRunning = false;
                btnStart.setText(getResources().getString(R.string.start_inventory_epc));
                isStart = false;
            }
            keyControl = true;
        }
    }

    //inventory epc
    private Runnable inventoryTask = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                if (isStart) {
                    List<Reader.TAGINFO> list1;
                    list1 = mUhfrManager.tagEpcTidInventoryByTimer((short) 50);
                    if (list1 != null && list1.size() > 0) {//
                        for (Reader.TAGINFO tfs : list1) {
                            byte[] epcdata = tfs.EpcId;
                            epc = Tools.Bytes2HexString(epcdata, epcdata.length);
                            int rssi = tfs.RSSI;
                            tid = Tools.Bytes2HexString(tfs.EmbededData, tfs.EmbededData.length);

                            Message msg = new Message();
                            msg.what = 1;
                            Bundle b = new Bundle();
                            b.putString("epc", epc);
                            b.putString("rssi", rssi + "");
                            b.putString("tid", tid);
                            msg.setData(b);
                            handler.sendMessage(msg);
                        }
                    }
                }
            }
        }
    };

    //handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String epc = msg.getData().getString("epc");
                    String rssi = msg.getData().getString("rssi");
                    String tid = msg.getData().getString("tid");
                    if (epc == null || epc.length() == 0) {
                        return;
                    }
                    int position;
                    allCount++;

                    if (epcSet == null) {//first add
                        epcSet = new HashSet<String>();
                        listEpc = new ArrayList<EpcDataModel>();
                        mapEpc = new HashMap<String, Integer>();
                        epcSet.add(epc);
                        mapEpc.put(epc, 0);
                        EpcDataModel epcTag = new EpcDataModel();
                        epcTag.setepc(epc);
                        epcTag.setrssi(rssi);
                        epcTag.setCount(1);
                        epcTag.setTid(tid);
                        listEpc.add(epcTag);
                        adapter = new EPCadapter(ScanCodeActivity.this, listEpc);
                        lvEpc.setAdapter(adapter);
                        Util.play(1, 0);
                        //mSetEpcs = epcSet;
                    } else {
                        if (epcSet.contains(epc)) {//set already exit
                            position = mapEpc.get(epc);
                            EpcDataModel epcOld = listEpc.get(position);
                            epcOld.setCount(epcOld.getCount() + 1);
                            if (epcOld.getTid() == "")
                                epcOld.setTid(tid);
                            listEpc.set(position, epcOld);
                        } else {
                            epcSet.add(epc);
                            mapEpc.put(epc, listEpc.size());
                            EpcDataModel epcTag = new EpcDataModel();
                            epcTag.setepc(epc);
                            epcTag.setrssi(rssi);
                            epcTag.setCount(1);
                            epcTag.setTid(tid);
                            listEpc.add(epcTag);

                            if (System.currentTimeMillis() - lastTime > 100) {
                                lastTime = System.currentTimeMillis();
                                Util.play(1, 0);
                            }
                        }

                        tvTagSum.setText("" + listEpc.size());
                        adapter.notifyDataSetChanged();

                    }

                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (mUhfrManager == null) {
            mUhfrManager = UHFRManager.getIntance(this);// Init Uhf module
            if (mUhfrManager != null)
                mUhfrManager.setRegion(Reader.Region_Conf.valueOf(1));
        } else {

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {//点击的是返回键
            stopScan();
        }
        return super.dispatchKeyEvent(event);
    }

    public void stopScan() {
        if (isStart) {
            if (isMulti)
                mUhfrManager.asyncStopReading();
            else
                mUhfrManager.stopTagInventory();
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isRunning = false;
            btnStart.setText(getResources().getString(R.string.start_inventory_epc));
            isStart = false;
        }

    }

}
