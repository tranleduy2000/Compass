package com.duy.compass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.duy.compass.CanvasHelper;
import com.duy.compass.SensorListener;

/**
 * Created by Duy on 10/15/2017.
 */

public class CompassView extends View implements SensorListener.OnValueChangedListener {
    CanvasHelper mCanvasHelper;
    private boolean mIsPortrait;
    private SensorListener mSensorListener;

    public CompassView(Context context) {
        super(context);
        init(context);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mIsPortrait = ((float) displayMetrics.heightPixels) / ((float) displayMetrics.widthPixels) > 1.4f;
        mCanvasHelper = new CanvasHelper();

        mSensorListener = new SensorListener(context);
        mSensorListener.setOnValueChangedListener(this);

    }

    @Override
    protected void onAttachedToWindow() {
        mSensorListener.start();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        mSensorListener.stop();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int rawWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mode2 = MeasureSpec.getMode(heightMeasureSpec);
        int rawHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width = (int) ((this.mIsPortrait ? 1.0f : 0.8f) * ((float) rawWidth));
        int height = (int) (((float) rawWidth) * 0.86f);
        if (mode == MeasureSpec.EXACTLY) {
            width = rawWidth;
        } else if (mode == Integer.MIN_VALUE) {
            width = Math.min(width, rawWidth);
        }
        if (mode2 == MeasureSpec.EXACTLY) {
            height = rawHeight;
        } else if (mode2 == Integer.MIN_VALUE) {
            height = Math.min(height, rawHeight);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvasHelper.draw(canvas);
    }

    @Override
    public void onCompassChangeValue(float oldDegree, float newDegree) {
        mCanvasHelper.setRotate(newDegree);
        invalidate();
    }
}
