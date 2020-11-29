package com.atsy.devguidesample.repositories;

import com.atsy.devguidesample.models.HourlyWeather;
import com.atsy.devguidesample.models.Result;
import com.atsy.devguidesample.models.openweather.Forecast5;
import com.atsy.devguidesample.services.OpenWeatherService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;



public class WeatherRepository {

    public interface Callback<T>{
        void onComplete(Result<T> result);
    }

    private final SettingsRepository mSettingsRepository;

    /** スレッドプール */
    private final Executor mExecutor;

    /** 天気リスト */
    private List<HourlyWeather> mWeathers;

    /** 天気リストを返す */
    public List<HourlyWeather> getWeathers() {
        return mWeathers;
    }

    @Inject
    public WeatherRepository(SettingsRepository settingsRepository,
                             Executor executor){
        mSettingsRepository = settingsRepository;
        mExecutor = executor;
    }

    /**
     * 天気情報を取得する。
     * @param callback 取得結果のコールバック
     */
    public void get(String city, Callback callback){

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
        // （省略時はデフォルト値となる）
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

        // スレッドプールでREST APIを呼び出す。
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {

                // REST APIの同期呼び出し。
                Response<Forecast5> result;
                try {
                    result = service.getForecast5(city,
                            mSettingsRepository.getSettings().getApiKey()).execute();
                } catch (IOException e) {
                    // 通信失敗。
                    callback.onComplete(new Result.Error(e));
                    return;
                }

                // HTTPレスポンスコード異常。
                if( result.code() != 200 ){
                    callback.onComplete(
                            new Result.Error(
                                new Exception(
                                        MessageFormat.format("HTTP error. ResponseCode={0}}", result.code()))));
                    return;
                }

                // APIから取得したデータを天気リストに格納する。
                mWeathers = new ArrayList<>();
                for( com.atsy.devguidesample.models.openweather.List element : result.body().list ){

                    HourlyWeather w = new HourlyWeather();
                    w.mDateTime = new Date(element.dt * 1000L);
                    w.mWeatherText = element.weather[0].main;
                    w.mWeatherDetailText = element.weather[0].description;
                    w.mTemp = element.main.temp;
                    w.mWind = element.wind.speed;
                    w.mPressure = element.main.pressure;
                    w.mIcon = element.weather[0].icon;
                    mWeathers.add(w);
                }

                // REST API呼び出し成功。
                callback.onComplete(new Result.Success(null) );
            }
        });
        Timber.d("通信要求完了！");
    }
}
