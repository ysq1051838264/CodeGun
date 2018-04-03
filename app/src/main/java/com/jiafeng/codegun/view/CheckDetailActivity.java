package com.jiafeng.codegun.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckDetailAdapter;
import com.jiafeng.codegun.base.BaseApplication;
import com.jiafeng.codegun.base.RealmOperationHelper;
import com.jiafeng.codegun.model.CheckDetailModel;
import com.jiafeng.codegun.adapter.MyDividerItemDecoration;
import com.jiafeng.codegun.http.BaseRetrofit;
import com.jiafeng.codegun.http.HttpPostService;
import com.jiafeng.codegun.model.CheckModel;
import com.jiafeng.codegun.model.SheetInfo;
import com.jiafeng.codegun.model.StoreList;
import com.jiafeng.codegun.util.DateUtils;
import com.jiafeng.codegun.util.ShareHelper;
import com.jiafeng.codegun.util.StringUtils;
import com.jiafeng.codegun.util.ToastMaker;

import java.net.SocketTimeoutException;
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
    TextView sheetNo;
    TextView shopName;
    TextView spNum;
    TextView pyNum;
    TextView pkNum;
    TextView ypNum; //已盘数量
    TextView wzNum;
    TextView bookNum;
    TextView storeName;
    TextView createTime;
    LinearLayout storeLly;

    CheckDetailAdapter mAdapter;
    LinearLayoutManager mLayoutManager;

    static String NEW_CHECK_MODEL = "new_check_model";

    CheckModel model;

    String storeNames = "";

    ArrayList<CheckDetailModel> pklist;  //盘亏列表
    ArrayList<CheckDetailModel> pylist;  //盘盈列表

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
        pklist = new ArrayList<>();
        pylist = new ArrayList<>();

        mAdapter = new CheckDetailAdapter(pylist);

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
                mAdapter.setModels(pklist);
                deficitBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.white));
                deficitBtn.setBackgroundColor(CheckDetailActivity.this.getResources().getColor(R.color.text_bg));
//                winBtn.setBackgroundColor(CheckDetailActivity.this.getResources().getColor(R.color.white));
                winBtn.setBackground(CheckDetailActivity.this.getResources().getDrawable(R.drawable.btn_bg_stroke));
                winBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.text_666666));
            }
        });

        storeLly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotEmpty(storeNames) && storeNames.length() > 10) {
                    new AlertDialog.Builder(
                            CheckDetailActivity.this)
                            .setTitle("柜台")
                            .setMessage(storeNames)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        winBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.setModels(pylist);
                winBtn.setTextColor(CheckDetailActivity.this.getResources().getColor(R.color.white));
                winBtn.setBackgroundColor(CheckDetailActivity.this.getResources().getColor(R.color.text_bg));
                deficitBtn.setBackground(CheckDetailActivity.this.getResources().getDrawable(R.drawable.btn_bg_stroke));
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

                                   if (e instanceof SocketTimeoutException) {
                                       ToastMaker.show(CheckDetailActivity.this, "网络不给力");
                                   }
                               }

                               @Override
                               public void onNext(StoreList d) {
                                   pklist = (ArrayList<CheckDetailModel>) d.getCheckResInfo().getPklist();
                                   pylist = (ArrayList<CheckDetailModel>) d.getCheckResInfo().getPylist();

                                   mAdapter.setModels(pylist);

                                   SheetInfo sheetInfo = d.getSheetInfo();
                                   sheetNo.setText(sheetInfo.getSheetNo());
                                   shopName.setText(sheetInfo.getShopName());
                                   storeName.setText(sheetInfo.getStoreName());
                                   storeNames = sheetInfo.getStoreName();
                                   createTime.setText(DateUtils.dateToString(DateUtils.stringToDate(sheetInfo.getCreateTime(), DateUtils.FORMAT_LONG), DateUtils.FORMAT_LONG));

                                   spNum.setText(d.getCheckResInfo().getSpNum());
                                   bookNum.setText(d.getCheckResInfo().getBookNum());
                                   pyNum.setText(d.getCheckResInfo().getPyNum());
                                   pkNum.setText(d.getCheckResInfo().getPkNum());
                                   wzNum.setText(d.getCheckResInfo().getWzNum());
                                   ypNum.setText(d.getCheckResInfo().getYpNum());
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

        sheetNo = findViewById(R.id.sheetNo);
        shopName = findViewById(R.id.shopName);
        spNum = findViewById(R.id.spNum);
        pyNum = findViewById(R.id.pyNum);
        pkNum = findViewById(R.id.pkNum);
        wzNum = findViewById(R.id.wzNum);
        bookNum = findViewById(R.id.bookNum);
        createTime = findViewById(R.id.createTime);
        ypNum = findViewById(R.id.ypNum);
        storeName = findViewById(R.id.storeName);
        storeLly = findViewById(R.id.storeLly);

        model = getIntent().getParcelableExtra(NEW_CHECK_MODEL);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

}
