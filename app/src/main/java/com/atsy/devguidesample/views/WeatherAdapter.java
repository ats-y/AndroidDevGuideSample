package com.atsy.devguidesample.views;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.atsy.devguidesample.models.HourlyWeather;

public class WeatherAdapter extends ArrayAdapter<HourlyWeather> {

    public WeatherAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
