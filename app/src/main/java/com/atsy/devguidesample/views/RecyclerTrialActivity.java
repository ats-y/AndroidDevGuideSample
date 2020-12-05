package com.atsy.devguidesample.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.atsy.devguidesample.databinding.FragmentListTrialBinding;

public class RecyclerTrialActivity extends AppCompatActivity {

    private FragmentListTrialBinding mViewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewBinding = FragmentListTrialBinding.inflate(getLayoutInflater());
        setContentView(mViewBinding.getRoot());
    }
}
