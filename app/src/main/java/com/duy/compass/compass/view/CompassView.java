package com.duy.compass.compass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.duy.compass.compass.CanvasHelper;
import com.duy.compass.sensor.SensorListener;

/**
 * Created by Duy on 10/15/2017.
 */

public class CompassView extends SurfaceView implements SensorListener.OnValueChangedListener,
        SurfaceHolder.Callback, Runnable {
    CanvasHelper mCanvasHelper;
    private SensorListener mSensorListener;
    private Thread mCurrentThread;
    private boolean mIsPortrait;
    private volatile boolean mIsActive;

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
        getHolder().addCallback(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mIsPortrait = ((float) displayMetrics.heightPixels) / ((float) displayMetrics.widthPixels) > 1.4f;
        mCanvasHelper = new CanvasHelper();

        mSensorListener = new SensorListener(context);
        mSensorListener.setOnValueChangedListener(this);

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
    public void onCompassRotate(float oldDegree, float newDegree) {
        mCanvasHelper.getSensorValue().setCompassRotate(newDegree);
    }

    @Override
    public void onMagneticFieldChanged(float value) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mIsActive = true;
        this.mCurrentThread = new Thread(this);
        this.mCurrentThread.start();
        this.mSensorListener.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSensorListener.stop();
        this.mIsActive = false;
        if (this.mCurrentThread != null) {
            try {
                this.mCurrentThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mCurrentThread = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIsActive = false;
    }

    public void run() {
        while (this.mIsActive) {
            long currentTimeMillis = System.currentTimeMillis();
            try {
                Canvas canvas = getHolder().lockCanvas();
                if (canvas != null) {
                    mCanvasHelper.draw(canvas);
                }
                if (canvas != null) {
                    try {
                        getHolder().unlockCanvasAndPost(canvas);
                    } catch (Throwable ignored) {
                    }
                }
                long floor = ((long) Math.floor(16.0d)) - (System.currentTimeMillis() - currentTimeMillis);
                if (floor > 0) {
                    try {
                        Thread.sleep(floor);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            } catch (Throwable ignored) {
            }
        }
    }
}
