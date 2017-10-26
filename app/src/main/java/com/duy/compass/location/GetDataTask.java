package com.duy.compass.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;

import com.duy.compass.location.model.LocationData;

import java.util.List;
import java.util.Locale;

/**
 * Created by Duy on 10/16/2017.
 */

public class GetDataTask extends AsyncTask<Location, Object, LocationData> {
    private static final String TAG = "LoadLocationDataTask";
    private LocationHelper.LocationDataChangeListener mLocationValueListener;
    private Context mContext;

    public GetDataTask(LocationHelper.LocationDataChangeListener listener, Context context) {
        this.mLocationValueListener = listener;
        this.mContext = context;
    }

    @Override
    protected LocationData doInBackground(Location... params) {
        Location location = params[0];

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        LocationData weatherData = new LocationData();
        weatherData.setLongitude((float) longitude);
        weatherData.setLatitude((float) latitude);
        weatherData.setAltitude(location.getAltitude());

        try {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                weatherData.setAddressLine(address.getAddressLine(0));
            }
            WeatherManager.getWeatherData(location, weatherData);
        } catch (Exception e) {
            return null;
        }
        return weatherData;
    }

    @Override
    protected void onPostExecute(LocationData locationData) {
        super.onPostExecute(locationData);
        mLocationValueListener.onUpdateLocationData(locationData);
    }
}
