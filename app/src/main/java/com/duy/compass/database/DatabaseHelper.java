package com.duy.compass.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;

import java.util.Vector;

import static com.duy.compass.weather.data.WeatherContract.LocationEntry;
import static com.duy.compass.weather.data.WeatherContract.WeatherEntry;

/**
 * Created by Duy on 10/16/2017.
 */

public class DatabaseHelper {
    private static final boolean DEBUG = true;
    private static final String TAG = "DatabaseHelper";
    private Context mContext;

    public DatabaseHelper(Context context) {
        this.mContext = context;
    }

    public long insertLocationInDatabase(String locationSetting, String cityName, double lat, double lon) {
        long rowId = 0;

        // First check if the location with this city name exists in the db
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                new String[]{LocationEntry._ID},
                LocationEntry.COLUMN_LOCATION_SETTING + " = ?",
                new String[]{locationSetting},
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {

            int locationIdIndex = cursor.getColumnIndex(LocationEntry._ID);
            rowId = cursor.getLong(locationIdIndex);

        } else {

            ContentValues locationValues = new ContentValues();
            locationValues.put(LocationEntry.COLUMN_LOCATION_SETTING, locationSetting);
            locationValues.put(LocationEntry.COLUMN_CITY_NAME, cityName);
            locationValues.put(LocationEntry.COLUMN_COORD_LAT, lat);
            locationValues.put(LocationEntry.COLUMN_COORD_LONG, lon);

            Uri uri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, locationValues);
            rowId = ContentUris.parseId(uri);

        }

        if (cursor != null) {
            cursor.close();
        }

        return rowId;
    }

    public void insertWeatherIntoDatabase(Vector<ContentValues> CVVector) {
        if (CVVector.size() > 0) {
            ContentValues[] contentValuesArray = new ContentValues[CVVector.size()];
            CVVector.toArray(contentValuesArray);

            int rowsInserted = mContext.getContentResolver().bulkInsert(WeatherEntry.CONTENT_URI, contentValuesArray);

            // Use a DEBUG variable to gate whether or not you do this, so you can easily
            // turn it on and off, and so that it's easy to see what you can rip out if
            // you ever want to remove it.
            if (DEBUG) {
                Cursor weatherCursor = mContext.getContentResolver().query(
                        WeatherEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null
                );

                if (weatherCursor.moveToFirst()) {
                    ContentValues resultValues = new ContentValues();
                    DatabaseUtils.cursorRowToContentValues(weatherCursor, resultValues);
                    Log.v(TAG, "Query succeeded! **********");
                    for (String key : resultValues.keySet()) {
                        Log.v(TAG, key + ": " + resultValues.getAsString(key));
                    }
                } else {
                    Log.v(TAG, "Query failed! :( **********");
                }
            }
        }
    }
}
