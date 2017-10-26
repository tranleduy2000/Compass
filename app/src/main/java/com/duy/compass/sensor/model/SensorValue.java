package com.duy.compass.sensor.model;

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

    /**
     * @return roll value in angle
     */
    public float getRoll() {
        return (roll);
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public float getPitch() {
        return (pitch);
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRawPitch() {
        return pitch;
    }

    /**
     * @param azimuth - degrees
     * @param roll    - degrees
     * @param pitch   - degrees
     */
    public void setRotation(float azimuth, float roll, float pitch) {
        this.azimuth = azimuth;
        this.roll = roll;
        this.pitch = pitch;
    }
}
