package com.duy.compass.weather;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.duy.compass.location.LocationHelper;
import com.duy.compass.weather.model.WeatherData;

/**
 * Created by Duy on 10/16/2017.
 */

public class FetchWeatherTask extends AsyncTask<Location, Object, WeatherData> {
    private LocationHelper.LocationValueListener mLocationValueListener;
    private Context context;

    public FetchWeatherTask(LocationHelper.LocationValueListener listener, Context context) {
        this.mLocationValueListener = listener;
        this.context = context;
    }

    @Override
    protected WeatherData doInBackground(Location... params) {
        return WeatherManager.getWeatherData(params[0]);
    }

    @Override
    protected void onPostExecute(WeatherData weatherData) {
        super.onPostExecute(weatherData);
        mLocationValueListener.onReceiveWeatherData(weatherData);
    }
}
