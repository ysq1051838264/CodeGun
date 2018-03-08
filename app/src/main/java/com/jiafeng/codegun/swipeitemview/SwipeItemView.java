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
 * 带左滑删除的ItemView
 * Created by bill on 2015/6/4.
 */
public class SwipeItemView extends LinearLayout {

    private LinearLayout viewContent;
    private RelativeLayout holder;
    private Scroller scroller;
    private OnSlideListener onSlideListener;

    private int mHolderWidth;

    private int downX;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;

    private int mLastX = 0;
    private int mLastY = 0;
    private boolean isHorizontalMove = true;//判断是否横滑
    private boolean firstMove = false;

    private boolean canSlide; //判断是否可以右滑出删除按钮

    public interface OnSlideListener {
        int SLIDE_STATUS_OFF = 0;
        int SLIDE_STATUS_START_SCROLL = 1;
        int SLIDE_STATUS_ON = 2;

        /**
         * @param swipeItemView current SlideView
         * @param status        SLIDE_STATUS_ON or SLIDE_STATUS_OFF
         */
        void onSlide(SwipeItemView swipeItemView, int status);

    }

    public SwipeItemView(Context context) {
        super(context);
        initView();
    }

    public SwipeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return onRequireTouchEvent(event) && super.onTouchEvent(event);
    }

    private void initView() {

        // 初始化弹性滑动对象
        scroller = new Scroller(getContext());
        // 设置其方向为横向
        setOrientation(LinearLayout.HORIZONTAL);
        // 将slide_view_merge加载进来
        View.inflate(getContext(), R.layout.swip_item_view_merge, this);
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
        if (canSlide) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            int scrollX = getScrollX();
            boolean enableClick = true;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = x;
                    isHorizontalMove = false;//按下后先设置称false
                    firstMove = true;
                    if (!scroller.isFinished()) {
                        scroller.abortAnimation();
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    int deltaX = x - mLastX;
                    int deltaY = y - mLastY;
                    //上下移动
                    if (firstMove) {
                        //第一次移动，先定性是否左滑
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            isHorizontalMove = true;
                            if (onSlideListener != null) {
                                onSlideListener.onSlide(this,
                                        OnSlideListener.SLIDE_STATUS_START_SCROLL);
                            }
                        }
                    }
                    //没有滑动则直接不做任何处理
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
                    if (isHorizontalMove) {
                        //有侧滑，则判断是否显示删除按钮
                        if (scrollX - mHolderWidth * 0.75 > 0) {
                            newScrollX = mHolderWidth;
                            enableClick = false;
                        } else if (Math.abs(x - downX) > mHolderWidth) {
                            enableClick = false;
                        }
                    } else {
                        //没有侧滑就直接交给父类处理
                        enableClick = true;
                    }
                    this.smoothScrollTo(newScrollX, 0);
                    if (isHorizontalMove && onSlideListener != null) {
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

    public boolean onTouchWhenExtendState(MotionEvent event) {
        float fixedX = event.getX() + getScrollX();
        return fixedX >= holder.getLeft() && fixedX <= holder.getRight();
    }


    /**
     * 设置SwipeItemView是否可以滑动
     *
     * @param slideOrNot
     * @return
     */
    public void setItemCanSlide(boolean slideOrNot) {
        this.canSlide = slideOrNot;
    }

    /**
     * 设置删除按钮隐藏掉
     */
    public void hidHolder() {
        if (!this.scroller.isFinished()) {
            this.scroller.abortAnimation();
        }
        this.scrollTo(0, 0);
    }


}
