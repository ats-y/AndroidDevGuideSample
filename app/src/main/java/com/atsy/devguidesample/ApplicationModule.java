package com.atsy.devguidesample;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/**
 * Daggerコンポーネント
 * スコープ＝Application単位
 */
@Module
@InstallIn(ApplicationComponent.class)
public class ApplicationModule {

    /**
     * 非同期処理用スレッドプールの生成方法を提供。
     * @return  非同期処理用のスレッドプール
     */
    @Singleton
    @Provides
    public static Executor provideExecutorService(){
        return Executors.newFixedThreadPool(4);
    }
}
