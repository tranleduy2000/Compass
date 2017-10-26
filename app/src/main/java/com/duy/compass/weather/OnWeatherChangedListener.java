package com.duy.compass.weather;

import com.duy.compass.weather.model.LocationData;

/**
 * Created by Duy on 10/26/2017.
 */

public interface OnWeatherChangedListener {
    public void notifyWeatherData(LocationData locationData);
}
