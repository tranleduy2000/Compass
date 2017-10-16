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
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mMagneticSensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float mAzimuth = 0f;
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

    private void onChangeValue() {
//        Log.d(TAG, "will set rotation from " + mCurrentAzimuth + " to " + mAzimuth);
        if (mOnValueChangedListener != null) {
            mOnValueChangedListener.onCompassRotate(mCurrentAzimuth, mAzimuth);
        }
        mCurrentAzimuth = mAzimuth;
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.mOnValueChangedListener = onValueChangedListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;
        long time = System.currentTimeMillis();
        if (time - mLastTime > mIntervalTime) {
            synchronized (this) {
                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                            * event.values[0];
                    mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                            * event.values[1];
                    mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                            * event.values[2];

                    // mGravity = event.values;

                    // Log.e(TAG, Float.toString(mGravity[0]));
                }

                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    // mGeomagnetic = event.values;

                    mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                            * event.values[0];
                    mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                            * event.values[1];
                    mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                            * event.values[2];
                    // Log.e(TAG, Float.toString(event.values[0]));

                    float magneticField = (float) Math.sqrt(mGeomagnetic[0] * mGeomagnetic[0] +
                            mGeomagnetic[1] * mGeomagnetic[1] + mGeomagnetic[2] * mGeomagnetic[2]);
                    if (mOnValueChangedListener != null) {
                        mOnValueChangedListener.onMagneticFieldChanged(magneticField);
                    }
                }


                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    mAzimuth = (float) Math.toDegrees(orientation[0]); // orientation
                    mAzimuth = (mAzimuth + 360) % 360;
                    // Log.d(TAG, "azimuth (deg): " + azimuth);

                    if (mOnValueChangedListener != null) {
                        mOnValueChangedListener.onCompassRotate(mCurrentAzimuth, mAzimuth);
                    }
                }
            }
            mLastTime = time;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public interface OnValueChangedListener {
        void onCompassRotate(float oldDegree, float newDegree);

        /**
         * @param value absolute uT value read from the magnetometer
         */
        void onMagneticFieldChanged(float value);
    }
}
