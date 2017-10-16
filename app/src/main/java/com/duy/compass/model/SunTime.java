package com.duy.compass.model;

/**
 * Created by Duy on 10/15/2017.
 */

public class SunTime {
    private float sunShine;
    private float sunRise;

    public SunTime(float sunShine, float sunRise) {
        this.sunShine = sunShine;
        this.sunRise = sunRise;
    }

    public float getSunShine() {
        return sunShine;
    }

    public void setSunShine(float sunShine) {
        this.sunShine = sunShine;
    }

    public float getSunRise() {
        return sunRise;
    }

    public void setSunRise(float sunRise) {
        this.sunRise = sunRise;
    }
}
