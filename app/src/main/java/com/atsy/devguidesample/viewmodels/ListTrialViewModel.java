package com.atsy.devguidesample.viewmodels;

import androidx.lifecycle.ViewModel;

import com.atsy.devguidesample.repositories.WeatherRepository;

import javax.inject.Inject;


public class ListTrialViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    @Inject
    public WeatherRepository mWeatherRepository;

    public ListTrialViewModel(){

    }

    public ListTrialViewModel(WeatherRepository weatherRepository){

        mWeatherRepository = weatherRepository;
    }
}