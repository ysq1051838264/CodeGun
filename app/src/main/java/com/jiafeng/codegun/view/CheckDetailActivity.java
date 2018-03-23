package com.jiafeng.codegun.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.jiafeng.codegun.http.BaseRetrofit;
import com.jiafeng.codegun.http.HttpPostService;
import com.jiafeng.codegun.model.CheckModel;
import com.jiafeng.codegun.model.StoreList;
import com.jiafeng.codegun.util.ShareHelper;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckDetailActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    View back;
    Button deficitBtn;
    Button winBtn;
    CheckDetailAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    static String NEW_CHECK_MODEL = "new_check_model";

    CheckModel model;

    public static Intent getCallIntent(Context context, CheckModel model) {
        return new Intent(context, CheckDetailActivity.class)
                .putExtra(NEW_CHECK_MODEL, model);
    }

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

        Retrofit retrofit = BaseRetrofit.getInstance();
        final ProgressDialog pd = new ProgressDialog(this);
        HttpPostService apiService = retrofit.create(HttpPostService.class);
        Observable<StoreList> observable = apiService.getGoodsCheckDtl(model.companyNo, model.id);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<StoreList>() {
                               @Override
                               public void onCompleted() {
                                   if (pd != null && pd.isShowing()) {
                                       pd.dismiss();
                                   }
                               }

                               @Override
                               public void onError(Throwable e) {
                                   if (pd != null && pd.isShowing()) {
                                       pd.dismiss();
                                   }
                               }

                               @Override
                               public void onNext(StoreList model) {

                               }

                               @Override
                               public void onStart() {
                                   super.onStart();
                                   pd.show();
                               }
                           }
                );
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        winBtn = findViewById(R.id.winBtn);
        deficitBtn = findViewById(R.id.deficitBtn);

        model = getIntent().getParcelableExtra(NEW_CHECK_MODEL);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CheckDetailAdapter(getData());
    }

    private ArrayList<CheckDetailModel> getData() {
        ArrayList<CheckDetailModel> data = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            CheckDetailModel model = new CheckDetailModel();
            model.barCode = "Mt1000213";
            model.oldBarCode = "Mt1000213";
            model.goodsName = "18K男戒";
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
            model.goodsName = "18K女戒";
            data.add(model);
        }

        return data;
    }

}
