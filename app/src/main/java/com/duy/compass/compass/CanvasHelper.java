package com.duy.compass.compass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.Nullable;

import com.duy.compass.model.SensorValue;
import com.duy.compass.model.Sunshine;

import java.util.Locale;

import static com.duy.compass.compass.Utility.getDirectionText;

public class CanvasHelper {
    private final Paint mNumberTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDirectionTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mMagneticPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private final SensorValue mSensorValue = new SensorValue();

    private Typeface mTypeface;
    @Nullable
    private Sunshine mSunshine = new Sunshine(30, 123);
    private float mPixelScale;
    private Point mCenter;
    private float mUnitPadding;
    private float mDirectionTextSize = 60f;
    @Nullable
    private Path mClockPathSecondary = null;
    @Nullable
    private Path mClockPathPrimary = null;

    public CanvasHelper(Context context) {
        mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
    }

    public SensorValue getSensorValue() {
        return mSensorValue;
    }

    public void draw(Canvas canvas) {
        mPixelScale = ((float) Math.min(canvas.getWidth(), canvas.getHeight())) / 1000.0f;
        mCenter = new Point(canvas.getWidth() / 2, canvas.getHeight() / 2);
        mUnitPadding = realPx(5);
        mDirectionTextSize = realPx(40f);

        initPaint();

        //draw background
//        canvas.drawRGB(0, 0, 0);
        drawCircle(canvas);
        drawMagnetic(canvas);
        drawClock(canvas);
        drawValue(canvas);
        drawSunTime(canvas);
        drawPitchRoll(canvas);
    }

    private void drawPitchRoll(Canvas canvas) {
        mPathPaint.setColor(Color.GRAY);
        mPathPaint.setStyle(Style.FILL);
        float roll = mSensorValue.getRoll();
        float pitch = mSensorValue.getPitch();

        float radius = realPx(20);

        float cosP = (float) Math.cos(Math.toRadians(pitch - 90));
        int length = 150;
        float x = (float) (realPx(length) * cosP);
        float cosR = (float) Math.cos(Math.toRadians(roll - 90));
        float y = (float) (realPx(length) * cosR);
        canvas.drawCircle(mCenter.x - x, mCenter.y + y, radius, mPathPaint);

        radius = realPx(length / 2);
        mPath.moveTo(mCenter.x - radius, mCenter.y);
        mPath.lineTo(mCenter.x + radius, mCenter.y);
        mPath.moveTo(mCenter.x, mCenter.y - radius);
        mPath.lineTo(mCenter.x, mCenter.y + radius);

        mPathPaint.setColor(Color.WHITE);
        mPathPaint.setStrokeWidth(realPx(3));
        mPathPaint.setStyle(Style.STROKE);
        canvas.drawPath(mPath, mPathPaint);
    }

    private void drawMagnetic(Canvas canvas) {
        float step = realPx(450);

        mPath.reset();
        mPathPaint.setStrokeWidth(realPx(25));
        mPathPaint.setColor(Color.GRAY);

        RectF bound = new RectF(mCenter.x - step, mCenter.y - step, mCenter.x + step, mCenter.y + step);
        int sweepAngle = 100;
        mPath.addArc(bound, 310, 100);
        canvas.drawPath(mPath, mPathPaint);

        float magneticField = mSensorValue.getMagneticField();
        int max = 160;
        float percent = magneticField / max;
        percent = percent * sweepAngle;

        mPath.reset();
        mPath.addArc(bound, 310 + sweepAngle - percent, percent);
        canvas.drawPath(mPath, mMagneticPaint);

        drawText(canvas, 305, String.format(Locale.US, "%dμT", (int) mSensorValue.getMagneticField()), 445, 30);
        drawText(canvas, 60, "mag.field", 445, 30);
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

    private void drawValue(Canvas canvas) {
        //draw triangle
        mPathPaint.setStyle(Style.FILL);
        mPathPaint.setColor(Color.WHITE);
        mPathPaint.setStrokeWidth(realPx(3));

        int x = mCenter.x;
        int y = (int) (mCenter.y - realPx(430 + mUnitPadding * 2));
        mPath.reset();
        int length = (int) realPx(30);
        mPath.lineTo(x - length / 2, y - length);
        mPath.lineTo(x + length / 2, y - length);
        mPath.lineTo(x, y);
        mPath.lineTo(x - length / 2, y - length);
        canvas.drawPath(mPath, mPathPaint);

        mPathPaint.setTextSize(realPx(80));
        mPathPaint.setColor(Color.WHITE);
        String str = ((int) mSensorValue.getAzimuth()) + "° " + getDirectionText(mSensorValue.getAzimuth());
        canvas.drawText(str, x - mPathPaint.measureText(str) / 2, y - length - realPx(10), mPathPaint);
    }


    private void initPaint() {
        mNumberTextPaint.setTextSize(realPx(30));
        mNumberTextPaint.setColor(Color.WHITE);
        mNumberTextPaint.setTypeface(mTypeface);

        mDirectionTextPaint.setTextSize(realPx(60));
        mDirectionTextPaint.setTypeface(mTypeface);

        LinearGradient gradient = new LinearGradient(0, 0, 0, realPx(500), Color.GREEN, Color.RED, Shader.TileMode.MIRROR);
        mMagneticPaint.setShader(gradient);
        mMagneticPaint.setStrokeWidth(realPx(25));
        mMagneticPaint.setStyle(Style.STROKE);

    }

    private void drawCircle(Canvas canvas) {
        float radius = realPx(430);
        mPathPaint.setColor(Color.GRAY);
        mPathPaint.setStyle(Style.STROKE);
        mPathPaint.setStrokeWidth(realPx(3));

        canvas.drawCircle(mCenter.x, mCenter.y, radius, mPathPaint);

        mPathPaint.setColor(Color.DKGRAY);

        Paint.FontMetrics fm = mNumberTextPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;

        float strokeWidth = 20 + fontHeight;
        mPathPaint.setStrokeWidth(realPx(strokeWidth));

        radius = realPx(350 - strokeWidth / 2.0f - mUnitPadding);
        canvas.drawCircle(mCenter.x, mCenter.y, radius, mPathPaint);
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
        mPathPaint.setColor(Color.GRAY);
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
        mPathPaint.setColor(Color.RED);
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

//        canvas.drawPoint(x, y, mNumberPaint);
//        canvas.drawPoint(mCenter.x, mCenter.y, mNumberPaint);

        canvas.save();

        canvas.translate(x, y);
        canvas.rotate(90.0f + degree);
        canvas.drawText(text, -mNumberTextPaint.measureText(text) / 2.0f, height, mNumberTextPaint);

        canvas.restore();
    }

    private void drawText(Canvas canvas, float degree, String text, float radius, float size) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(realPx(size));
        paint.setColor(Color.GRAY);
        paint.setTypeface(mTypeface);

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

        mDirectionTextPaint.setColor(Color.RED);
        mDirectionTextPaint.setTextSize(realPx(60));

        drawDirectionText(canvas, 270, "N", radiusPx, mDirectionTextPaint);
        mDirectionTextPaint.setColor(Color.WHITE);
        drawDirectionText(canvas, 0, "E", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 90, "S", radiusPx, mDirectionTextPaint);
        drawDirectionText(canvas, 180, "W", radiusPx, mDirectionTextPaint);

        mDirectionTextPaint.setTextSize(realPx(40));

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

}
