package com.jiafeng.codegun.view;

import android.app.ProgressDialog;
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
import com.jiafeng.codegun.model.CheckModel;
import com.jiafeng.codegun.model.StoreModel;
import com.jiafeng.codegun.customzie.multiselect.widget.MultiSelectPopupWindows;
import com.jiafeng.codegun.http.BaseRetrofit;
import com.jiafeng.codegun.http.HttpPostService;
import com.jiafeng.codegun.model.StoreList;
import com.jiafeng.codegun.util.ShareHelper;
import com.jiafeng.codegun.util.StringUtils;
import com.jiafeng.codegun.util.ToastMaker;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewCheckActivity extends AppCompatActivity {
    Button nextBtn;
    TextView shopName;
    TextView shopCode;
    TextView storeTv;
    ImageView image;
    View back;
    RelativeLayout productType;
    LinearLayout storeNameLly;

    Retrofit retrofit;
    String companyNo;
    String sn;
    StringBuilder storeId;

    StoreList storeList;
    private List<StoreModel> stores;
    private List<StoreModel> selectStores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_check);

        initView();
        initData();
    }

    private void initData() {
        companyNo = ShareHelper.getInstance().getString(ShareHelper.KEY_COMPANY_NO, null);
        sn = ShareHelper.getInstance().getString(ShareHelper.KEY_SN, null);

        retrofit = BaseRetrofit.getInstance();
        final ProgressDialog pd = new ProgressDialog(this);
        HttpPostService apiService = retrofit.create(HttpPostService.class);
        Observable<StoreList> observable = apiService.getAssistInfo(companyNo, sn);
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
                                   storeList = model;
                                   stores = model.getStoreInfo();
                                   shopCode.setText(model.getShopInfo().getCompanyNo());
                                   shopName.setText(model.getShopInfo().getShopName());
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
        productType = findViewById(R.id.productType);
        storeNameLly = findViewById(R.id.storeNameLly);
        storeTv = findViewById(R.id.storeTv);
        shopCode = findViewById(R.id.shopCode);
        image = findViewById(R.id.image);
        nextBtn = findViewById(R.id.nextBtn);
        back = findViewById(R.id.back);
        shopName = findViewById(R.id.shopName);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.isEmpty(storeTv.getText().toString())) {
                    ToastMaker.show(NewCheckActivity.this, "请选择柜台");
                    return;
                }

                final CheckModel model = new CheckModel();
                model.shopName = storeList.getShopInfo().getShopName();
                model.sheetNo = storeList.getShopInfo().getCompanyNo();
                model.storeName = storeTv.getText().toString();

                final ProgressDialog pd = new ProgressDialog(NewCheckActivity.this);
                HttpPostService apiService = retrofit.create(HttpPostService.class);
                Observable<StoreList> observable = apiService.createGoodsCheck(companyNo, sn, storeList.getShopInfo().getAreaCode(), storeId.toString(), "创建订单");
                observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<StoreList>() {
                                       @Override
                                       public void onCompleted() {
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           if (pd != null && pd.isShowing()) {
                                               pd.dismiss();
                                           }
                                           if (e instanceof SocketTimeoutException) {
                                               ToastMaker.show(NewCheckActivity.this, "网络不给力");
                                           }
                                       }

                                       @Override
                                       public void onNext(StoreList s) {
                                           if (pd != null && pd.isShowing()) {
                                               pd.dismiss();
                                           }

                                           model.createTime = s.getSheetInfo().getCreateTime();
                                           model.sheetNo = s.getSheetInfo().getSheetNo();
                                           model.sheetStatus = Integer.valueOf(s.getSheetInfo().getSheetStatus());
                                           model.id = s.getSheetInfo().getId();
                                           startActivity(ChengWeiScanActivity.getCallIntent(NewCheckActivity.this, model, false));
                                           finish();
                                       }

                                       @Override
                                       public void onStart() {
                                           super.onStart();
                                           if (pd != null)
                                               pd.show();
                                       }
                                   }
                        );
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
                if (stores == null)
                    return;
                MultiSelectPopupWindows productsMultiSelectPopupWindows = new MultiSelectPopupWindows(NewCheckActivity.this, storeNameLly, 230, stores);
                image.setImageResource(R.drawable.push);
                productsMultiSelectPopupWindows.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        image.setImageResource(R.drawable.pull);

                        selectStores = new ArrayList<>();

                        for (StoreModel model : stores) {
                            if (model.isChecked()) {
                                selectStores.add(model);
                            }
                        }

                        StringBuilder s = new StringBuilder();
                        storeId = new StringBuilder();

                        if (selectStores.size() == stores.size()) {
                            s.append("全部柜台");
                            storeId.append("-1");
                        } else {
                            for (StoreModel d : selectStores) {
                                s.append(d.getStoreName());
                                storeId.append(d.getId());
                                if (d != selectStores.get(selectStores.size() - 1)) {
                                    s.append(",");
                                    storeId.append(",");
                                }
                            }
                        }

                        storeTv.setText(s);
                    }
                });
            }
        });
    }
}
