package com.jiafeng.codegun.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckModel;
import com.jiafeng.codegun.customzie.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NewCheckActivity extends AppCompatActivity {
    NiceSpinner niceSpinner;
    Button nextBtn;
    TextView shopName;
    View back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check);

        initView();
        initData();
    }

    private void initData() {
//        String imeis = ShareHelper.getInstance().getString(ShareHelper.KEY_IMEI,null);
//        String macs = ShareHelper.getInstance().getString(ShareHelper.KEY_MAC,null);
//
//        Toast.makeText(NewCheckActivity.this, "mac地址是： " + macs + "   imei是：  " + imeis    , Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        niceSpinner = findViewById(R.id.nice_spinner);
        nextBtn = findViewById(R.id.nextBtn);
        List<String> data = new LinkedList<>(Arrays.asList("柜台1", "柜台2", "柜台3", "柜台4", "柜台5"));
        niceSpinner.attachDataSource(data);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckModel model = new CheckModel();
                model.shopName = "1号店";
                model.sheetNo = "1111111";
                model.storeName = "2柜台";

                startActivity(ScanCodeActivity.getCallIntent(NewCheckActivity.this,model));
                finish();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
