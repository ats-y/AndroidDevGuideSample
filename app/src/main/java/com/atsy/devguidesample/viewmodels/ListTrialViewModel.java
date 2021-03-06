package com.atsy.devguidesample.viewmodels;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.atsy.devguidesample.models.HourlyWeather;
import com.atsy.devguidesample.models.Result;
import com.atsy.devguidesample.repositories.WeatherRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ListTrialViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public ObservableField<String> inputText = new ObservableField<>("sapporo");

    private final WeatherRepository mWeatherRepository;

    /**
     * 天気情報取得状況
     *  null:通信中。
     *  Result.Success:成功。
     *  Result.Error:失敗。
     * */
    public MutableLiveData<Result> mRet = new MutableLiveData<>();

    /**
     * 天気リスト
     */
    public MutableLiveData<List<HourlyWeather>> mHourlyWeather = new MutableLiveData<>();

    @Inject
    public ListTrialViewModel(WeatherRepository weatherRepository){
        mWeatherRepository = weatherRepository;
    }

    /**
     * 天気情報を取得する。
     */
    public void get() {

        // 状況を通信中にする。
        mRet.postValue(null);

        mHourlyWeather.postValue(new ArrayList<>());

        // 天気情報を取得する。
        mWeatherRepository.get(inputText.get(), result -> {

            // 状況に取得結果を設定。
            Timber.d("通信完了！");

            if(result instanceof Result.Success){
                mHourlyWeather.postValue(mWeatherRepository.getWeathers());
            }
            mRet.postValue(result);
        });
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