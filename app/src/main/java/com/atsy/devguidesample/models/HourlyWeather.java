package com.atsy.devguidesample.models;

import androidx.annotation.NonNull;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HourlyWeather {

    /** 日時 */
    public Date mDateTime;

    /** 気温 */
    public double mTemp;

    /** 天気 */
    public String mWeatherText;

    /** 風速 */
    public double mWind;
    public String mWeatherDetailText;
    public long mPressure;

    @NonNull
    @Override
    public String toString() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        String formattedDate = sdf.format(mDateTime);

        return MessageFormat.format("{0} {1}",
                formattedDate, mWeatherDetailText);
    }
}
