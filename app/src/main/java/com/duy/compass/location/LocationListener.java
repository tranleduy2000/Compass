package com.duy.compass.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.compass.utils.DLog;
import com.duy.compass.weather.FetchWeatherTask;

import java.io.IOException;
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
    public void onLocationChanged(@Nullable Location location) {
        DLog.d(TAG, "onLocationChanged() called with: location = [" + location + "]");

        if (mLocationValueListener != null && location != null) {
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
