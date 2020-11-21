package com.atsy.devguidesample;

import android.app.Application;

import com.atsy.devguidesample.services.LogbackTree;

import timber.log.Timber;



public class DevGuideSampleApplication extends Application {

    // DaggerIApplicationComponentは、IApplicationComponent作成直後にビルドすると自動生成される。
    public IApplicationComponent appComponent = DaggerIApplicationComponent.create();

    @Override
    public void onCreate() {
        super.onCreate();

        // ログ出力の初期設定。
        // 出力処理の実体をLogbackにする。
        Timber.plant(new LogbackTree());
        Timber.i("App Start!");
    }
}
