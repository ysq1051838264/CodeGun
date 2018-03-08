package com.jiafeng.codegun.swipeitemview;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by hdr on 15/9/10.
 */
public class SwipeOnItemListener implements RecyclerView.OnItemTouchListener, SwipeItemView.OnSlideListener {
    private SwipeItemView curSwipeItemView;

    boolean hitDelete = false;
    OnDeleteListener mOnDeleteListener;

    boolean isSliding = false;//正在滑动

    @Override
    public void onSlide(SwipeItemView swipeItemView, int status) {
        switch (status) {
            case SLIDE_STATUS_ON:
                endSliding();
                break;
            case SLIDE_STATUS_OFF:
                reset();
                break;
            case SLIDE_STATUS_START_SCROLL:
                startSliding(swipeItemView);
                break;
        }
    }

    public interface OnDeleteListener {
        void onDeleteClick(SwipeItemView curSwipeItemView);
    }

    public void startSliding(SwipeItemView curSwipeItemView) {
        this.curSwipeItemView = curSwipeItemView;
        isSliding = true;
    }

    public void endSliding() {
        isSliding = false;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.mOnDeleteListener = onDeleteListener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (curSwipeItemView != null) {
            handle(recyclerView, motionEvent);
        }
        return curSwipeItemView != null;
    }

    void handle(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (curSwipeItemView == null) {
            return;
        }
        if (isSliding) {
            final float offsetX = recyclerView.getScrollX() - curSwipeItemView.getLeft();
            final float offsetY = recyclerView.getScrollY() - curSwipeItemView.getTop();
            motionEvent.offsetLocation(offsetX, offsetY);
            curSwipeItemView.onTouchEvent(motionEvent);
        } else {
            View touchedView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (touchedView == curSwipeItemView) {
                        final float offsetX = recyclerView.getScrollX() - touchedView.getLeft();
                        final float offsetY = recyclerView.getScrollY() - touchedView.getTop();
                        motionEvent.offsetLocation(offsetX, offsetY);
                        hitDelete = curSwipeItemView.onTouchWhenExtendState(motionEvent);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (hitDelete) {
                        mOnDeleteListener.onDeleteClick(curSwipeItemView);
                    } else {
                        shrink();
                    }
                    break;
            }
        }

    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        handle(recyclerView, motionEvent);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void shrink() {
        hitDelete = false;
        if (curSwipeItemView != null) {
            curSwipeItemView.shrink();
            curSwipeItemView = null;
        }
    }

    public void reset() {
        hitDelete = false;
        if (curSwipeItemView != null) {
            curSwipeItemView.hidHolder();
            curSwipeItemView = null;
        }
    }
}
