package com.duy.compass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Duy on 10/15/2017.
 */

public class CompassView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private Thread mCurrentThread;
    private boolean mIsPortrait;
    private volatile boolean mIsActive;

    private Point mCenter = new Point();
    private float mClockRadius = 0f;
    private int mLineWidth = 7;

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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mCenter.set(w / 2, h / 2);
        this.mClockRadius = w / 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.mIsActive = true;
        this.mCurrentThread = new Thread(this);
        this.mCurrentThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
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
    public void run() {
        while (mIsActive) {
            long currentTimeMillis = System.currentTimeMillis();
            try {
                Canvas canvas = getHolder().lockCanvas();
                if (canvas != null) {
                    CanvasHelper.drawClock(canvas);
//                drawNumber(canvas);
//                drawDirection(canvas);
                }
                if (canvas != null) {
                    try {
                        getHolder().unlockCanvasAndPost(canvas);
                    } catch (Throwable e) {
//                        C0888a.m3420a(e);
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
            } catch (Exception e) {

            }
        }
    }

    private void drawLine(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(mLineWidth);
        float lineLength = ((float) canvas.getWidth()) / 1200.0f;
        CanvasHelper.drawClock(canvas, mCenter, mClockRadius, lineLength, paint);
    }
}
