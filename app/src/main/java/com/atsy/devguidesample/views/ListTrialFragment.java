package com.atsy.devguidesample.views;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.atsy.devguidesample.R;
import com.atsy.devguidesample.databinding.ListTrialFragmentBinding;
import com.atsy.devguidesample.models.HourlyWeather;
import com.atsy.devguidesample.models.Result;
import com.atsy.devguidesample.models.openweather.Weather;
import com.atsy.devguidesample.repositories.WeatherRepository;
import com.atsy.devguidesample.viewmodels.ListTrialViewModel;

import java.text.SimpleDateFormat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import timber.log.Timber;

@AndroidEntryPoint
public class ListTrialFragment extends Fragment {

    private ListTrialViewModel mViewModel;
    private ListTrialFragmentBinding mViewBinding;

    @Inject
    public WeatherRepository mWeatherRepository;

    public static ListTrialFragment newInstance() {
        return new ListTrialFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Timber.v("ListTrialFragment.onCreateView()");

        mViewBinding = ListTrialFragmentBinding.inflate(getLayoutInflater());
        return mViewBinding.getRoot();
        //return inflater.inflate(R.layout.list_trial_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Timber.v("ListTrialFragment.onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        // ViewModelを引数ありで生成するためにListTrialViewModel.Factoryを利用。
//        mViewModel = new ViewModelProvider(this).get(ListTrialViewModel.class);
        ListTrialViewModel.Factory viewModelFactory
                = new ListTrialViewModel.Factory(mWeatherRepository);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(ListTrialViewModel.class);
        mViewBinding.setViewModel(mViewModel);

        // 天気情報取得状態を監視し、状況を表示する。
        mViewModel.mRet.observe(getViewLifecycleOwner(), result -> {
            // 取得状況を表示する。
            String resultText;
            if(result == null){
                // 通信中表示。
                resultText = "---";
                mViewBinding.guruguru.setVisibility(View.VISIBLE);
                mViewBinding.weatherList.setVisibility(View.GONE);
            } else {
                mViewBinding.guruguru.setVisibility(View.GONE);
                mViewBinding.weatherList.setVisibility(View.VISIBLE);
                if( result instanceof Result.Error ){
                    // 失敗した場合。
                    resultText = ((Result.Error)result).exception.getMessage();
                } else {
                    // 成功した場合。
                    resultText = "Success";
                }
            }
            mViewBinding.textResult.setText(resultText);
        });

        // ボタンタップイベント。
        mViewBinding.btnTest.setOnClickListener(view -> {
            // 天気情報を取得する。
            Timber.d("input = %s", mViewModel.inputText.get());
            mViewModel.get();
        });

        mViewModel.mHourlyWeather.observe(getViewLifecycleOwner(), hourlyWeather -> {

            Timber.d("weather list changed.");
            WeatherAdapter adapter = new WeatherAdapter(ListTrialFragment.this.getContext(), R.layout.support_simple_spinner_dropdown_item);
            adapter.addAll(hourlyWeather);
            mViewBinding.weatherList.setAdapter(adapter);
        });
    }
}