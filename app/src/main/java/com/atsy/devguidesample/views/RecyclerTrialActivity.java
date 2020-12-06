package com.atsy.devguidesample.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.atsy.devguidesample.databinding.ActivityRecyclerTrialBinding;
import com.atsy.devguidesample.databinding.FragmentListTrialBinding;
import com.atsy.devguidesample.repositories.WeatherRepository;
import com.atsy.devguidesample.viewmodels.RecyclerTrialViewModel;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RecyclerTrialActivity extends AppCompatActivity {

    private ActivityRecyclerTrialBinding mViewBinding;

    @Inject
    public WeatherRepository mWeatherRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewBinding = ActivityRecyclerTrialBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());

        RecyclerTrialViewModel.RecyclerTrialViewModelFactory factory;
        factory = new RecyclerTrialViewModel.RecyclerTrialViewModelFactory(
                mWeatherRepository);
        RecyclerTrialViewModel vm = new ViewModelProvider(this, factory).get(RecyclerTrialViewModel.class);

        mViewBinding.btnTest.setOnClickListener(view -> {

            String place = mViewBinding.inputEdit.getText().toString();
            if(StringUtils.isEmpty(place)){
                return;
            }

            vm.getWeather(place);
        });
    }
}
