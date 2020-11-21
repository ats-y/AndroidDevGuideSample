package com.atsy.devguidesample;

import com.atsy.devguidesample.views.ListTrialActivity;
import com.atsy.devguidesample.views.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface IApplicationComponent {

    /**
     * DaggerにMainActivityが依存注入を要求していること伝える。
     * @param mainActivity
     */
    void inject(MainActivity mainActivity);
}
