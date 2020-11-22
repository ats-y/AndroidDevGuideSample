package com.atsy.devguidesample.repositories;

import com.atsy.devguidesample.services.OpenWeather;

import javax.inject.Inject;

public class WeatherRepository {

    private String mData;

    public String GetData(){
        return mData;
    }

    public void SetData(String value){
        mData = value;
    }

    private OpenWeather mOpenWeather;

    @Inject
    public WeatherRepository(OpenWeather api){
        mOpenWeather = api;
    }
}
