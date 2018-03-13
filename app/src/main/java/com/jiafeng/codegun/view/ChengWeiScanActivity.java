package com.jiafeng.codegun.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckModel;
import com.jiafeng.codegun.adapter.EPCadapter;
import com.jiafeng.codegun.adapter.EpcDataModel;
import com.jiafeng.codegun.customzie.multiselect.model.StoreModel;
import com.jiafeng.codegun.customzie.multiselect.widget.MultiSelectPopupWindows;
import com.jiafeng.codegun.util.SoundManage;
import com.jiafeng.codegun.util.StringUtils;
import com.rscja.deviceapi.RFIDWithUHF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yangshuquan on 2018/3/8.
 */

public class ChengWeiScanActivity extends AppCompatActivity {
    public RFIDWithUHF mReader;
    public boolean loopFlag = false;
    Handler handler;

    View back;
    CheckModel model;
    TextView sheetNo;
    TextView shopName;
    TextView storeName;
    TextView tagSum;
    Button startBtn;
    Button saveBtn;
    ListView LvTags;
    SeekBar seekbar;
    TextView nameTv;

    static String NEW_CHECK_MODEL = "new_check_model";
    private ArrayList<HashMap<String, String>> tagList;
    private List<EpcDataModel> listEpc = null;
    private HashMap<String, String> map;

    private EPCadapter adapter;

    int value = 30;

    public static Intent getCallIntent(Context context, CheckModel model) {
        return new Intent(context, ChengWeiScanActivity.class)
                .putExtra(NEW_CHECK_MODEL, model);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_chengwei);

        model = getIntent().getParcelableExtra(NEW_CHECK_MODEL);
        tagList = new ArrayList<HashMap<String, String>>();
        listEpc = new ArrayList<EpcDataModel>();

        try {
            mReader = RFIDWithUHF.getInstance();
        } catch (Exception ex) {
            toastMessage(ex.getMessage());
            return;
        }

        if (mReader != null) {
            new InitTask().execute();
        }

        initView();
        initData();
    }

    private void initData() {
        sheetNo.setText(model.sheetNo);
        shopName.setText(model.shopName);
        storeName.setText(model.storeName);

       // mReader.setPower(value);

        if (model.checkNum != null) {
            tagSum.setText(model.checkNum);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopInventory();
                finish();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopInventory();
                finish();
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readTag();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.obj + "";
                String[] strs = result.split("@");
                addEPCToList(strs[0], strs[1], strs[2]);
            }
        };


    }

    private void initView() {
        LvTags = findViewById(R.id.listView_epc);
        saveBtn = findViewById(R.id.saveBtn);
        startBtn = findViewById(R.id.startBtn);
        tagSum = findViewById(R.id.tagSum);
        back = findViewById(R.id.back);
        sheetNo = findViewById(R.id.sheetNo);
        shopName = findViewById(R.id.shopName);
        storeName = findViewById(R.id.storeName);

        seekbar = findViewById(R.id.seekbar);
        nameTv = findViewById(R.id.nameTv);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nameTv.setText(String.valueOf(i));
                value = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mReader != null && value > 10) {
                    if (mReader.setPower(value)) {
                        Toast.makeText(ChengWeiScanActivity.this,"范围设置成功",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChengWeiScanActivity.this,"范围设置失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void readTag() {
        // 识别标签
        if (startBtn.getText().equals(getString(R.string.btInventory)) && mReader.openInventoryEPCAndTIDMode()) {
            if (mReader.startInventoryTag((byte) 0, (byte) 0)) {
                startBtn.setText(getString(R.string.title_stop_Inventory));
                loopFlag = true;
                new TagThread(10).start();
            } else {
                mReader.stopInventory();
//                UIHelper.ToastMessage(mContext,R.string.uhf_msg_inventory_open_fail); mContext.playSound(2);
            }
        } else {
            stopInventory();
        }
    }

    /**
     * 停止识别
     */
    private void stopInventory() {
        if (loopFlag) {
            loopFlag = false;
            if (mReader.stopInventory()) {
                startBtn.setText(getString(R.string.btInventory));
            } else {
//                UIHelper.ToastMessage(mContext,R.string.uhf_msg_inventory_stop_fail);
            }
        }
    }

    /**
     * 添加EPC到列表中
     *
     * @param epc
     */
    private void addEPCToList(String tid, String epc, String rssi) {
        if (!TextUtils.isEmpty(epc)) {
            int index = checkIsExist(epc);

            map = new HashMap<String, String>();
            map.put("tagUii", epc);
            map.put("tagCount", String.valueOf(1));
            map.put("tagRssi", rssi);

            if (index == -1) {
                tagList.add(map);
//                LvTags.setAdapter(adapter);
                SoundManage.PlaySound(getBaseContext(), SoundManage.SoundType.SUCCESS);

                EpcDataModel epcTag = new EpcDataModel();
                epcTag.setepc(epc);
                epcTag.setrssi(rssi);
                epcTag.setCount(1);
                epcTag.setTid(tid);
                listEpc.add(epcTag);
                adapter = new EPCadapter(ChengWeiScanActivity.this, listEpc);
                LvTags.setAdapter(adapter);

                tagSum.setText("" + adapter.getCount());
            } else {
                int tagcount = Integer.parseInt(tagList.get(index).get("tagCount"), 10) + 1;
                map.put("tagCount", String.valueOf(tagcount));
                tagList.set(index, map);
            }

            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 判断EPC是否在列表中
     *
     * @param strEPC 索引
     * @return
     */
    public int checkIsExist(String strEPC) {
        int existFlag = -1;
        if (StringUtils.isEmpty(strEPC)) {
            return existFlag;
        }

        String tempStr = "";
        for (int i = 0; i < tagList.size(); i++) {
            HashMap<String, String> temp = new HashMap<String, String>();
            temp = tagList.get(i);

            tempStr = temp.get("tagUii");
            if (strEPC.equals(tempStr)) {
                existFlag = i;
                break;
            }
        }

        return existFlag;
    }

    class TagThread extends Thread {
        private int mBetween = 80;
        HashMap<String, String> map;

        public TagThread(int iBetween) {
            mBetween = iBetween;
        }

        public void run() {
            String strTid;
            String strResult;

            String[] res = null;

            while (loopFlag) {
                res = mReader.readTagFromBuffer();//.readTagFormBuffer();

                if (res != null) {
                    strTid = res[0];
                    if (!strTid.equals("0000000000000000") && !strTid.equals("000000000000000000000000") && StringUtils.isNotEmpty(strTid)) {
                        strResult = "TID:" + strTid + "\n";
                    } else {
                        strResult = "";
                    }
                    Message msg = handler.obtainMessage();
                    msg.obj = strResult + "@" + "EPC:"
                            + mReader.convertUiiToEPC(res[1]) + "@"
                            + res[2];
                    handler.sendMessage(msg);
                    // SoundManage.PlaySound(getBaseContext(), SoundManage.SoundType.SUCCESS);
                }
                try {
                    sleep(mBetween);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 280) {
            if (event.getRepeatCount() == 0) {
                readTag();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mReader != null) {
            mReader.free();
        }
        super.onDestroy();
    }

    public class InitTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog mypDialog;

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            return mReader.init();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            mypDialog.cancel();

            if (!result) {
                Toast.makeText(ChengWeiScanActivity.this, "init fail",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mypDialog = new ProgressDialog(ChengWeiScanActivity.this);
            mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mypDialog.setMessage("init...");
            mypDialog.setCanceledOnTouchOutside(false);
            mypDialog.show();
        }
    }

    public void toastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
