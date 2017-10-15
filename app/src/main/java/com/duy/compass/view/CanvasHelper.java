package com.duy.compass.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextPaint;

public class CanvasHelper {
    private final TextPaint mNumberPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float mPixelScale;
    private int mWidth, mHeight;
    private Point mCenter;
    private float mUnitPadding;
    private float mClockNumberSize = 25;
    private float mDirectionTextSize = 25f;

    public CanvasHelper() {
    }

    public void draw(Canvas canvas) {
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        mPixelScale = ((float) mWidth) / 1000.0f;
        mCenter = new Point(mWidth / 2, mHeight / 2);
        mUnitPadding = realWidth(5);
        mDirectionTextSize = realWidth(40f);

        initPaint();

        //draw background
        canvas.drawRGB(0, 0, 0);

        drawCircle(canvas);
        drawClock(canvas);
        drawDirection(canvas);
    }

    private void initPaint() {
        mNumberPaint.setTextSize(realWidth(mClockNumberSize));
        mNumberPaint.setColor(Color.WHITE);
    }

    private void drawCircle(Canvas canvas) {
        float radius = realWidth(430);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(realWidth(3));

        canvas.drawCircle(mCenter.x, mCenter.y, radius, paint);

        paint.setColor(Color.DKGRAY);

        mNumberPaint.setTextSize(realWidth(mClockNumberSize));
        Paint.FontMetrics fm = mNumberPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;

        float strokeWidth = 20 + fontHeight;
        paint.setStrokeWidth(realWidth(strokeWidth));

        radius = realWidth(350 - strokeWidth / 2.0f - mUnitPadding);
        canvas.drawCircle(mCenter.x, mCenter.y, radius, paint);
    }

    private void drawClock(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        drawClock(canvas, paint, mCenter);
        drawClock1(canvas, paint, mCenter);
        drawNumber(canvas);
    }

    private void drawDirection(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(realWidth(5));
        Path path = new Path();

        float radian = (float) Math.toRadians(270.0d);

        float cos = (float) Math.cos((double) radian);
        float sin = (float) Math.sin((double) radian);

        float x = realWidth(320) * cos;
        float y = realWidth(320) * sin;
        path.moveTo(((float) mCenter.x) + x, ((float) mCenter.y) + y);

        x = realWidth(400) * cos;
        y = realWidth(400) * sin;
        path.lineTo(x + ((float) mCenter.x), y + ((float) mCenter.y));
        canvas.drawPath(path, paint);


        //draw triangle
        paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);

        x = mCenter.x;
        y = mCenter.y - realWidth(430 + mUnitPadding * 2);
        path.reset();
        float length = realWidth(20);
        path.lineTo(x - length / 2.0f, y - length);
        path.lineTo(x + length / 2.0f, y - length);
        path.lineTo(x, y);
        path.lineTo(x - length / 2.0f, y - length);
        canvas.drawPath(path, paint);

        //draw direction N S E W
        //N = 0, E = 90, S = 180, W = 270
        mNumberPaint.setTextSize(realWidth(mClockNumberSize));
        Paint.FontMetrics fm = mNumberPaint.getFontMetrics();
        float fontHeight = fm.bottom - fm.top + fm.leading;
        float radius = 330 - fontHeight - mUnitPadding ;
        drawText(canvas, 270, "N", mDirectionTextSize, radius);
        drawText(canvas, 0, "E", mDirectionTextSize, radius);
        drawText(canvas, 90, "S", mDirectionTextSize, radius);
        drawText(canvas, 180, "W", mDirectionTextSize, radius);
    }

    private void drawClock(Canvas canvas, Paint paint, Point center) {
        paint.setColor(Color.GRAY);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(realWidth(3));
        Path path = new Path();
        float degreeStep = 2.5f;
        for (float step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
            float cos = (float) Math.cos(step);
            float sin = (float) Math.sin(step);

            float x = realWidth(350) * cos;
            float y = realWidth(350) * sin;
            path.moveTo(x + ((float) center.x), y + ((float) center.y));

            x = realWidth(380) * cos;
            y = realWidth(380) * sin;
            path.lineTo(x + ((float) center.x), y + ((float) center.y));
        }
        canvas.drawPath(path, paint);
    }

    private float realWidth(float width) {
        return width * mPixelScale;
    }

    private void drawClock1(Canvas canvas, Paint paint, Point center) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(realWidth(5));
        Path path = new Path();
        float degreeStep = 30.0f;
        for (float step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
            float cos = (float) Math.cos(step);
            float sin = (float) Math.sin(step);

            float x = realWidth(330) * cos;
            float y = realWidth(330) * sin;
            path.moveTo(x + ((float) center.x), y + ((float) center.y));

            cos *= realWidth(380);
            sin *= realWidth(380);
            path.lineTo(cos + ((float) center.x), sin + ((float) center.y));
        }
        canvas.drawPath(path, paint);
    }

    private void drawNumber(Canvas canvas) {
        float radius = 330;
        drawText(canvas, 300.0f, "30", mClockNumberSize, radius);
        drawText(canvas, 330.0f, "60", mClockNumberSize, radius);
        drawText(canvas, 360.0f, "90", mClockNumberSize, radius);
        drawText(canvas, 30.0f, "120", mClockNumberSize, radius);
        drawText(canvas, 60.0f, "150", mClockNumberSize, radius);
        drawText(canvas, 90.0f, "180", mClockNumberSize, radius);
        drawText(canvas, 120.0f, "210", mClockNumberSize, radius);
        drawText(canvas, 150.0f, "240", mClockNumberSize, radius);
        drawText(canvas, 180.0f, "270", mClockNumberSize, radius);
        drawText(canvas, 210.0f, "300", mClockNumberSize, radius);
        drawText(canvas, 240.0f, "330", mClockNumberSize, radius);
    }

    private void drawText(Canvas canvas, float degree, String text, float textSize, float radius) {
        mNumberPaint.setTextSize(textSize);
        Paint.FontMetrics fm = mNumberPaint.getFontMetrics();
        float height = fm.bottom - fm.top + fm.leading;

        float cos = (float) Math.cos(Math.toRadians(degree));
        float sin = (float) Math.sin(Math.toRadians(degree));

        float x = (cos * realWidth(radius)) + mCenter.x;
        float y = (sin * realWidth(radius)) + mCenter.y;

        canvas.drawPoint(x, y, mNumberPaint);
        canvas.drawPoint(mCenter.x, mCenter.y, mNumberPaint);

        canvas.save();

        canvas.translate(x, y);
        canvas.rotate(90.0f + degree);
        canvas.drawText(text, -mNumberPaint.measureText(text) / 2.0f, height, mNumberPaint);

        canvas.restore();
    }
}
