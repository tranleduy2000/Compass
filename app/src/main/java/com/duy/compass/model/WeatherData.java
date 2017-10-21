package com.duy.compass.model;

/**
 * Created by Duy on 10/21/2017.
 */

public class WeatherData {
    private float temp;
    private float humidity;
    private float pressure;
    private float tempMax;
    private float tempMin;
    private Sunshine sunshine;
    private int id;

    public WeatherData() {

    }

    public WeatherData(float temp, float humidity, float pressure, float tempMax, float tempMin) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.tempMax = tempMax;
        this.tempMin = tempMin;
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
}
