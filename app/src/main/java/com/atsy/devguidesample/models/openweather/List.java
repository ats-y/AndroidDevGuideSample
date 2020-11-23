package com.atsy.devguidesample.models.openweather;

/**
 * 5 day weather forecast APIレスポンス
 * https://openweathermap.org/forecast5#JSON
 */
public class List {
    String dt;
    Main main;
    Weather[] weather;
    String dt_txt;
}
