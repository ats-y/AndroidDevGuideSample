package com.atsy.devguidesample.viewmodels;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atsy.devguidesample.repositories.WeatherRepository;

import timber.log.Timber;

public class ListTrialViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public ObservableField<String> inputText = new ObservableField<>();

    private final WeatherRepository mWeatherRepository;

    public ListTrialViewModel(WeatherRepository weatherRepository){
        mWeatherRepository = weatherRepository;
    }

    public void get() {

        mWeatherRepository.get();
    }

    /**
     * ListTrialViewModelファクトリ。
     * 引数
     */
    static public class Factory implements ViewModelProvider.Factory{

        private final WeatherRepository mWeatherRepository;

        public Factory(WeatherRepository weatherRepository){
            mWeatherRepository = weatherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            Timber.d("WeatherRepository.Factory.create()");
            return (T) new ListTrialViewModel(mWeatherRepository);
        }
    }
}