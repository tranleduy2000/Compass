package com.duy.compass;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.text.TextPaint;

public class CanvasHelper {
    private final TextPaint mNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private float mPixelScale;
    private int mWidth, mHeight;
    private Point mCenter;
    private float mUnitPadding;
    private float mClockNumberSize = 30;
    private float mDirectionTextSize = 50f;
    private int mRotate;

    public CanvasHelper() {
    }

    public void draw(Canvas canvas) {
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        mPixelScale = ((float) mWidth) / 1000.0f;
        mCenter = new Point(mWidth / 2, mHeight / 2);
        mUnitPadding = realPx(5);
        mDirectionTextSize = realPx(40f);

        initPaint();

        //draw background
        canvas.drawRGB(0, 0, 0);

        drawCircle(canvas);
        drawClock(canvas);
        drawValue(canvas);
    }

    private void drawValue(Canvas canvas) {
        //draw triangle
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.WHITE);
        int x = mCenter.x;
        int y = (int) (mCenter.y - realPx(430 + mUnitPadding * 2));
        mPath.reset();
        int length = (int) realPx(30);
        mPath.lineTo(x - length / 2, y - length);
        mPath.lineTo(x + length / 2, y - length);
        mPath.lineTo(x, y);
        mPath.lineTo(x - length / 2, y - length);
        canvas.drawPath(mPath, mPaint);

        mPaint.setTextSize(realPx(70));
        mPaint.setColor(Color.WHITE);
        String str = mRotate + "° " + getDirectionText(mRotate);
        canvas.drawText(str, x - mPaint.measureText(str) / 2, y - length - realPx(10), mPaint);
    }

    private String getDirectionText(int degree) {
        final float step = 22.5f;
        if (degree >= 0 && degree < step || degree > 360 - step) {
            return "N";
        }
        if (degree >= step && degree < step * 3) {
            return "NE";
        }
        if (degree >= step * 3 && degree < step * 5) {
            return "E";
        }
        if (degree >= step * 5 && degree < step * 7) {
            return "SE";
        }
        if (degree >= step * 7 && degree < step * 9) {
            return "S";
        }
        if (degree >= step * 9 && degree < step * 11) {
            return "SW";
        }
        if (degree >= step * 11 && degree < step * 13) {
            return "W";
        }
        if (degree >= step * 13 && degree < step * 15) {
            return "NW";
        }
        return "";
    }

    private void initPaint() {
        mNumberPaint.setTextSize(realPx(mClockNumberSize));
        mNumberPaint.setColor(Color.WHITE);
    }

    private void drawCircle(Canvas canvas) {
        float radius = realPx(430);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(realPx(3));

        canvas.drawCircle(mCenter.x, mCenter.y, radius, mPaint);

        mPaint.setColor(Color.DKGRAY);

        mNumberPaint.setTextSize(realPx(mClockNumberSize));
        Paint.FontMetrics fm = mNumberPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;

        float strokeWidth = 20 + fontHeight;
        mPaint.setStrokeWidth(realPx(strokeWidth));

        radius = realPx(350 - strokeWidth / 2.0f - mUnitPadding);
        canvas.drawCircle(mCenter.x, mCenter.y, radius, mPaint);
    }

    private void drawClock(Canvas canvas) {
        canvas.save();
        canvas.rotate(-mRotate, mCenter.x, mCenter.y);
        drawClock(canvas, mCenter);
        drawClock1(canvas, mCenter);
        drawNumber(canvas);
        drawDirectionText(canvas);
        canvas.restore();
    }


    private void drawClock(Canvas canvas, Point center) {
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(realPx(3));
        mPath.reset();
        float degreeStep = 2.5f;
        for (float step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
            float cos = (float) Math.cos(step);
            float sin = (float) Math.sin(step);

            float x = realPx(350) * cos;
            float y = realPx(350) * sin;
            mPath.moveTo(x + ((float) center.x), y + ((float) center.y));

            x = realPx(380) * cos;
            y = realPx(380) * sin;
            mPath.lineTo(x + ((float) center.x), y + ((float) center.y));
        }
        canvas.drawPath(mPath, mPaint);
    }

    private float realPx(float width) {
        return width * mPixelScale;
    }

    private void drawClock1(Canvas canvas, Point center) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(realPx(5));
        mPath.reset();
        float degreeStep = 30.0f;
        for (float step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
            float cos = (float) Math.cos(step);
            float sin = (float) Math.sin(step);

            float x = realPx(330) * cos;
            float y = realPx(330) * sin;
            mPath.moveTo(x + ((float) center.x), y + ((float) center.y));

            cos *= realPx(380);
            sin *= realPx(380);
            mPath.lineTo(cos + ((float) center.x), sin + ((float) center.y));
        }
        canvas.drawPath(mPath, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(realPx(5));

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
        canvas.drawPath(mPath, mPaint);
    }

    private void drawNumber(Canvas canvas) {
        float radius = 330;
        mNumberPaint.setTextSize(mClockNumberSize);
        mNumberPaint.setColor(Color.WHITE);
        mNumberPaint.setTypeface(Typeface.DEFAULT);

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
        Paint.FontMetrics fm = mNumberPaint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;

        float cos = (float) Math.cos(Math.toRadians(degree));
        float sin = (float) Math.sin(Math.toRadians(degree));

        float x = (cos * realPx(radius)) + mCenter.x;
        float y = (sin * realPx(radius)) + mCenter.y;

        canvas.drawPoint(x, y, mNumberPaint);
        canvas.drawPoint(mCenter.x, mCenter.y, mNumberPaint);

        canvas.save();

        canvas.translate(x, y);
        canvas.rotate(90.0f + degree);
        canvas.drawText(text, -mNumberPaint.measureText(text) / 2.0f, height, mNumberPaint);

        canvas.restore();
    }

    private void drawDirectionText(Canvas canvas) {
        //draw direction N S E W
        //N = 0, E = 90, S = 180, W = 270
        mNumberPaint.setTextSize(realPx(mClockNumberSize));
        Paint.FontMetrics fm = mNumberPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;
        float radiusPx = realPx(330) - fontHeight - realPx(mUnitPadding);

        mPaint.setTypeface((Typeface.MONOSPACE));
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(realPx(mDirectionTextSize));

        drawDirectionText(canvas, 270, "N", radiusPx, mPaint);
        mPaint.setColor(Color.WHITE);
        drawDirectionText(canvas, 0, "E", radiusPx, mPaint);
        drawDirectionText(canvas, 90, "S", radiusPx, mPaint);
        drawDirectionText(canvas, 180, "W", radiusPx, mPaint);
    }

    private void drawDirectionText(Canvas canvas, float degree, String text, float radiusPx, Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;

        float cos = (float) Math.cos(Math.toRadians(degree));
        float sin = (float) Math.sin(Math.toRadians(degree));

        float x = (cos * (radiusPx)) + mCenter.x;
        float y = (sin * (radiusPx)) + mCenter.y;

        canvas.drawPoint(x, y, paint);
        canvas.drawPoint(mCenter.x, mCenter.y, paint);

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(90.0f + degree);
        canvas.drawText(text, -paint.measureText(text) / 2.0f, height, paint);
        canvas.restore();
    }

    public void setRotate(float rotate) {
        this.mRotate = (int) rotate;
    }
}
