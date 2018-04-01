package com.jiafeng.codegun.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.adapter.CheckAdapter;
import com.jiafeng.codegun.adapter.SpaceDivider;
import com.jiafeng.codegun.base.BaseApplication;
import com.jiafeng.codegun.base.RealmOperationHelper;
import com.jiafeng.codegun.http.BaseRetrofit;
import com.jiafeng.codegun.http.HttpPostService;
import com.jiafeng.codegun.model.CheckModel;
import com.jiafeng.codegun.model.StoreList;
import com.jiafeng.codegun.refresh.Footer.LoadingView;
import com.jiafeng.codegun.refresh.RefreshListenerAdapter;
import com.jiafeng.codegun.refresh.TwinklingRefreshLayout;
import com.jiafeng.codegun.refresh.header.SinaRefreshView;
import com.jiafeng.codegun.util.ShareHelper;
import com.jiafeng.codegun.util.ToastMaker;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;
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
    TwinklingRefreshLayout refreshLayout;
    ArrayList<CheckModel> models = new ArrayList<>();

    long exitTime = 0L;
    Retrofit retrofit;
    String sn;
    HttpPostService apiService;
    ProgressDialog pd;

    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        initView();
        initData();
        initAdapterData();

        if (models != null && models.size() > 0) {
            RealmOperationHelper.getInstance(BaseApplication.REALM_INSTANCE).add(models);
        }

    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceDivider(60));

        mAdapter = new CheckAdapter(models);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(mAdapter.getSwipeOnItemListener());

        retrofit = BaseRetrofit.getInstance();
        pd = new ProgressDialog(this);
        apiService = retrofit.create(HttpPostService.class);

        sn = ShareHelper.initInstance(this).getString(ShareHelper.KEY_SN, "");

        mAdapter.setOnItemClickListener(new CheckAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (models.get(position).sheetStatus == 3) {
                    startActivity(CheckDetailActivity.getCallIntent(CheckListActivity.this, models.get(position)));
                } else if (models.get(position).sheetStatus == 2) {
                    ToastMaker.show(CheckListActivity.this, "盘点结果产生中,请稍等");
                    requestData(3, page);
                } else {
                    startActivity(ChengWeiScanActivity.getCallIntent(CheckListActivity.this, models.get(position), true));
                }
            }

            @Override
            public void onItemDelete(final int position) {
                Observable<StoreList> observable = apiService.deleteGoodsCheck(models.get(position).companyNo,
                        models.get(position).id, sn);
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
                                       public void onNext(StoreList storeList) {
                                           ToastMaker.show(CheckListActivity.this, storeList.getMsg());
                                           if (storeList.isSuccess()) {
                                               models.remove(position);
                                               mAdapter.setModels(models);
                                               mAdapter.notifyDataSetChanged();
                                               RealmOperationHelper.getInstance(BaseApplication.REALM_INSTANCE).deleteElement(CheckModel.class, position);
                                           }
                                       }

                                       @Override
                                       public void onStart() {
                                           super.onStart();
                                           pd.show();
                                       }
                                   }
                        );


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
                                ShareHelper.getInstance().clearData();
                                RealmOperationHelper.getInstance(BaseApplication.REALM_INSTANCE).deleteAll(CheckModel.class);
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


        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setArrowResource(R.drawable.arrow);
        headerView.setTextColor(0xff745D5C);
        refreshLayout.setHeaderView(headerView);

        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                page = 1;
                requestData(2, page);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                page = page + 1;
                requestData(4, page);
            }
        });

    }

    public void refresh(int flag) {
        if (flag == 2)
            refreshLayout.finishRefreshing();
        if (flag == 4)
            refreshLayout.finishLoadmore();

    }

    public void requestData(final int flag, final int page) {
        Observable<StoreList> observable = apiService.getGoodsCheckList(sn, "20", String.valueOf(page));
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
                                   refresh(flag);

                                   if (e instanceof SocketTimeoutException) {
                                       ToastMaker.show(CheckListActivity.this, "网络不给力");
                                   }
                               }

                               @Override
                               public void onNext(StoreList storeList) {
                                   if (page == 1) {
                                       if (models != null && models.size() > 0)
                                           models.clear();
                                       models = (ArrayList<CheckModel>) storeList.getGoodscheck();
                                   } else {
                                       for (CheckModel d : storeList.getGoodscheck()) {
                                           models.add(d);
                                       }
                                   }

                                   mAdapter.setModels(models);
                                   mAdapter.notifyDataSetChanged();
                                   refresh(flag);

                                   if (storeList.getGoodscheck().size() < 20) {
                                       refreshLayout.setEnableLoadmore(false);
                                   } else {
                                       refreshLayout.setEnableLoadmore(true);
                                   }
                               }

                               @Override
                               public void onStart() {
                                   super.onStart();
                                   if (flag == 1) {
                                       pd.show();
                                   }
                               }
                           }
                );
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        quitTv = findViewById(R.id.quitTv);
        createTv = findViewById(R.id.createTv);
        refreshLayout = findViewById(R.id.refresh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pd != null && mAdapter != null) {
            page = 1;
            requestData(1, page);
        }
    }

    public void initAdapterData() {
        final RealmResults<CheckModel> results = (RealmResults<CheckModel>) RealmOperationHelper.getInstance(BaseApplication.REALM_INSTANCE).queryAllAsync(CheckModel.class);
        results.addChangeListener(new RealmChangeListener<RealmResults<CheckModel>>() {
            @Override
            public void onChange(RealmResults<CheckModel> element) {
                //只要results改变就会回调，及时取消监听
                results.removeAllChangeListeners();
                //获取数据，更新UI。
                models.clear();
                for (CheckModel model : element) {
                    models.add(model);
                }
                mAdapter.setModels(models);
                mAdapter.notifyDataSetChanged();
            }
        });
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

}
