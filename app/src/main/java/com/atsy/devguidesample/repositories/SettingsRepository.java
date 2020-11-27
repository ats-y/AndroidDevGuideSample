package com.atsy.devguidesample.repositories;

import com.atsy.devguidesample.models.Settings;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * 設定情報リポジトリ
 */
@Singleton
public class SettingsRepository {

    /** 設定情報 */
    private Settings mSettings;

    public Settings getSettings(){
        return mSettings;
    }

    /**
     * コンストラクタ
     */
    @Inject
    public SettingsRepository(){
        mSettings = null;
    }

    /**
     * 設定情報読取
     * @param path 設定情報保存パス
     * @throws Exception 読取失敗
     */
    public void load(String path) throws Exception{

        InputStreamReader isr = new InputStreamReader( new FileInputStream(path));
        JsonReader jsr = new JsonReader( isr );
        Gson gson = new Gson();
        mSettings = gson.fromJson( jsr, Settings.class );

        Timber.d("Settings url=%s, ApiKey=%s", mSettings.getUrl(), mSettings.getApiKey());
    }
}
