package com.atsy.devguidesample.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.atsy.devguidesample.databinding.ActivityRecyclerTrialBinding;
import com.atsy.devguidesample.databinding.FragmentListTrialBinding;
import com.atsy.devguidesample.repositories.WeatherRepository;
import com.atsy.devguidesample.viewmodels.RecyclerTrialViewModel;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecyclerTrialActivity extends AppCompatActivity {

    /** ビューバインディング */
    private ActivityRecyclerTrialBinding mViewBinding;

    @Inject
    public WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ビューバインディングからビューを生成する。
        mViewBinding = ActivityRecyclerTrialBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        // ViewModelを生成する。
        RecyclerTrialViewModel.RecyclerTrialViewModelFactory factory;
        factory = new RecyclerTrialViewModel.RecyclerTrialViewModelFactory(
                mWeatherRepository);
        RecyclerTrialViewModel vm = new ViewModelProvider(this, factory).get(RecyclerTrialViewModel.class);

        // 天気取得ボタン。
        mViewBinding.btnTest.setOnClickListener(view -> {

            String place = mViewBinding.inputEdit.getText().toString();
            if(StringUtils.isEmpty(place)){
                return;
            }

            vm.getWeather(place);
        });

        // RecyclerViewの設定。
        mViewBinding.weatherList.setHasFixedSize(true);
        mViewBinding.weatherList.setLayoutManager(
                new LinearLayoutManager(this));

        // ViewModelの天気リストを監視する。
        vm.mWeatherList.observe(this, weathers -> {

            // RecyclerViewに天気リストを表示する。
            WeatherRecyclerAdapter adapter = new WeatherRecyclerAdapter(weathers);
            mViewBinding.weatherList.setAdapter(adapter);
        });
    }
}
