package com.jiafeng.codegun.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckAdapter;
import com.jiafeng.codegun.adapter.CheckModel;
import com.jiafeng.codegun.adapter.MyDividerItemDecoration;
import com.jiafeng.codegun.adapter.SpaceDivider;
import com.jiafeng.codegun.http.BaseRetrofit;
import com.jiafeng.codegun.http.HttpPostService;
import com.jiafeng.codegun.http.RetrofitEntity;
import com.jiafeng.codegun.util.Util;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckListActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    TextView createTv;
    TextView quitTv;
    CheckAdapter mAdapter;
    ArrayList<CheckModel> models = new ArrayList<>();

    long exitTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        initView();

//        getListData();

        initData();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceDivider(60));

        models = getData();
        mAdapter = new CheckAdapter(models);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(mAdapter.getSwipeOnItemListener());

        mAdapter.setOnItemClickListener(new CheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (models.get(position).isCompile()) {
                    startActivity(new Intent(CheckListActivity.this, CheckDetailActivity.class));
                } else {
                    startActivity(ScanCodeActivity.getCallIntent(CheckListActivity.this, models.get(position)));
                }
            }

            @Override
            public void onItemDelete(int position) {
                models.remove(position);
                mAdapter.setModels(models);
                mAdapter.notifyDataSetChanged();
            }
        });

        createTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckListActivity.this, NewCheckActivity.class));
            }
        });

        quitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(
                        CheckListActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("确定要注销吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        quitTv = findViewById(R.id.quitTv);
        createTv = findViewById(R.id.createTv);
    }

    private ArrayList<CheckModel> getData() {
        ArrayList<CheckModel> data = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            CheckModel model = new CheckModel();
            model.checkNum = "1" + i;
            model.shopName = "佳峰集团" + i;
            model.createTime = "14:11";
            model.sheetNo = "1111111" + i;
            model.storeName = i + "2柜台";
            if (i % 4 == 0)
                model.sheetStatus = 2;
            else
                model.sheetStatus = 1;
            data.add(model);
        }
        return data;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getListData() {
        Retrofit retrofit = BaseRetrofit.getInstance();

        final ProgressDialog pd = new ProgressDialog(this);
        HttpPostService apiService = retrofit.create(HttpPostService.class);
        Observable<RetrofitEntity> observable = apiService.getAllVedioBy(true);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<RetrofitEntity>() {
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
                            public void onNext(RetrofitEntity retrofitEntity) {
                                Log.e("ysq", retrofitEntity.getData().toString());
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                pd.show();
                            }
                        }

                );
    }
}
