package com.atsy.devguidesample.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atsy.devguidesample.models.HourlyWeather;
import com.atsy.devguidesample.models.Result;
import com.atsy.devguidesample.repositories.SettingsRepository;
import com.atsy.devguidesample.repositories.WeatherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import timber.log.Timber;

public class RecyclerTrialViewModel extends ViewModel {

    private final WeatherRepository mWeatherRepository;

    public final MutableLiveData<List<HourlyWeather>> mWeatherList = new MutableLiveData<>();

    public RecyclerTrialViewModel(WeatherRepository weatherRepository){
        mWeatherRepository = weatherRepository;
    }

    public void getWeather(String place) {

        // 天気情報を取得する。
        mWeatherList.postValue(new ArrayList<>());
        mWeatherRepository.get(place, result -> {

            // 状況に取得結果を設定。
            Timber.d("通信完了！");

            if( result instanceof Result.Success){
                mWeatherList.postValue(mWeatherRepository.getWeathers());
            }
        });
    }

    static public class RecyclerTrialViewModelFactory implements ViewModelProvider.Factory{

        private final WeatherRepository mWeatherRepository;

        public RecyclerTrialViewModelFactory(WeatherRepository weatherRepository){
            mWeatherRepository = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            Timber.d("WeatherRepository.Factory.create()");
            return (T) new RecyclerTrialViewModel(mWeatherRepository);
        }
    }
}
