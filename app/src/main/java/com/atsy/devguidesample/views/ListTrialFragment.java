package com.atsy.devguidesample.views;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atsy.devguidesample.R;
import com.atsy.devguidesample.viewmodels.ListTrialViewModel;


public class ListTrialFragment extends Fragment {

    private ListTrialViewModel mViewModel;

    public static ListTrialFragment newInstance() {
        return new ListTrialFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_trial_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ListTrialViewModel.class);
        // TODO: Use the ViewModel
    }

}