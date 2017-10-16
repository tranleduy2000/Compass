package com.duy.compass.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Duy on 10/16/2017.
 */

public class LocationListener implements android.location.LocationListener {
    private static final String TAG = "LocationListener";
    private Context mContext;

    public LocationListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        DLog.d(TAG, "onLocationChanged() called with: location = [" + location + "]");

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        String cityName = null;
        try {
            List<Address> addresses;
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                cityName = address.getLocality();
            }
            DLog.d(TAG, "onLocationChanged: " + addresses);
        } catch (IOException e) {
            e.printStackTrace();
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
