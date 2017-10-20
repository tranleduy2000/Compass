package com.duy.compass.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.compass.DLog;
import com.duy.compass.weather.sync.FetchWeatherTask;

import net.e175.klaus.solarpositioning.AzimuthZenithAngle;
import net.e175.klaus.solarpositioning.DeltaT;
import net.e175.klaus.solarpositioning.SPA;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Duy on 10/16/2017.
 */

public class LocationListener implements android.location.LocationListener {
    private static final String TAG = "LocationListener";
    @NonNull
    private Context mContext;
    @Nullable
    private LocationHelper.LocationValueListener mLocationValueListener;
    private FetchWeatherTask mFetchWeatherTask;

    public LocationListener(@NonNull Context context) {
        this.mContext = context;
    }

    public void setLocationValueListener(@Nullable LocationHelper.LocationValueListener listener) {
        this.mLocationValueListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        DLog.d(TAG, "onLocationChanged() called with: location = [" + location + "]");

        if (mLocationValueListener != null) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            String cityName = null;
            try {
                List<Address> addresses;
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    mLocationValueListener.onUpdateLocation(location, address);
                }
                DLog.d(TAG, "onLocationChanged: " + addresses);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mFetchWeatherTask != null) {
                mFetchWeatherTask.cancel(true);
            }
            mFetchWeatherTask = new FetchWeatherTask(mLocationValueListener, mContext);
            mFetchWeatherTask.execute(location);

            final GregorianCalendar dateTime = new GregorianCalendar();
            AzimuthZenithAngle position = SPA.calculateSolarPosition(
                    dateTime,
                    latitude, // latitude (degrees)
                    longitude, // longitude (degrees)
                    10, // elevation (m)
                    DeltaT.estimate(dateTime), // delta T (s)
                    1010, // avg. air pressure (hPa)
                    11); // avg. air temperature (°C)
            System.out.println("SPA: " + position);

            GregorianCalendar[] res = SPA.calculateSunriseTransitSet(
                    new GregorianCalendar(),
                    70.978056, // latitude
                    25.974722, // longitude
                    69); // delta T
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
