package com.duy.compass.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Duy on 10/15/2017.
 */

public class Sunshine {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH);
    private long sunset;
    private long sunrise;

    public Sunshine(long sunset, long sunrise) {
        this.sunset = sunset;
        this.sunrise = sunrise;
    }

    @Override
    public String toString() {
        return "SunTime{" +
                "sunset=" + DATE_FORMAT.format(new Date(sunset)) +
                ", sunrise=" + DATE_FORMAT.format(new Date(sunrise)) +
                '}';
    }

    public float getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }
}
