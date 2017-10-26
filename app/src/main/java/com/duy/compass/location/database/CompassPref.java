package com.duy.compass.location.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.compass.location.model.LocationData;
import com.duy.compass.location.model.Sunshine;

/**
 * Created by Duy on 10/26/2017.
 */

public class CompassPref {
    private static final String TEMPERATURE = "temp";
    private static final String PRESSURE = "pressure";
    private static final String HUMIDITY = "humidity";
    private static final String ADDRESS_LINE = "address_line";
    private static final String WEATHER_ID = "weather_id";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private static final String SUNSET = "sunset";
    private static final String SUNRISE = "sunrise";

    private SharedPreferences mPref;

    public CompassPref(Context context) {
        this.mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Nullable
    public LocationData getLastedLocationData() {
        LocationData locationData = new LocationData();
        locationData.setId(mPref.getInt(WEATHER_ID, -1));
        if (locationData.getId() == -1) {
            return null;
        }
        locationData.setTemp(mPref.getFloat(TEMPERATURE, -1));
        locationData.setPressure(mPref.getFloat(PRESSURE, -1));
        locationData.setHumidity(mPref.getFloat(HUMIDITY, -1));
        locationData.setAddressLine(mPref.getString(ADDRESS_LINE, ""));
        locationData.setLongitude(mPref.getFloat(LONGITUDE, -1));
        locationData.setLatitude(mPref.getFloat(LATITUDE, -1));
        locationData.setSunshine(new Sunshine(mPref.getLong(SUNSET, -1), mPref.getLong(SUNRISE, -1)));
        return locationData;
    }

    @NonNull
    public String getString(String key, String def) {
        try {
            return mPref.getString(key, def);
        } catch (Exception e) {
        }
        return def;
    }

    @NonNull
    public String getString(String key) {
        return getString(key, "");
    }

    public int getInt(String key, int def) {
        try {
            return mPref.getInt(key, def);
        } catch (Exception e) {
            return def;
        }
    }

    public void setInt(String key, int val) {
        mPref.edit().putInt(key, val).apply();
    }

    public void setString(String key, String val) {
        mPref.edit().putString(key, val).apply();
    }
}
