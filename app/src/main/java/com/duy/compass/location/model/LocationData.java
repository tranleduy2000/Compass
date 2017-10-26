package com.duy.compass.location.model;

/**
 * Created by Duy on 10/21/2017.
 */

public class LocationData {
    private String addressLine;
    private float longitude, latitude;
    private float temp;
    private float humidity;
    private float pressure;
    private float tempMax;
    private float tempMin;
    private Sunshine sunshine;
    private int id;
    private double altitude;

    public LocationData() {

    }

    public LocationData(float temp, float humidity, float pressure, float tempMax, float tempMin) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    @Override
    public String toString() {
        return "WeatherData{" +
                "temp=" + temp +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", sunshine=" + sunshine +
                ", id=" + id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sunshine getSunshine() {
        return sunshine;
    }

    public void setSunshine(Sunshine sunshine) {
        this.sunshine = sunshine;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}
