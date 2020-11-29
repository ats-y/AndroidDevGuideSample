package com.atsy.devguidesample.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.atsy.devguidesample.databinding.ViewWeatherBinding;
import com.atsy.devguidesample.models.HourlyWeather;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherAdapter extends ArrayAdapter<HourlyWeather> {

    public WeatherAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        HourlyWeather target = getItem(position);

        ViewWeatherBinding binding;
        if( convertView == null){

            binding = ViewWeatherBinding.inflate(LayoutInflater.from(getContext()));
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ViewWeatherBinding) convertView.getTag();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        binding.time.setText(sdf.format(target.mDateTime));
        binding.pressure.setText(String.format(Locale.getDefault(), "%d hPa", target.mPressure));
        binding.temperature.setText(String.format(Locale.getDefault(), "%.2f ℃", target.mTemp-270));
        binding.wind.setText(String.format(Locale.getDefault(), "%.2f m/s", target.mWind));

        Picasso.get().load(target.getIconUlr()).into(binding.icon);

        return convertView;
    }
}
