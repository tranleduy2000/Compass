package com.duy.compass.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.duy.compass.util.DLog;
import com.duy.compass.R;
import com.duy.compass.model.SensorValue;
import com.duy.compass.model.Sunshine;
import com.duy.compass.view.TypefaceManager;

import java.util.Locale;

import static com.duy.compass.util.Utility.getDirectionText;

public class CompassDrawer {
    private static final String TAG = "CanvasHelper";
    private final Paint mNumberTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDirectionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mMagneticPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mSecondaryTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPrimaryTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private final SensorValue mSensorValue = new SensorValue();
    private final float mMaxRadius = 430;
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
    private Typeface mTypeface;
    @Nullable
    private Sunshine mSunshine = new Sunshine(30, 123);
    private float mPixelScale;
    private Point mCenter;
    private float mUnitPadding;
    @Nullable
    private Path mClockPathSecondary = null;
    @Nullable
    private Path mClockPathPrimary = null;
    private boolean mIsPaintCreated = false;

    public CompassDrawer(@NonNull Context context) {
        this.mContext = context;
        this.mTypeface = TypefaceManager.get(context, "Roboto-Light.ttf");
    }

    public SensorValue getSensorValue() {
        return mSensorValue;
    }

    public void draw(Canvas canvas) {
        mPixelScale = ((float) Math.min(canvas.getWidth(), canvas.getHeight())) / 1000.0f;
        mCenter = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
        mUnitPadding = realPx(5);
        initPaint();

        drawBackground(canvas);
        drawMagnetic(canvas);
        drawClock(canvas);
        drawAzimuthValue(canvas);
        //drawSunTime(canvas);
    }

    private void initPaint() {
        mNumberTextPaint.setTextSize(realPx(30));
        mNumberTextPaint.setColor(mPrimaryTextColor);
        mNumberTextPaint.setTypeface(mTypeface);

        //no need setup
        if (mIsPaintCreated) {
            return;
        }

        mDirectionTextPaint.setTextSize(realPx(60));
        mDirectionTextPaint.setTypeface(mTypeface);

        LinearGradient gradient = new LinearGradient(0, 0, 0, realPx(500),
                new int[]{Color.GREEN, Color.GREEN, Color.RED, Color.RED}, null, Shader.TileMode.MIRROR);
        mMagneticPaint.setShader(gradient);
        mMagneticPaint.setStrokeWidth(realPx(25));
        mMagneticPaint.setStyle(Style.STROKE);
        mMagneticPaint.setStrokeCap(Paint.Cap.ROUND);

        mForegroundColor = ContextCompat.getColor(mContext, R.color.compass_foreground_color);
        mBackgroundColor = ContextCompat.getColor(mContext, R.color.compass_background_color);
        mPrimaryTextColor = ContextCompat.getColor(mContext, R.color.compass_text_primary_color);
        mSecondaryTextColor = ContextCompat.getColor(mContext, R.color.compass_text_secondary_color);
        mAccentColor = ContextCompat.getColor(mContext, R.color.compass_accent_color);

        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Style.FILL);

        mPathPaint.setStrokeCap(Paint.Cap.ROUND);

        mSecondaryTextPaint.setColor(mSecondaryTextColor);
        mSecondaryTextPaint.setTypeface(mTypeface);

        mPrimaryTextPaint.setColor(mPrimaryTextColor);
        mPrimaryTextPaint.setTypeface(mTypeface);
        mIsPaintCreated = true;
    }

    private void drawBackground(Canvas canvas) {
        float radius = realPx(mMaxRadius);

        canvas.drawCircle(mCenter.x, mCenter.y, radius, mBackgroundPaint);

        mPathPaint.setColor(mSecondaryTextColor);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeWidth(realPx(3));
        canvas.drawCircle(mCenter.x, mCenter.y, radius, mPathPaint);

        mPathPaint.setColor(mForegroundColor);

        Paint.FontMetrics fm = mNumberTextPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;

        float strokeWidth = realPx(20) + fontHeight;
        mPathPaint.setStrokeWidth(strokeWidth);
        radius = realPx(350) - strokeWidth / 2.0f - realPx(mUnitPadding);
        canvas.drawCircle(mCenter.x, mCenter.y, radius, mPathPaint);
    }


    private void drawMagnetic(Canvas canvas) {
        float step = realPx(450);

        mPathPaint.setStrokeWidth(realPx(25));
        mPathPaint.setColor(mBackgroundColor);

        RectF bound = new RectF(mCenter.x - step, mCenter.y - step, mCenter.x + step, mCenter.y + step);
        int sweepAngle = 100;

        mPath.reset();
        mPath.addArc(bound, 310, 100);
        canvas.drawPath(mPath, mPathPaint);

        float magneticField = mSensorValue.getMagneticField();
        int max = 160;
        float percent = Math.min(1, magneticField / max);
        percent = percent * sweepAngle;

        mPath.reset();
        mPath.addArc(bound, 310 + sweepAngle - percent, percent);
        canvas.drawPath(mPath, mMagneticPaint);

        mPrimaryTextPaint.setTextSize(realPx(30));
        drawText(canvas, 303, String.format(Locale.US, "%dμT", (int) mSensorValue.getMagneticField()), 445, mPrimaryTextPaint);

        mSecondaryTextPaint.setTextSize(realPx(30));
        drawText(canvas, 60, "mag.field", 445, mSecondaryTextPaint);
    }

    private void drawSunTime(Canvas canvas) {
        if (mSunshine == null) return;
        float sunRise = mSunshine.getSunrise();
        float sunShine = mSunshine.getSunset();
        mPathPaint.setColor(Color.YELLOW);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeWidth(realPx(10));
        float step = realPx(405);

        mPath.reset();
        RectF bound = new RectF(mCenter.x - step, mCenter.y - step, mCenter.x + step, mCenter.y + step);
        mPath.addArc(bound, sunRise, Math.abs(sunShine - sunRise));
        canvas.drawPath(mPath, mPathPaint);
    }

    private void drawAzimuthValue(Canvas canvas) {
        //draw triangle
        float x = mCenter.x;
        float length = realPx(30);
        float y = (mCenter.y - realPx(mMaxRadius) + length / 2.0f);

        mPath.reset();
        mPath.lineTo(x - length / 2.0f, y - length);
        mPath.lineTo(x + length / 2.0f, y - length);
        mPath.lineTo(x, y);
        mPath.lineTo(x - length / 2.0f, y - length);

        mPathPaint.setStyle(Style.FILL);
        mPathPaint.setColor(mAccentColor);
        mPathPaint.setShadowLayer(realPx(4), 0, 0, Color.RED);

        canvas.drawPath(mPath, mPathPaint);
        mPathPaint.reset();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);

//        length = realPx(16);
//        y = (mCenter.y - realPx(mMaxRadius) + length / 2.0f);
//        mPath.reset();
//        mPath.lineTo(x - length / 2.0f, y - length);
//        mPath.lineTo(x + length / 2.0f, y - length);
//        mPath.lineTo(x, y);
//        mPath.lineTo(x - length / 2.0f, y - length);
//
//        mPathPaint.setStyle(Style.FILL);
//        mPathPaint.setColor(mAccentColor);
//        canvas.drawPath(mPath, mPathPaint);


        mPrimaryTextPaint.setTextSize(realPx(80));
        String str = ((int) mSensorValue.getAzimuth()) + "° " + getDirectionText(mSensorValue.getAzimuth());
        Rect rectF = new Rect();
        mPrimaryTextPaint.getTextBounds(str, 0, str.length(), rectF);
        y = mCenter.y + rectF.height() / 2.0f;
        x = mCenter.x - mPrimaryTextPaint.measureText(str) / 2.0f;
        canvas.drawText(str, x, y, mPrimaryTextPaint);
    }

    private void drawClock(Canvas canvas) {
        canvas.save();
        canvas.rotate(-mSensorValue.getAzimuth(), mCenter.x, mCenter.y);
        drawClock(canvas, mCenter);
        drawClockBig(canvas, mCenter);
        drawNumber(canvas);
        drawDirectionText(canvas);
        canvas.restore();
    }

    private void drawClock(Canvas canvas, Point center) {
        mPathPaint.setColor(mForegroundColor);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeWidth(realPx(3));

        if (mClockPathSecondary == null) {
            mClockPathSecondary = new Path();
            float degreeStep = 2.5f;
            for (float step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
                float cos = (float) Math.cos(step);
                float sin = (float) Math.sin(step);

                float x = realPx(350) * cos;
                float y = realPx(350) * sin;
                mClockPathSecondary.moveTo(x + ((float) center.x), y + ((float) center.y));

                x = realPx(380) * cos;
                y = realPx(380) * sin;
                mClockPathSecondary.lineTo(x + ((float) center.x), y + ((float) center.y));
            }
        }
        canvas.drawPath(mClockPathSecondary, mPathPaint);
    }

    private float realPx(float width) {
        return width * mPixelScale;
    }

    private void drawClockBig(Canvas canvas, Point center) {
        mPathPaint.setStrokeWidth(realPx(7));

        if (mClockPathPrimary == null) {
            mClockPathPrimary = new Path();
            float degreeStep = 30.0f;
            for (float step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
                float cos = (float) Math.cos(step);
                float sin = (float) Math.sin(step);

                float x = realPx(330) * cos;
                float y = realPx(330) * sin;
                mClockPathPrimary.moveTo(x + ((float) center.x), y + ((float) center.y));

                cos *= realPx(380);
                sin *= realPx(380);
                mClockPathPrimary.lineTo(cos + ((float) center.x), sin + ((float) center.y));
            }
        }
        mPathPaint.setColor(Color.WHITE);
        canvas.drawPath(mClockPathPrimary, mPathPaint);

        mPath.reset();
        float radian = (float) Math.toRadians(270.0d);

        float cos = (float) Math.cos((double) radian);
        float sin = (float) Math.sin((double) radian);

        float x = realPx(320) * cos;
        float y = realPx(320) * sin;
        mPath.moveTo(((float) mCenter.x) + x, ((float) mCenter.y) + y);

        x = realPx(400) * cos;
        y = realPx(400) * sin;
        mPath.lineTo(x + ((float) mCenter.x), y + ((float) mCenter.y));
        mPathPaint.setColor(mAccentColor);
        mPathPaint.setStrokeWidth(realPx(9));

        canvas.drawPath(mPath, mPathPaint);
    }

    private void drawNumber(Canvas canvas) {
        float radius = 330;
        drawNumber(canvas, 300.0f, "30", radius);
        drawNumber(canvas, 330.0f, "60", radius);
        drawNumber(canvas, 360.0f, "90", radius);
        drawNumber(canvas, 30.0f, "120", radius);
        drawNumber(canvas, 60.0f, "150", radius);
        drawNumber(canvas, 90.0f, "180", radius);
        drawNumber(canvas, 120.0f, "210", radius);
        drawNumber(canvas, 150.0f, "240", radius);
        drawNumber(canvas, 180.0f, "270", radius);
        drawNumber(canvas, 210.0f, "300", radius);
        drawNumber(canvas, 240.0f, "330", radius);
    }

    private void drawNumber(Canvas canvas, float degree, String text, float radius) {
        Paint.FontMetrics fm = mNumberTextPaint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;

        float cos = (float) Math.cos(Math.toRadians(degree));
        float sin = (float) Math.sin(Math.toRadians(degree));

        float x = (cos * realPx(radius)) + mCenter.x;
        float y = (sin * realPx(radius)) + mCenter.y;

        canvas.save();

        canvas.translate(x, y);
        canvas.rotate(90.0f + degree);
        canvas.drawText(text, -mNumberTextPaint.measureText(text) / 2.0f, height, mNumberTextPaint);

        canvas.restore();
    }

    private void drawText(Canvas canvas, float degree, String text, float radius, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;

        float cos = (float) Math.cos(Math.toRadians(degree));
        float sin = (float) Math.sin(Math.toRadians(degree));

        float x = (cos * realPx(radius)) + mCenter.x;
        float y = (sin * realPx(radius)) + mCenter.y;

//        canvas.drawPoint(x, y, mNumberPaint);
//        canvas.drawPoint(mCenter.x, mCenter.y, mNumberPaint);

        canvas.save();

        canvas.translate(x, y);
        if (degree > 0 && degree < 180) {
            canvas.rotate(270 + degree);
            canvas.drawText(text, -paint.measureText(text) / 2.0f, height / 2, paint);
        } else {
            canvas.rotate(90 + degree);
            canvas.drawText(text, -paint.measureText(text) / 2.0f, 0, paint);

        }

        canvas.restore();
    }

    private void drawDirectionText(Canvas canvas) {
        //draw direction N S E W
        //N = 0, E = 90, S = 180, W = 270
        Paint.FontMetrics fm = mNumberTextPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;
        float radiusPx = realPx(330) - fontHeight - realPx(mUnitPadding);

        mDirectionTextPaint.setColor(mAccentColor);
        mDirectionTextPaint.setTextSize(realPx(60));

        drawDirectionText(canvas, 270, "N", radiusPx, mDirectionTextPaint);
        mDirectionTextPaint.setColor(mPrimaryTextColor);
        drawDirectionText(canvas, 0, "E", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 90, "S", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 180, "W", radiusPx, mDirectionTextPaint);

        mDirectionTextPaint.setTextSize(realPx(40));
        mDirectionTextPaint.setColor(mSecondaryTextColor);

        drawDirectionText(canvas, 315, "NE", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 45, "SE", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 135, "SW", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 225, "NW", radiusPx, mDirectionTextPaint);
    }

    private void drawDirectionText(Canvas canvas, float degree, String text, float radiusPx, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;

        float cos = (float) Math.cos(Math.toRadians(degree));
        float sin = (float) Math.sin(Math.toRadians(degree));

        float x = (cos * (radiusPx)) + mCenter.x;
        float y = (sin * (radiusPx)) + mCenter.y;

        canvas.save();
        canvas.translate(x, y);

        canvas.rotate(90 + degree);
        canvas.drawText(text, -paint.measureText(text) / 2.0f, height, paint);
        canvas.restore();
    }

    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        DLog.d(TAG, "onSizeChanged() called with: w = [" + w + "], h = [" + h + "], oldw = [" + oldw + "], oldh = [" + oldh + "]");
        mIsPaintCreated = false;
    }
}
