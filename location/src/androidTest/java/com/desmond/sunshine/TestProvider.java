package com.desmond.sunshine;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.desmond.sunshine.data.WeatherContract.LocationEntry;
import com.desmond.sunshine.data.WeatherContract.WeatherEntry;

/**
 * Test runner will execute all the functions in this class that start with "test"
 * and in the order by which they are declared here.
 * Each function should have at least one assert function
 */
public class TestProvider extends AndroidTestCase {

    public static final String TAG = TestProvider.class.getSimpleName();
    static final String KALAMAZOO_LOCATION_SETTING = "kalamazoo";
    static final String KALAMAZOO_WEATHER_START_DATE = "20140625";

    long locationRowId;

    // The target api annotation is needed for the call to keySet -- we wouldn't want
    // to use this in our app, but in a test it's fine to assume a higher target
    void addAllContentValues(ContentValues destination, ContentValues source) {
        for (String key : source.keySet())
            destination.put(key, source.getAsString(key));
    }

    static ContentValues createWeatherValues(long locationRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, "20141205");
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.2);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.3);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 75);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 65);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 5.5);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 321);

        return weatherValues;
    }

    static ContentValues createKalamazooWeatherValues(long locationRowId) {
        ContentValues weatherValues = new ContentValues();
        weatherValues.put(WeatherEntry.COLUMN_LOC_KEY, locationRowId);
        weatherValues.put(WeatherEntry.COLUMN_DATETEXT, KALAMAZOO_WEATHER_START_DATE);
        weatherValues.put(WeatherEntry.COLUMN_DEGREES, 1.2);

        weatherValues.put(WeatherEntry.COLUMN_HUMIDITY, 1.5);
        weatherValues.put(WeatherEntry.COLUMN_PRESSURE, 1.1);
        weatherValues.put(WeatherEntry.COLUMN_MAX_TEMP, 85);
        weatherValues.put(WeatherEntry.COLUMN_MIN_TEMP, 35);
        weatherValues.put(WeatherEntry.COLUMN_SHORT_DESC, "Cats and Dogs");
        weatherValues.put(WeatherEntry.COLUMN_WIND_SPEED, 3.4);
        weatherValues.put(WeatherEntry.COLUMN_WEATHER_ID, 42);

        return weatherValues;
    }

    static ContentValues createKalamazooLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(LocationEntry.COLUMN_LOCATION_SETTING, KALAMAZOO_LOCATION_SETTING);
        testValues.put(LocationEntry.COLUMN_CITY_NAME, "Kalamazoo");
        testValues.put(LocationEntry.COLUMN_COORD_LAT, 42.2917);
        testValues.put(LocationEntry.COLUMN_COORD_LONG, -85.5872);

        return testValues;
    }

    public void testInsertReadProvider() {
        setUp();

        //Create a new map of values, where columns names are the keys
        ContentValues testLocationValues = TestDb.createLocationValues();

        Uri locationInsertUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testLocationValues);
        assertTrue(locationInsertUri != null);

        locationRowId = ContentUris.parseId(locationInsertUri);

        // A cursor is your primary interface to the query results.
        Cursor locationCursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(locationCursor, testLocationValues);
        locationCursor.close();

        // Now see if we can successfully query if we include the row id
        locationCursor = mContext.getContentResolver().query(
                LocationEntry.buildLocationUri(locationRowId),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(locationCursor, testLocationValues);
        locationCursor.close();

        ContentValues testWeatherValues = createWeatherValues(locationRowId);

        Uri weatherInsertUri = mContext.getContentResolver().insert(WeatherEntry.CONTENT_URI, testWeatherValues);
        assertTrue(weatherInsertUri != null);

        long weatherRowId = ContentUris.parseId(weatherInsertUri);

        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,       // leaving "columns" null just returns all the columns
                null,       // columns for "where" clause
                null,       // values for "where" clause
                null        // columns to group by
        );

        TestDb.validateCursor(weatherCursor, testWeatherValues);
        weatherCursor.close();

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        addAllContentValues(testWeatherValues, testLocationValues);

        // Get the joined Weather and Location data
        Cursor weatherAndLocationCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocation(TestDb.TEST_LOCATION),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(weatherAndLocationCursor, testWeatherValues);
        weatherAndLocationCursor.close();

        // Get the joined Weather and Location data with a start date
        weatherAndLocationCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithStartDate(
                        TestDb.TEST_LOCATION, TestDb.TEST_DATE),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(weatherAndLocationCursor, testWeatherValues);
        weatherAndLocationCursor.close();

        weatherAndLocationCursor = mContext.getContentResolver().query(
                WeatherEntry.buildWeatherLocationWithDate(
                        TestDb.TEST_LOCATION, TestDb.TEST_DATE),
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(weatherAndLocationCursor, testLocationValues);
        weatherAndLocationCursor.close();

    }

    public void testUpdateLocation() {
        setUp();

        // Create a new map of values, where column names are the keys
        ContentValues values = TestDb.createLocationValues();

        Uri locationUri = mContext.getContentResolver().insert(
                LocationEntry.CONTENT_URI,
                values
        );
        locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back
        assertTrue(locationRowId != -1);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(LocationEntry._ID, locationRowId);
        updatedValues.put(LocationEntry.COLUMN_CITY_NAME, "Santa's Village");

        int numRowUpdated = mContext.getContentResolver().update(
                LocationEntry.CONTENT_URI,
                updatedValues,
                LocationEntry._ID + "= ?",
                new String[] { Long.toString(locationRowId) }
        );
        assertEquals(numRowUpdated, 1);

        // A cursor is your primary interface to the query result
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestDb.validateCursor(cursor, updatedValues);
        cursor.close();
    }

    public void testDeleteProvider() {
        deleteAllRecords();
    }



    public void setUp() {
        deleteAllRecords();
    }

    public void deleteAllRecords() {
        // Brings our database to an empty state
        mContext.getContentResolver().delete(
                WeatherEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                LocationEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                LocationEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    // Insert both the location and weather data for the Kalamazoo data set
    public void insertKalamazooData() {
        ContentValues kalamazooLocationValues = createKalamazooLocationValues();
        Uri locationInsertUri = mContext.getContentResolver().insert(
                LocationEntry.CONTENT_URI,
                kalamazooLocationValues
        );
        assertTrue(locationInsertUri != null);

        locationRowId = ContentUris.parseId(locationInsertUri);

        ContentValues kalamazooWeatherValues = createKalamazooWeatherValues(locationRowId);
        Uri weatherInsertUri = mContext.getContentResolver().insert(
                WeatherEntry.CONTENT_URI,
                kalamazooWeatherValues
        );
        assertTrue(weatherInsertUri != null);
    }

    public void testUpdateAndReadWeather() {
        insertKalamazooData();
        String newDescription = "Cats and Frogs (don't warn the tadpoles!)";

        // Make an update to one value
        ContentValues kalamazooUpdate = new ContentValues();
        kalamazooUpdate.put(WeatherEntry.COLUMN_SHORT_DESC, newDescription);

        mContext.getContentResolver().update(
                WeatherEntry.CONTENT_URI,
                kalamazooUpdate,
                null,
                null
        );

        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make the same update to the full ContentValues for comparison
        ContentValues kalamazooAltered = createKalamazooWeatherValues(locationRowId);
        kalamazooAltered.put(WeatherEntry.COLUMN_SHORT_DESC, newDescription);

        TestDb.validateCursor(weatherCursor, kalamazooAltered);
        weatherCursor.close();
    }

    public void testRemoveHumidityAndReadWeather() {
        setUp();
        insertKalamazooData();

        mContext.getContentResolver().delete(
                WeatherEntry.CONTENT_URI,
                WeatherEntry.COLUMN_HUMIDITY + " = " + locationRowId,
                null
        );

        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make the same update to the full ContentValues for comparison.
        ContentValues kalamazooAltered = createKalamazooWeatherValues(locationRowId);
        kalamazooAltered.remove(WeatherEntry.COLUMN_HUMIDITY);

        TestDb.validateCursor(weatherCursor, kalamazooAltered);
        weatherCursor.close();
    }

    public void testGetType() {
        // content://com.desmond.sunshine.app/weather/
        String type = mContext.getContentResolver().getType(WeatherEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.desmond.sunshine.app/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testLocation = "97074";
        // content://com.desmond.sunshine.app/weather/94074
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocation(testLocation));
        // vnd.android.cursor.dir/com.desmond.sunshine.app/weather
        assertEquals(WeatherEntry.CONTENT_TYPE, type);

        String testDate = "20140612";
        // content://com.desmond.sunshine.app/weather/94074/20140612
        type = mContext.getContentResolver().getType(
                WeatherEntry.buildWeatherLocationWithDate(testLocation, testDate));
        // vnd.android.cursor.item/com.desmond.sunshine.app/weather
        assertEquals(WeatherEntry.CONTENT_ITEM_TYPE, type);

        // content://com.desmond.sunshine.app/location
        type = mContext.getContentResolver().getType(LocationEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.desmond.sunshine.app/location
        assertEquals(LocationEntry.CONTENT_TYPE, type);

        // content://com.desmond.sunshine.app/location/1
        type = mContext.getContentResolver().getType(LocationEntry.buildLocationUri(1L));
        // vnd.android.cursor.item/com.desmond.sunshine.app/location
        assertEquals(LocationEntry.CONTENT_ITEM_TYPE, type);
    }
}
