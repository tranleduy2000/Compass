package com.duy.compass.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

public class CanvasHelper {
    private static int f9236a = 5;
    private static int mDirectionWidth = 10;
    private static int mLineWidth = 7;

    public static void drawClock(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Point center = new Point(width / 2, height / 2);
        float lineLength = ((float) width) / 1200.0f;

        CanvasHelper.drawClock(canvas, paint, center, lineLength);
        CanvasHelper.drawClock1(canvas, paint, center, lineLength);
        CanvasHelper.drawDirection(canvas, center, lineLength);
    }

    private static void drawDirection(Canvas canvas, Point center, float lineLength) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth((float) mDirectionWidth);
        Path path = new Path();
        float toRadians = (float) Math.toRadians(270.0d);
        float cos = (float) Math.cos((double) toRadians);
        toRadians = (float) Math.sin((double) toRadians);
        float f2 = (350.0f * lineLength) * cos;
        float f3 = (350.0f * lineLength) * toRadians;
        cos *= 450.0f * lineLength;
        lineLength = (lineLength * 450.0f) * toRadians;
        path.moveTo(((float) center.x) + f2, ((float) center.y) + f3);
        path.lineTo(((float) center.x) + cos, lineLength + ((float) center.y));
        canvas.drawPath(path, paint);
    }

    private static void drawClock(Canvas canvas, Paint paint, Point point, float f) {
        paint.setColor(Color.GRAY);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth((float) f9236a);
        Path path = new Path();
        double degreeStep = 2.5d;
        for (double step = 0.0f; step < 2 * Math.PI; step = (step + Math.toRadians(degreeStep))) {
            float cos = (float) Math.cos(step);
            float sin = (float) Math.sin(step);


            float f3 = (350.0f * f) * cos;
            float f4 = (350.0f * f) * sin;
            path.moveTo(f4 + (float) point.y, f3 + (float) point.x);

            cos *= 380.0f * f;
            sin *= 380.0f * f;
            path.lineTo(cos + (float) point.x, sin + (float) point.y);
        }
        canvas.drawPath(path, paint);
    }

    private static void drawClock1(Canvas canvas, Paint paint, Point center, float lineLength) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth((float) mLineWidth);
        Path path = new Path();
        double degreeStep = 30.0d;
        for (double step = 0.0f; step < 2 * Math.PI; step += Math.toRadians(degreeStep)) {
            float cos = (float) Math.cos(step);
            float sin = (float) Math.sin(step);

            float r1 = 350.0f;
            float f3 = (r1 * lineLength) * cos;
            float f4 = (r1 * lineLength) * sin;
            path.moveTo(f3 + ((float) center.x), f4 + ((float) center.y));

            float r2 = 400.0f;
            cos *= r2 * lineLength;
            sin *= r2 * lineLength;
            path.lineTo(cos + ((float) center.x), sin + ((float) center.y));
        }
        canvas.drawPath(path, paint);
    }


}
