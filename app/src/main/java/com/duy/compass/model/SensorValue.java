package com.duy.compass.model;

/**
 * Created by Duy on 10/16/2017.
 */

public class SensorValue {
    private float magneticField = 0;
    private float azimuth;
    private float roll;
    private float pitch;



    public float getMagneticField() {
        return magneticField;
    }

    public void setMagneticField(float magneticField) {
        this.magneticField = magneticField;
    }


    public float getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setRotation(float azimuth, float roll, float pitch) {
        this.azimuth = azimuth;
        this.roll = roll;
        this.pitch = pitch;
    }
}
