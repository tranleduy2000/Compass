package com.duy.compass.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.Nullable;

/**
 * Created by Duy on 10/15/2017.
 */

public class SensorListener implements SensorEventListener {
    private static final String TAG = "CompassListener";
    private final float mOrientation[] = new float[3];
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mMagneticSensor;
    private Sensor mOrientationSensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float mCurrentAzimuth = 0;
    @Nullable
    private OnValueChangedListener mOnValueChangedListener;
    private int mIntervalTime = 0;
    private long mLastTime = 0;
    private float[] R = new float[9];
    private float[] I = new float[9];

    public SensorListener(Context context) {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mMagneticSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.mOnValueChangedListener = onValueChangedListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mOnValueChangedListener == null) return;
        final float alpha = 0.97f;
        long time = System.currentTimeMillis();
        if (time - mLastTime > mIntervalTime) {
            synchronized (this) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    mGravity[0] = alpha * mGravity[0] + (1 - alpha) * event.values[0];
                    mGravity[1] = alpha * mGravity[1] + (1 - alpha) * event.values[1];
                    mGravity[2] = alpha * mGravity[2] + (1 - alpha) * event.values[2];
                }

                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    // mGeomagnetic = event.values;
                    mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha) * event.values[0];
                    mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha) * event.values[1];
                    mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha) * event.values[2];

                    // Log.e(TAG, Float.toString(event.values[0]));
                    float magneticField = (float) Math.sqrt(mGeomagnetic[0] * mGeomagnetic[0]
                            + mGeomagnetic[1] * mGeomagnetic[1]
                            + mGeomagnetic[2] * mGeomagnetic[2]);

                    mOnValueChangedListener.onMagneticFieldChanged(magneticField);
                }


                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    SensorManager.getOrientation(R, mOrientation);
                    float azimuth = (float) Math.toDegrees(mOrientation[0]);
                    azimuth = (azimuth + 360) % 360;
                    float pitch = (float) Math.toDegrees(mOrientation[1]);
                    float roll = (float) Math.toDegrees(mOrientation[2]);

                    mOnValueChangedListener.onRotationChanged(azimuth, pitch, roll);
                }
            }
            mLastTime = time;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface OnValueChangedListener {
        void onRotationChanged(float azimuth, float pitch, float oldDegree);

        /**
         * @param value absolute uT value read from the magnetometer
         */
        void onMagneticFieldChanged(float value);
    }
}
