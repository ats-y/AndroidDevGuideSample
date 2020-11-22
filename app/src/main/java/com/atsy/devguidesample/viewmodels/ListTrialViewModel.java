package com.atsy.devguidesample.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atsy.devguidesample.repositories.WeatherRepository;

import javax.inject.Inject;

import timber.log.Timber;


public class ListTrialViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public MutableLiveData<String> inputText = new MutableLiveData<>();

    @Inject
    public WeatherRepository mWeatherRepository;

    public ListTrialViewModel(){
        inputText.postValue("abc");
    }

    @Inject
    public ListTrialViewModel(WeatherRepository weatherRepository){

        mWeatherRepository = weatherRepository;
    }

    /**
     * ListTrialViewModelファクトリ。
     * 引数
     */
    static public class Factory implements ViewModelProvider.Factory{

        private WeatherRepository mWeatherReopsitory;

        public Factory(WeatherRepository weatherRepository){
            mWeatherReopsitory = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            Timber.d("WeatherRepository.Factory.create()");
            return (T) new ListTrialViewModel(mWeatherReopsitory);
        }
    }
}