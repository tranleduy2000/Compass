package com.duy.compass.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.Paint.Align;
import static android.graphics.Paint.Style;

public class CompassViewNG extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private static final String TAG = CompassViewNG.class.getSimpleName();
    static int f9096e = 112;
    float f9098a;
    float f9099b;
    float f9100c;
    private int mTextSize = 42;
    private Thread mCurrentThread;
    private boolean mIsPortrait;
    private float zzzz;
    private Point mCenterPoint;
    private Paint mPathPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint mNumberPaint = new Paint(ANTI_ALIAS_FLAG);
    private Path mPath;
    private Path f9108n;
    //    private C1602f f9109o;
    private volatile boolean mIsActive;

    public CompassViewNG(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CompassViewNG(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        getHolder().addCallback(this);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mIsPortrait = ((float) displayMetrics.heightPixels) / ((float) displayMetrics.widthPixels) > 1.4f;
    }

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

    private void m9840a(Canvas canvas, float f, String str) {
        this.mNumberPaint.setTextSize(((float) f9096e) * this.zzzz);
        Rect rect = new Rect();
        this.mNumberPaint.getTextBounds(str, 0, 1, rect);
        float toRadians = this.f9098a + ((float) Math.toRadians((double) f));
        float cos = (float) Math.cos((double) toRadians);
        float f2 = 264.0f * this.zzzz;
        canvas.drawText(str, (cos * f2) + ((float) this.mCenterPoint.x), ((((float) Math.sin((double) toRadians)) * f2) + ((float) this.mCenterPoint.y)) - ((float) ((rect.bottom + rect.top) / 2)), this.mNumberPaint);
    }

    private void drawNumber(Canvas canvas, float f, String str) {
        this.mNumberPaint.setTextSize(((float) mTextSize) * this.zzzz);
        Rect rect = new Rect();
        this.mNumberPaint.getTextBounds(str, 0, str.length(), rect);
        float toRadians = this.f9098a + ((float) Math.toRadians((double) f));
        float cos = (float) Math.cos((double) toRadians);
        float f2 = 450.0f * this.zzzz;
        canvas.drawText(str, (cos * f2) + ((float) this.mCenterPoint.x), ((((float) Math.sin((double) toRadians)) * f2) + ((float) this.mCenterPoint.y)) - ((float) ((rect.bottom + rect.top) / 2)), this.mNumberPaint);
    }

//    public void setDirectionStrings(C1602f c1602f) {
////        this.f9109o = c1602f;
//    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mIsActive = true;
        this.mCurrentThread = new Thread(this);
        this.mCurrentThread.start();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
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

    public void run() {
        while (this.mIsActive) {
            long currentTimeMillis = System.currentTimeMillis();
            try {
                Canvas canvas = getHolder().lockCanvas();
                if (canvas != null) {
                    float f;
                    if (this.f9108n == null) {
                        this.zzzz = ((float) getMeasuredWidth()) / 1200.0f;
                        this.mCenterPoint = new Point(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                        this.mNumberPaint.setTextAlign(Align.CENTER);
                        f = this.zzzz * 100.0f;
                        this.mPath = new Path();
                        this.mPath.moveTo(((float) this.mCenterPoint.x) - f, (float) this.mCenterPoint.y);
                        this.mPath.lineTo(((float) this.mCenterPoint.x) + f, (float) this.mCenterPoint.y);
                        this.mPath.moveTo((float) this.mCenterPoint.x, ((float) this.mCenterPoint.y) - f);
                        this.mPath.lineTo((float) this.mCenterPoint.x, f + ((float) this.mCenterPoint.y));
                        this.f9108n = new Path();

                        this.f9108n.moveTo((float) this.mCenterPoint.x, ((float) this.mCenterPoint.y) - (480.0f * this.zzzz));
                        this.f9108n.lineTo(((float) this.mCenterPoint.x) + (this.zzzz * 15.0f), ((float) this.mCenterPoint.y) - (510.0f * this.zzzz));
                        this.f9108n.lineTo(((float) this.mCenterPoint.x) - (this.zzzz * 15.0f), ((float) this.mCenterPoint.y) - (510.0f * this.zzzz));
                    }
                    canvas.drawColor(-16777216);
                    canvas.save();
                    canvas.rotate((float) Math.toDegrees((double) this.f9098a), (float) this.mCenterPoint.x, (float) this.mCenterPoint.y);

                    CanvasHelper.drawClock(canvas);
                    canvas.restore();
                    this.mPathPaint.setColor(-8355712);
                    this.mPathPaint.setStyle(Style.FILL);

                    f = this.zzzz * 100.0f;
                    canvas.drawCircle(Math.min(Math.max(-f, (-this.f9100c) * f), f) + ((float) this.mCenterPoint.x), Math.min(Math.max(-f, (-this.f9099b) * f), f) + ((float) this.mCenterPoint.y), 24.0f * this.zzzz, this.mPathPaint);
                    this.mPathPaint.setStrokeWidth(2.0f);
                    this.mPathPaint.setStyle(Style.STROKE);
                    this.mPathPaint.setColor(-1);
                    canvas.drawPath(this.mPath, this.mPathPaint);
                    this.mPathPaint.setStrokeWidth(1.0f);
                    this.mPathPaint.setStyle(Style.FILL);
                    this.mPathPaint.setColor(-1);
                    canvas.drawPath(this.f9108n, this.mPathPaint);
                    this.mNumberPaint.setColor(-1);

                    drawNumber(canvas, 300.0f, "30");
                    drawNumber(canvas, 330.0f, "60");
                    drawNumber(canvas, 360.0f, "90");
                    drawNumber(canvas, 30.0f, "120");
                    drawNumber(canvas, 60.0f, "150");
                    drawNumber(canvas, 90.0f, "180");
                    drawNumber(canvas, 120.0f, "210");
                    drawNumber(canvas, 150.0f, "240");
                    drawNumber(canvas, 180.0f, "270");
                    drawNumber(canvas, 210.0f, "300");
                    drawNumber(canvas, 240.0f, "330");
//                    m9840a(lockCanvas, 0.0f, this.f9109o.f9241c);
//                    m9840a(lockCanvas, 90.0f, this.f9109o.f9243e);
//                    m9840a(lockCanvas, 180.0f, this.f9109o.f9245g);
//                    m9840a(lockCanvas, 270.0f, this.f9109o.f9239a);
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
            } catch (Throwable th) {
            }
        }
    }
}