package com.jiafeng.codegun.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckDetailAdapter;
import com.jiafeng.codegun.adapter.CheckDetailModel;
import com.jiafeng.codegun.adapter.MyDividerItemDecoration;

import java.util.ArrayList;

public class CheckDetailActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    View back;
    Button deficitBtn;
    Button winBtn;
    CheckDetailAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);

        initView();

        initData();
    }

    private void initData() {
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deficitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setModels(getPkData());
                deficitBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.text_bg));
                winBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.text_666666));
            }
        });

        winBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setModels(getData());
                winBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.text_bg));
                deficitBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.text_666666));
            }
        });
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        winBtn = findViewById(R.id.winBtn);
        deficitBtn = findViewById(R.id.deficitBtn);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CheckDetailAdapter(getData());
    }

    private ArrayList<CheckDetailModel> getData() {
        ArrayList<CheckDetailModel> data = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            CheckDetailModel model = new CheckDetailModel();
            model.barCode = "Mt1000213";
            model.oldBarCode = "Mt1000213";
            model.goodsName= "18K男戒";
            data.add(model);
        }

        return data;
    }

    private ArrayList<CheckDetailModel> getPkData() {
        ArrayList<CheckDetailModel> data = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            CheckDetailModel model = new CheckDetailModel();
            model.barCode = "Mt1000214";
            model.oldBarCode = "Mt1000214";
            model.goodsName= "18K女戒";
            data.add(model);
        }

        return data;
    }

}
