package com.atsy.devguidesample.repositories;

import com.atsy.devguidesample.services.OpenWeather;

import javax.inject.Inject;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;

@Module
@InstallIn(ActivityRetainedComponent.class)
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
