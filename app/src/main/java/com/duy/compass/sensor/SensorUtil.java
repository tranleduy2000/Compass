package com.duy.compass.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

/**
 * Created by Duy on 10/21/2017.
 */

public class SensorUtil {
    public static boolean hasGravitySensor(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensors = sensorManager.getSensorList(Sensor.TYPE_GRAVITY);
        return !listSensors.isEmpty();
    }

    public static boolean hasMagnetometer(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> listSensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        return !listSensors.isEmpty();
    }

    public static boolean hasAccelerometer(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> listSensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        return !listSensors.isEmpty();
    }

    public static boolean hasRotationVector(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> listSensors = sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
        return !listSensors.isEmpty();
    }
}
