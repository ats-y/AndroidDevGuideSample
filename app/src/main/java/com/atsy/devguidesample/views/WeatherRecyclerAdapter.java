package com.atsy.devguidesample.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atsy.devguidesample.databinding.ViewWeatherBinding;
import com.atsy.devguidesample.models.HourlyWeather;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView用の天気アダプタ
 */
public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.MyViewHolder>{

    /** 表示対象の天気リスト */
    private final List<HourlyWeather> mWeathers;

    /**
     * コンストラクタ
     * @param weathers 表示対象の天気リスト
     */
    public WeatherRecyclerAdapter(List<HourlyWeather> weathers){
        mWeathers = weathers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 要素を表示するViewをビューバインディングから生成する。
        ViewWeatherBinding viewBinding = ViewWeatherBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new MyViewHolder(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // 表示対象を取得する。
        HourlyWeather weather = mWeathers.get(position);

        // ViewHolderが保持しているビューバインディングを使って表示する。
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        holder.mViewBinding.time.setText(sdf.format(weather.mDateTime));
        holder.mViewBinding.pressure.setText(String.format(Locale.getDefault(), "%d hPa", weather.mPressure));
        holder.mViewBinding.temperature.setText(String.format(Locale.getDefault(), "%.2f ℃", weather.mTemp-270));
        holder.mViewBinding.wind.setText(String.format(Locale.getDefault(), "%.2f m/s", weather.mWind));
    }

    @Override
    public int getItemCount() {
        return mWeathers.size();
    }

    /**
     * 要素のViewHolder。
     */
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        /** Viewのビューバインディングを保持する */
        private final ViewWeatherBinding mViewBinding;

        /**
         * コンストラクタ
         * @param viewBinding ビューバインディング。
         */
        public MyViewHolder(@NonNull ViewWeatherBinding viewBinding) {
            super(viewBinding.getRoot());
            mViewBinding = viewBinding;
        }
    }
}
