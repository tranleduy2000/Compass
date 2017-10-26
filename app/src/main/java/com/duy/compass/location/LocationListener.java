package com.duy.compass.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.duy.compass.utils.DLog;
import com.duy.compass.weather.LoadLocationDataTask;

/**
 * Created by Duy on 10/16/2017.
 */

public class LocationListener implements android.location.LocationListener {
    private static final String TAG = "LocationListener";
    @NonNull
    private Context mContext;
    @Nullable
    private LocationHelper.LocationDataChangeListener mLocationValueListener;
    private LoadLocationDataTask mLoadLocationData;

    public LocationListener(@NonNull Context context) {
        this.mContext = context;
    }

    public void setLocationValueListener(@Nullable LocationHelper.LocationDataChangeListener listener) {
        this.mLocationValueListener = listener;
    }

    @Override
    public void onLocationChanged(@Nullable Location location) {
        DLog.d(TAG, "onLocationChanged() called with: location = [" + location + "]");
        if (mLocationValueListener != null && location != null) {
            if (mLoadLocationData != null) {
                mLoadLocationData.cancel(true);
            }
            mLoadLocationData = new LoadLocationDataTask(mLocationValueListener, mContext);
            mLoadLocationData.execute(location);
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
