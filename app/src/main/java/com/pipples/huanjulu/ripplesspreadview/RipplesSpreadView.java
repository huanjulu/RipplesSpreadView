package com.pipples.huanjulu.ripplesspreadview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by huanjulu on 19/1/17.
 */

public class RipplesSpreadView extends View {
    private float mInitialRadius;
    private float mMaxRadiusRate = 0.85f;
    private float mMaxRadius;
    private long mDuration = 1000;
    private int mSpeed = 500;
    private Interpolator mInterpolator = new LinearInterpolator();

    private List<Circle> mCircleList = new ArrayList<Circle>();
    private boolean mIsRunning;

    private boolean mMaxRadiusSet;

    private Paint mPaint;
    private long mLastCreateTime;

    private Runnable mCreateCircle = new Runnable() {
        @Override
        public void run() {
            if (mIsRunning) {
                handler.sendEmptyMessage(0);
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);

    }

    public RipplesSpreadView(Context context) {
        this(context, null);
    }

    public RipplesSpreadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setStyle(Paint.Style.FILL);
    }

    public void setStyle(Paint.Style style) {
        mPaint.setStyle(style);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (!mMaxRadiusSet) {
            mMaxRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
        }
    }

    public void setMaxRadiusRate(float maxRadiusRate) {
        this.mMaxRadiusRate = maxRadiusRate;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.d("TAG", "newCircle");
                    newCircle();
                    break;
            }
        }
    };


    /**
     * 开始
     */
    public void start() {
        newCircle();
    }

    /**
     * 停止
     */
    public void stop() {
        mIsRunning = false;
    }

    protected void onDraw(Canvas canvas) {
        Iterator<Circle> iterator = mCircleList.iterator();
        while (iterator.hasNext()) {
            Circle circle = iterator.next();

            mPaint.setAlpha(circle.getAlpha());
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, circle.getCurrentRadius(), mPaint);


            if (System.currentTimeMillis() - circle.mCreateTime < mDuration) {
                mPaint.setAlpha(circle.getAlpha());
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, circle.getCurrentRadius(), mPaint);
            } else {
                iterator.remove();
            }
        }
        if (mCircleList.size() > 0) {
            postInvalidateDelayed(10);
        }
    }

    public void setInitialRadius(float radius) {
        mInitialRadius = radius;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public void setMaxRadius(float maxRadius) {
        this.mMaxRadius = maxRadius;
        mMaxRadiusSet = true;
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }


    public void onSetStyle() {

    }

    private void newCircle() {
        long currentTime = System.currentTimeMillis();
        Circle circle = new Circle();
        mCircleList.add(circle);
        invalidate();
        mLastCreateTime = currentTime;
    }

    private class Circle {
        private long mCreateTime;

        public Circle() {
            this.mCreateTime = System.currentTimeMillis();
        }

        public int getAlpha() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.3f / mDuration;
            int aplpa = (int) ((1.0f - mInterpolator.getInterpolation(percent)) * 100);
            return (int) ((1.0f - mInterpolator.getInterpolation(percent)) * 90);
        }

        public float getCurrentRadius() {
            float percent = (System.currentTimeMillis() - mCreateTime) * 1.0f / mDuration;
            return mInitialRadius + mInterpolator.getInterpolation(percent) * (mMaxRadius - mInitialRadius);
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        if (mInterpolator == null) {
            mInterpolator = new LinearInterpolator();
        }
    }
}