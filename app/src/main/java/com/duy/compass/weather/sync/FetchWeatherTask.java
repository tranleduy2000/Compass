package com.duy.compass.weather.sync;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;

import com.duy.compass.location.LocationHelper;
import com.duy.compass.model.Sunshine;

/**
 * Created by Duy on 10/16/2017.
 */

public class FetchWeatherTask extends AsyncTask<Location, Object, Sunshine> {
    private LocationHelper.LocationValueListener mLocationValueListener;
    private Context context;

    public FetchWeatherTask(LocationHelper.LocationValueListener listener, Context context) {
        this.mLocationValueListener = listener;
        this.context = context;
    }

    @Override
    protected Sunshine doInBackground(Location... params) {
        WeatherManager weatherManager = new WeatherManager(context);
        Sunshine sunshine = weatherManager.getSunTime(params[0]);
        System.out.println("sunTime = " + sunshine);
        return sunshine;
    }

    @Override
    protected void onPostExecute(Sunshine sunshine) {
        super.onPostExecute(sunshine);
        mLocationValueListener.onUpdateSunTime(sunshine);
    }
}
