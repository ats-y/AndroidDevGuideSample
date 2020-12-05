package com.atsy.devguidesample.models.openweather;

/**
 * 5 day weather forecast APIレスポンス
 * https://openweathermap.org/forecast5#JSON
 */
public class List {
    public long dt;
    public Main main;
    public Weather[] weather;
    public String dt_txt;
    public long pressure;
    public Wind wind;

}
