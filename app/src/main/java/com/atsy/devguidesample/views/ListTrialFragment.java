package com.atsy.devguidesample.views;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atsy.devguidesample.databinding.ListTrialFragmentBinding;
import com.atsy.devguidesample.repositories.WeatherRepository;
import com.atsy.devguidesample.viewmodels.ListTrialViewModel;

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
        //mViewModel = new ViewModelProvider(this).get(ListTrialViewModel.class);
        ListTrialViewModel.Factory viewModelFactory
                = new ListTrialViewModel.Factory(mWeatherRepository);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(ListTrialViewModel.class);

        mViewBinding.setViewModel(mViewModel);

        // TODO: Use the ViewModel
        mViewBinding.btnTest.setOnClickListener(view -> {
            Timber.d("input = %s", mViewModel.inputText.get());

            mViewModel.get();
        });
    }

}