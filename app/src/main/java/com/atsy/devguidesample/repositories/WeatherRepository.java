package com.atsy.devguidesample.repositories;

import com.atsy.devguidesample.models.Result;
import com.atsy.devguidesample.models.openweather.Forecast5;
import com.atsy.devguidesample.services.OpenWeather;
import com.atsy.devguidesample.services.OpenWeatherService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.MessageFormat;
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

    private String mData;

    public String GetData(){
        return mData;
    }

    public void SetData(String value){
        mData = value;
    }

    private final OpenWeather mOpenWeather;

    private final SettingsRepository mSettingsRepository;

    /** スレッドプール */
    private final Executor mExecutor;

    private Forecast5 mForecast;

    @Inject
    public WeatherRepository(SettingsRepository settingsRepository,
                             OpenWeather api,
                             Executor executor){
        mSettingsRepository = settingsRepository;
        mOpenWeather = api;
        mExecutor = executor;
    }

    /**
     * 天気情報を取得する。
     * @param callback 取得結果のコールバック
     */
    public void get(Callback callback){

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
                    result = service.getForecast5("sapporo",
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

                // REST API呼び出し成功。
                callback.onComplete(new Result.Success(null) );
            }
        });
        Timber.d("通信要求完了！");

//        // REST APIの非同期呼び出し。
//        service.getForecast5("sapporo", mSettingsRepository.getSettings().getApiKey())
//                .enqueue(new Callback<Forecast5>() {
//                    @Override
//                    public void onResponse(Call<Forecast5> call, Response<Forecast5> response) {
//
//                        mForecast = response.body();
//                    }
//
//                    @Override
//                    public void onFailure(Call<Forecast5> call, Throwable t) {
//
//                    }
//                });
    }
}
