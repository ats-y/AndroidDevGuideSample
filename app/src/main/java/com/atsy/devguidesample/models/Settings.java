package com.atsy.devguidesample.models;

import com.google.gson.annotations.SerializedName;

/**
 * 設定情報
 */
public class Settings {

    /** URL */
    @SerializedName("Url")
    private String mUrl;

    /** API KEY */
    @SerializedName("ApiKey")
    private String mApiKey;

    public String getUrl(){
        return mUrl;
    }

    public String getApiKey(){
        return mApiKey;
    }
}
