package com.atsy.devguidesample.models.openweather;

/**
 * 5 day weather forecast APIレスポンス
 * https://openweathermap.org/forecast5#JSON
 */
public class Forecast5 {

    public String cod;
    public String message;
    public List[] list;
}
