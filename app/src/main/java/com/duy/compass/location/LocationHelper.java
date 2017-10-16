package com.duy.compass.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by Duy on 10/16/2017.
 */

public class LocationHelper {
    private static final int REQUEST_CODE = 1111;
    private static final String TAG = "LocationHelper";
    private Activity mActivity;

    public LocationHelper(Activity context) {
        this.mActivity = context;
    }

    public void onCreate() {
        if (permissionGranted()) {
            DLog.d(TAG, "onCreate() called");

            LocationManager locationManager =
                    (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
            LocationListener listener = new LocationListener(mActivity);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, listener);
        } else {
            requestPermission();
        }
    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        onCreate();
    }
}
