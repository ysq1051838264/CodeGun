package com.jiafeng.codegun.swipeitemview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.jiafeng.codegun.R;


/**
 * 带左滑删除的ItemView，适用与listView
 * Created by bill on 2015/6/4.
 */
public class ListViewSwipeItemView extends LinearLayout {

    private LinearLayout viewContent;
    private RelativeLayout holder;
    private Scroller scroller;
    private OnSlideListener onSlideListener;

    private int mHolderWidth;

    private int downX;

    private int mLastX = 0;
    private int mLastY = 0;
    private static final int TAN = 2;
    private boolean isHorizontalMove=true;//判断是否横滑

    private boolean isSlide; //判断是否可以右滑出删除按钮

    public interface OnSlideListener {
        int SLIDE_STATUS_OFF = 0;
        int SLIDE_STATUS_START_SCROLL = 1;
        int SLIDE_STATUS_ON = 2;

        /**
         * @param view
         *            current SlideView
         * @param status
         *            SLIDE_STATUS_ON or SLIDE_STATUS_OFF
         */
        void onSlide(View view, int status);
    }

    public ListViewSwipeItemView(Context context) {
        super(context);
        initView();
    }

    public ListViewSwipeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        // 初始化弹性滑动对象
        scroller = new Scroller(getContext());
        // 设置其方向为横向
        setOrientation(LinearLayout.HORIZONTAL);
        // 将slide_view_merge加载进来
        View.inflate(getContext(), R.layout.create_circle_slide_view_merge, this);
        viewContent = (LinearLayout) findViewById(R.id.view_content);

        holder = (RelativeLayout) findViewById(R.id.holder);
        holder.measure(MeasureSpec.UNSPECIFIED,
                MeasureSpec.UNSPECIFIED);
        mHolderWidth = holder.getMeasuredWidth();
        holder.setLayoutParams(new LayoutParams(mHolderWidth,
                LayoutParams.MATCH_PARENT));
    }

    public void setButtonText(CharSequence text) {
        ((TextView) findViewById(R.id.delete)).setText(text);
    }

    public void setContentView(View view) {
        viewContent.addView(view);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScrollX() != 0) {
            this.smoothScrollTo(0, 0);
        }
    }

    public boolean onRequireTouchEvent(MotionEvent event) {
        if (isSlide) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int scrollX = getScrollX();

            boolean enableClick = true;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = x;
                    isHorizontalMove = true;//按下默认为true，当移上下则为false
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                    }
                    if (onSlideListener != null) {
                        onSlideListener.onSlide(this,
                                OnSlideListener.SLIDE_STATUS_START_SCROLL);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    int deltaX = x - mLastX;
                    int deltaY = y - mLastY;
                    //上下移动
                    if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                        isHorizontalMove = false;
                        break;
                    }

                    if (!isHorizontalMove)
                        break;
                    int newScrollX = scrollX - deltaX;

                    if (deltaX != 0) {
                        if (newScrollX < 0) {
                            newScrollX = 0;
                        } else if (newScrollX > mHolderWidth) {
                            newScrollX = mHolderWidth;
                        }
                        this.scrollTo(newScrollX, 0);
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    int newScrollX = 0;
                    if (scrollX - mHolderWidth * 0.75 > 0) {
                        newScrollX = mHolderWidth;
                        setPressed(false);
                        enableClick = false;
                    }else if (Math.abs(x - downX) > mHolderWidth) {
                        setPressed(false);
                        enableClick = false;
                    }
                    this.smoothScrollTo(newScrollX, 0);
                    if (onSlideListener != null) {
                        onSlideListener.onSlide(this,
                                newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF
                                        : OnSlideListener.SLIDE_STATUS_ON);
                    }
                    break;
                }
                default:
                    break;
            }

            mLastX = x;
            mLastY = y;

            return enableClick;
        }
        return true;
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        scroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    public boolean getisHorizontalMove(){
        return isHorizontalMove;
    }

    /**
     * 设置SwipeItemView是否可以滑动
     * @param slideOrNot
     * @return
     */
    public void setItemCanSlide(boolean slideOrNot) {
        this.isSlide = slideOrNot;
    }

    /**
     * 设置删除按钮隐藏掉
     */
    public void setHolderGone() {
        if(!this.scroller.isFinished()) {
            this.scroller.abortAnimation();
        }
        this.scrollTo(0,0);
    }
}
