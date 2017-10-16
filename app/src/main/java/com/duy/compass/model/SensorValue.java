package com.duy.compass.model;

/**
 * Created by Duy on 10/16/2017.
 */

public class SensorValue {
    private float compassRotate = 0;
    private float magneticField = 0;

    public float getCompassRotate() {
        return compassRotate;
    }

    public void setCompassRotate(float compassRotate) {
        this.compassRotate = compassRotate;
    }

    public float getMagneticField() {
        return magneticField;
    }

    public void setMagneticField(float magneticField) {
        this.magneticField = magneticField;
    }
}
