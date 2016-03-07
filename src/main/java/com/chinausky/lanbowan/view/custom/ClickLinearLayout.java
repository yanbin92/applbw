package com.chinausky.lanbowan.view.custom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Created by Woody on 2015/12/10.
 */
public class ClickLinearLayout extends LinearLayout {

    private Animator anim1;
    private Animator anim2;
    private int mHeight;
    private int mWidth;
    private Handler mHandler = new Handler();

    private ClickListener listener;

    public ClickLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private void init() {

        PropertyValuesHolder valueHolder_1 = PropertyValuesHolder.ofFloat(
                "scaleX", 1f, 0.95f);
        PropertyValuesHolder valuesHolder_2 = PropertyValuesHolder.ofFloat(
                "scaleY", 1f, 0.95f);
        anim1 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_1,
                valuesHolder_2);
        anim1.setDuration(20);
        anim1.setInterpolator(new LinearInterpolator());

        PropertyValuesHolder valueHolder_3 = PropertyValuesHolder.ofFloat(
                "scaleX", 0.95f, 1f);
        PropertyValuesHolder valuesHolder_4 = PropertyValuesHolder.ofFloat(
                "scaleY", 0.95f, 1f);
        anim2 = ObjectAnimator.ofPropertyValuesHolder(this, valueHolder_3,
                valuesHolder_4);
        anim2.setDuration(20);
        anim2.setInterpolator(new LinearInterpolator());
    }

    public void setClickListener(ClickListener clickListener) {
        this.listener = clickListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        anim2.end();
                        anim1.start();
                    }
                });
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isInnerLinearLayout(event.getX(), event.getY())) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            anim1.end();
                            anim2.start();
                        }
                    });
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        anim1.end();
                        anim2.start();
                    }
                });
                if (isInnerLinearLayout(event.getX(), event.getY())) {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    //按下的点是否在View内
    protected boolean isInnerLinearLayout(float x, float y) {
        if (x >= 0 && x <= mWidth) {
            if (y >= 0 && y <= mHeight) {
                return true;
            }
        }
        return false;
    }

    /**
     * 点击事件回调处理
     */
    public interface ClickListener {

        void onClick();
    }

    @Override
    protected void onDetachedFromWindow() {
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
    }
}
