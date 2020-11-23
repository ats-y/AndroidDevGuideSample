package com.atsy.devguidesample.repositories;

import com.atsy.devguidesample.models.openweather.Forecast5;
import com.atsy.devguidesample.services.OpenWeather;
import com.atsy.devguidesample.services.OpenWeatherService;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class WeatherRepository {

    private String mData;

    public String GetData(){
        return mData;
    }

    public void SetData(String value){
        mData = value;
    }

    private final OpenWeather mOpenWeather;

    private final SettingsRepository mSettingsRepository;

    private Forecast5 mForecast;

    @Inject
    public WeatherRepository(SettingsRepository settingsRepository, OpenWeather api){
        mSettingsRepository = settingsRepository;
        mOpenWeather = api;
    }

    public void get(){

        // HTTPログ出力をTimber経由にする。
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(@NotNull String s) {
                        Timber.tag("okhttp").d(s);
                    }
                })
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        // HTTPクライアントを作成する。
        // （省略化）
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .writeTimeout(3000, TimeUnit.MICROSECONDS);

        // Retrofitを生成。
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mSettingsRepository.getSettings().getUrl())
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // HTTP APIの実体を生成する。
        OpenWeatherService service = retrofit.create(OpenWeatherService.class);

        service.getForecast5("sapporo", mSettingsRepository.getSettings().getApiKey())
                .enqueue(new Callback<Forecast5>() {
                    @Override
                    public void onResponse(Call<Forecast5> call, Response<Forecast5> response) {

                        mForecast = response.body();
                    }

                    @Override
                    public void onFailure(Call<Forecast5> call, Throwable t) {

                    }
                });
    }
}
