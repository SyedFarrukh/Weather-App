package com.chilindoassignment.model;

import java.io.Serializable;

public class WeatherForecast implements Serializable {

    private String dateTime;
    private float temperatureMin;
    private float temperatureMax;
    private String icon;

    public WeatherForecast(String dateAndTime, float temp_min, float temp_max, String icon) {
        this.dateTime = dateAndTime;
        this.temperatureMax = temp_max;
        this.temperatureMin = temp_min;
        this.icon = icon;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

}
