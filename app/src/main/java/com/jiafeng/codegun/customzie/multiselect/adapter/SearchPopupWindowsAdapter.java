package com.jiafeng.codegun.customzie.multiselect.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiafeng.codegun.R;
import com.jiafeng.codegun.model.StoreModel;


/**
 * Created by HZF on 2017/5/16.
 */

public class SearchPopupWindowsAdapter extends CommonBaseAdapter<StoreModel> {

    public SearchPopupWindowsAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_list_selector, viewGroup, false);
        if (view != null) {
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout_search);
            TextView textView = (TextView) view.findViewById(R.id.textView_listView_selector);
            final ImageView imageView = (ImageView) view.findViewById(R.id.image_search_check);
            imageView.setImageResource(itemList.get(i).isChecked() ? R.drawable.icon_selected : 0);
            textView.setText(itemList.get(i).getStoreName());
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.get(i).setChecked(itemList.get(i).isChecked() ? false : true);
                    imageView.setImageResource(itemList.get(i).isChecked() ? R.drawable.icon_selected : 0);
                }
            });
        }
        return view;
    }

}
