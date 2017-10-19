package com.duy.compass.weather.sync;

import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;

import com.duy.compass.DLog;
import com.duy.compass.database.DatabaseHelper;
import com.duy.compass.model.Sunshine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * SyncAdapter
 */
public class WeatherManager {


    // Interval at which to sync with the weather, in seconds
    // 60 seconds (1min) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;


    // These indices must match the projection
    private static final int INDEX_WEATHER_ID = 0;
    private static final int INDEX_MAX_TEMP = 1;
    private static final int INDEX_MIN_TEMP = 2;
    private static final int INDEX_SHORT_DESC = 3;

    private static final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final int WEATHER_NOTIFICATION_ID = 3004;
    private static final String TAG = "WeatherManager";
    private boolean DEBUG = true;
    private DatabaseHelper mDatabaseHelper;
    private Context mContext;

    public WeatherManager(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public Vector<ContentValues> getWeatherData(Location location) {
        DLog.d(TAG, "getWeatherData() called with: location = [" + location + "]");

        String weatherForecast = getWeatherForecastData(location.getLongitude(), location.getLatitude());
        try {
            return getWeatherDataFromJson(weatherForecast);
        } catch (JSONException e) {
            e.printStackTrace();
            return new Vector<>();
        }
    }

    @Nullable
    public Sunshine getSunTime(Location location) {
        DLog.d(TAG, "getWeatherData() called with: location = [" + location + "]");

        String weatherForecast = getWeatherForecastData(location.getLongitude(), location.getLatitude());
        try {
            return getSunTimeFromJson(weatherForecast);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private Vector<ContentValues> getWeatherDataFromJson(String forecastJsonStr)
            throws JSONException {
        DLog.d(TAG, "getWeatherDataFromJson() called with: forecastJsonStr = [" + forecastJsonStr + "]");

        final String OWM_COORD = "coord";
        final String OWM_COORD_LAT = "lat";
        final String OWM_COORD_LONG = "lon";

        // sys information
        final String OWM_SYS = "sys";
        final String OWM_COUNTRY = "country";
        final String OHM_SUNRISE = "sunrise";
        final String OWM_SUNSET = "sunset";

        final String OWM_DATETIME = "dt";
        final String OWM_PRESSURE = "pressure";
        final String OWM_HUMIDITY = "humidity";
        final String OWM_WINDSPEED = "speed";
        final String OWM_WIND_DIRECTION = "deg";

        // All temperatures are children of the "temp" object
        final String OWM_MAIN_TEMP = "temp";
        final String OWM_MAIN_TEMP_MAX = "temp_max";
        final String OWM_MAIN_TEMP_MIN = "temp_min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_WEATHER_ID = "id";

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_WEATHER);

//        JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
//        String cityName = cityJson.getString(OWM_CITY_NAME);
//        JSONObject coordJSON = cityJson.getJSONObject(OWM_COORD);
//        double cityLatitude = coordJSON.getDouble(OWM_COORD_LAT);
//        double cityLongitude = coordJSON.getDouble(OWM_COORD_LONG);


        // Get and insert the new weather information into the database
        Vector<ContentValues> cVVector = new Vector<>(weatherArray.length());

//        for (int i = 0; i < weatherArray.length(); i++) {
//            //  These are the values that will be collected
//
//            long dateTime;
//            double pressure;
//            int humidity;
//            double windSpeed;
//            double windDirection;
//
//            double high;
//            double low;
//
//            String description;
//            int weatherId;
//
//            // Get the JSON object representing the day
//            JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//            // The date/time is returned as a long.  We need to convert that
//            // into something human-readable, since most people won't read "1400356800" as
//            // "this saturday".
//            dateTime = dayForecast.getLong(OWM_DATETIME);
//
//            pressure = dayForecast.getDouble(OWM_PRESSURE);
//            humidity = dayForecast.getInt(OWM_HUMIDITY);
//            windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
//            windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);
//
//            // description is in a child array called "weather", which is 1 element long.
//            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//            description = weatherObject.getString(OWM_DESCRIPTION);
//            weatherId = weatherObject.getInt(OWM_WEATHER_ID);
//
//            // Temperatures are in a child object called "temp".  Try not to name variables
//            // "temp" when working with temperature.  It confuses everybody.
//            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_MAIN_TEMP);
//            high = temperatureObject.getDouble(OWM_MAIN_TEMP_MAX);
//            low = temperatureObject.getDouble(OWM_MIN);
//
//            ContentValues weatherValues = new ContentValues();
//
////            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationID);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATETEXT, WeatherContract.getDbDateString(new Date(dateTime * 1000L)));
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, description);
//            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId);
//
//            cVVector.add(weatherValues);
//        }
        /** Insert weather data into database */
//        mDatabaseHelper.insertWeatherIntoDatabase(cVVector);
        return cVVector;
    }

    private Sunshine getSunTimeFromJson(String forecastJsonStr) throws JSONException {
        DLog.d(TAG, "getSunTimeFromJson() called with: forecastJsonStr = [" + forecastJsonStr + "]");

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONObject sysJson = forecastJson.getJSONObject("sys");
        long sunrise = sysJson.getLong("sunrise");
        long sunset = sysJson.getLong("sunset");
        return new Sunshine(sunset * 1000, sunrise * 1000);
    }

    private String getWeatherForecastData(double lon, double lat) {
        //These two need to be declared outside the try/catch
        //so that they can be closed in the finally block
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Wil contain the raw JSON response as a string
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 14;
        String key = "632bd08931e170a4e19c711abab52be3";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String forecastBaseUrl = "http://api.openweathermap.org/data/2.5/weather?";
            final String longitudeParam = "lon";
            final String latitudeParam = "lat";
            final String apiKey = "appid";

            Uri builtUri = Uri.parse(forecastBaseUrl).buildUpon()
                    .appendQueryParameter(longitudeParam, String.valueOf(lon))
                    .appendQueryParameter(latitudeParam, String.valueOf(lat))
                    .appendQueryParameter(apiKey, key)
                    .build();

            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                //Nothing to do
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                //Since it's JSON, adding a newline isn't necessary (it won't affect
                //parsing) But it does make debugging a lot easier if you print out the
                //completed buffer for debugging
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                //Stream was empty. No point in parsing
                return null;
            }
            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("MainFragment", "Error: ", e);
            //If the code didn't successfully get the weather data, there's no point in attempting to parse it
            forecastJsonStr = null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("MainFragment", "Error closing stream", e);
                }
            }
        }

        return forecastJsonStr;
    }

}
