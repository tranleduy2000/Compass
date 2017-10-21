package com.duy.compass.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.duy.compass.R;
import com.duy.compass.fragments.CompassFragment;
import com.duy.compass.model.WeatherData;
import com.duy.compass.util.DLog;
import com.duy.compass.util.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

/**
 * Created by Duy on 10/16/2017.
 */

public class LocationHelper {
    private static final int REQUEST_CODE = 1111;
    private static final String TAG = "LocationHelper";
    private Context mContext;
    private CompassFragment mFragment;
    @Nullable
    private LocationListener mLocationListener;
    @Nullable
    private LocationValueListener mLocationValueListener;

    public LocationHelper(CompassFragment fragment) {
        this.mContext = fragment.getContext();
        this.mFragment = fragment;
    }

    @SuppressWarnings("MissingPermission")
    public void onCreate() {
        DLog.d(TAG, "onCreate() called");
        if (permissionGranted()) {
            if (!Utility.isNetworkAvailable(mContext)) {
                return;
            }
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationListener = new LocationListener(mContext);
            mLocationListener.setLocationValueListener(mLocationValueListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
            FusedLocationProviderClient client = getFusedLocationProviderClient(mContext);
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        mLocationListener.onLocationChanged(location);
                    }
                }
            });
        } else {
            requestPermission();
        }
    }

    private boolean permissionGranted() {
        return ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED
                || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            /*requestPermission();*/
            Toast.makeText(mContext, R.string.permission_denide, Toast.LENGTH_SHORT).show();
        } else {
            onCreate();
        }
    }

    public void setLocationValueListener(LocationValueListener locationValueListener) {
        this.mLocationValueListener = locationValueListener;
        if (mLocationListener != null) {
            mLocationListener.setLocationValueListener(locationValueListener);
        }
    }

    public void networkUnavailable() {

    }

    public interface LocationValueListener {
        void onUpdateLocation(Location location, @Nullable Address name);

        void onReceiveWeatherData(@Nullable WeatherData weatherData);
    }
}
