package com.atsy.devguidesample.services;

import com.atsy.devguidesample.models.openweather.Forecast5;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherService {

    @GET("forecast?mode=json")
    Call<Forecast5> getForecast5(@Query("q") String locate, @Query("appid") String apiKey);

}
