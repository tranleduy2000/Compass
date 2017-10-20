package com.duy.compass.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.duy.compass.DLog;
import com.duy.compass.R;
import com.duy.compass.model.SensorValue;

/**
 * Created by Duy on 10/20/2017.
 */

public class AccelerometerCompassHelper {
    private static final String TAG = "AccelerometerCompassHel";
    private final Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final SensorValue mSensorValue = new SensorValue();
    private final Path mPath = new Path();
    private boolean mIsPaintCreated = false;
    /*Color*/
    @ColorInt
    private int mForegroundColor;
    @ColorInt
    private int mBackgroundColor;
    @ColorInt
    private int mPrimaryTextColor;
    @ColorInt
    private int mSecondaryTextColor;
    @ColorInt
    private int mAccentColor;
    @NonNull
    private Context mContext;
    private float mPixelScale;
    private Point mCenter;
    private float mUnitPadding;

    public AccelerometerCompassHelper(@NonNull Context context) {
        this.mContext = context;
    }

    public void draw(Canvas canvas) {
        mPixelScale = ((float) Math.min(canvas.getWidth(), canvas.getHeight())) / 1000.0f;
        mCenter = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
        mUnitPadding = realPx(5);
        initPaint();

        //drawSunTime(canvas);
        drawPitchRoll(canvas);
    }

    private float realPx(float width) {
        return width * mPixelScale;
    }

    private void initPaint() {
        //no need setup
        if (mIsPaintCreated) {
            return;
        }


        mForegroundColor = ContextCompat.getColor(mContext, R.color.compass_foreground_color);
        mBackgroundColor = ContextCompat.getColor(mContext, R.color.compass_background_color);
        mPrimaryTextColor = ContextCompat.getColor(mContext, R.color.compass_text_primary_color);
        mSecondaryTextColor = ContextCompat.getColor(mContext, R.color.compass_text_secondary_color);
        mAccentColor = ContextCompat.getColor(mContext, R.color.compass_accent_color);

        mPathPaint.setStrokeCap(Paint.Cap.ROUND);

        mIsPaintCreated = true;
    }

    public SensorValue getSensorValue() {
        return mSensorValue;
    }

    private void drawPitchRoll(Canvas canvas) {
        mPathPaint.setColor(mPrimaryTextColor);
        mPathPaint.setStyle(Paint.Style.FILL);
        float roll = mSensorValue.getRoll();
        float pitch = mSensorValue.getPitch();

        float radius = realPx(20);

        float cosP = (float) Math.cos(Math.toRadians(pitch - 90));
        int length = 100;
        float x = (float) (realPx(length) * cosP);
        float cosR = (float) Math.cos(Math.toRadians(roll - 90));
        float y = (float) (realPx(length) * cosR);
        canvas.drawCircle(mCenter.x - x, mCenter.y + y, radius, mPathPaint);

        radius = realPx(length);
        mPath.reset();
        mPath.moveTo(mCenter.x - radius, mCenter.y);
        mPath.lineTo(mCenter.x + radius, mCenter.y);
        mPath.moveTo(mCenter.x, mCenter.y - radius);
        mPath.lineTo(mCenter.x, mCenter.y + radius);

        mPathPaint.setColor(mSecondaryTextColor);
        mPathPaint.setStrokeWidth(realPx(3));
        mPathPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPathPaint);
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        DLog.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
        mIsPaintCreated = false;
    }
}
