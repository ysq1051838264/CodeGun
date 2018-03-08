package com.jiafeng.codegun.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yangshuquan on 2018/3/7.
 */

public class SpaceDivider extends RecyclerView.ItemDecoration {

    private int verticalSpace;
    private int horizontalSpace;

    public SpaceDivider(int verticalSpace, int horizontalSpace) {
        this.verticalSpace = verticalSpace;
        this.horizontalSpace = horizontalSpace;
    }

    public SpaceDivider(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, horizontalSpace, verticalSpace);
    }
}
