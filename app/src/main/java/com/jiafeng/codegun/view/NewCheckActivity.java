package com.jiafeng.codegun.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckModel;
import com.jiafeng.codegun.customzie.NiceSpinner;
import com.jiafeng.codegun.customzie.multiselect.model.StoreModel;
import com.jiafeng.codegun.customzie.multiselect.widget.MultiSelectPopupWindows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NewCheckActivity extends AppCompatActivity {
    Button nextBtn;
    TextView shopName;
    TextView storeTv;
    ImageView image;
    View back;
    RelativeLayout productType;

    private List<StoreModel> stores;
    private List<StoreModel> selectStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check);

        initView();
        initData();
        getData();
    }

    private void initData() {
//        String imeis = ShareHelper.getInstance().getString(ShareHelper.KEY_IMEI,null);
//        String macs = ShareHelper.getInstance().getString(ShareHelper.KEY_MAC,null);
//
//        Toast.makeText(NewCheckActivity.this, "mac地址是： " + macs + "   imei是：  " + imeis    , Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        productType = findViewById(R.id.productType);
        storeTv = findViewById(R.id.storeTv);
        image = findViewById(R.id.image);
        nextBtn = findViewById(R.id.nextBtn);
        back = findViewById(R.id.back);
        shopName = findViewById(R.id.shopName);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckModel model = new CheckModel();
                model.shopName = "1号店";
                model.sheetNo = "1111111";
                model.storeName = "2柜台";

                startActivity(ChengWeiScanActivity.getCallIntent(NewCheckActivity.this, model));
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        productType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiSelectPopupWindows productsMultiSelectPopupWindows = new MultiSelectPopupWindows(NewCheckActivity.this, productType, 230, stores);
                image.setImageResource(R.drawable.push);
                productsMultiSelectPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        image.setImageResource(R.drawable.pull);

                        selectStores = new ArrayList<>();
                        StringBuilder s = new StringBuilder();
                        for (StoreModel d : stores) {
                            if (d.isChecked()) {
                                selectStores.add(d);
                                s.append(d.getStoreName()).append(",");
                            }
                        }

                        storeTv.setText(s);
                    }
                });
            }
        });
    }


    private void getData() {
        stores = new ArrayList<>();
        stores.add(new StoreModel("珠宝玉石1", false, "0"));
        stores.add(new StoreModel("珠宝玉石2", false, "1"));
        stores.add(new StoreModel("珠宝玉石3", false, "2"));
        stores.add(new StoreModel("珠宝玉石4", false, "3"));
        stores.add(new StoreModel("珠宝玉石5", false, "4"));
        stores.add(new StoreModel("珠宝玉石6", false, "5"));
        stores.add(new StoreModel("珠宝玉石7", false, "6"));
    }
}
