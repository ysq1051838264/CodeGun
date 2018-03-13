package com.jiafeng.codegun.customzie.multiselect.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.customzie.multiselect.adapter.SearchPopupWindowsAdapter;
import com.jiafeng.codegun.customzie.multiselect.model.StoreModel;
import com.jiafeng.codegun.customzie.multiselect.utils.DensityUtil;

import java.util.List;

/**
 * Created by HZF on 2017/3/1.
 */
public class MultiSelectPopupWindows extends PopupWindow {

    private Context context;
    private View parent;
    private List<StoreModel> data;
    private int yStart;
    private SearchPopupWindowsAdapter adapter;

    public MultiSelectPopupWindows(Context context, View parent, int yStart, List<StoreModel> data) {
        this.context = context;
        this.parent = parent;
        this.yStart = yStart;
        this.data = data;
        initView();
    }

    private void initView() {
        View view = View.inflate(context, R.layout.popupwindows_multiselect, null);
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_slow));
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_selector);
        linearLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.list_top_in));
        final ListView listView = (ListView) view.findViewById(R.id.listView_selector);
        Button selectData = view.findViewById(R.id.selectData);

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.TOP, 0, DensityUtil.dip2px(context, yStart));
        update();

        initListView(listView, data);

        selectData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (StoreModel d : data) {
                    d.setChecked(true);
                }
                initListView(listView, data);
            }
        });
    }

    private void initListView(ListView listView, List<StoreModel> data) {
        adapter = new SearchPopupWindowsAdapter(context);
        adapter.setItems(data);
        listView.setAdapter(adapter);
    }

    public List getItemList() {
        return adapter.getItemList();
    }

}