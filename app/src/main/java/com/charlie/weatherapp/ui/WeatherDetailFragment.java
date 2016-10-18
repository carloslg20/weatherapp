package com.charlie.weatherapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charlie.weatherapp.R;
import com.charlie.weatherapp.model.CityWeather;
import com.charlie.weatherapp.model.Weather;
import com.charlie.weatherapp.util.Utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherDetailFragment extends Fragment {

    public static final String TAG = "WeatherDetailFragment";
    private static final String EXTRA_CITY_WEATHER = "cityWeather";
    private static final String EXTRA_CITY_NAME = "cityName";


    public static Fragment getInstance(CityWeather cityWeather, boolean showCityName) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_CITY_WEATHER, cityWeather);
        arguments.putBoolean(EXTRA_CITY_NAME, showCityName);
        Fragment fragment = new WeatherDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        CityWeather cityWeather = getArguments().getParcelable(EXTRA_CITY_WEATHER);
        boolean showCityName = getArguments().getBoolean(EXTRA_CITY_NAME);

        NumberFormat formatter = new DecimalFormat("##");
        NumberFormat formatterWind = new DecimalFormat("##.##");

        ImageView weatherIcon = (ImageView) view.findViewById(R.id.weatherIcon);
        TextView cityTemperature = (TextView) view.findViewById(R.id.cityTemperature);
        TextView cityHumidity = (TextView) view.findViewById(R.id.cityHumidity);
        TextView cityWindSpeed = (TextView) view.findViewById(R.id.cityWindSpeed);
        TextView weatherDescription = (TextView) view.findViewById(R.id.weatherDescription);
        TextView cityName = (TextView) view.findViewById(R.id.cityName);
        cityName.setText(cityWeather.getName());
        cityName.setVisibility(showCityName ? View.VISIBLE : View.GONE);
        //binding
        if (cityWeather.getWeather() != null && !cityWeather.getWeather().isEmpty()) {
            //Use the first item
            Weather weather = cityWeather.getWeather().get(0);
            weatherDescription.setText(Utils.uppercaseFirtLetter(weather.getDescription()));
            Glide.with(getContext()).load(weather.getIconUrl())
                    .crossFade()
                    .error(R.drawable.ic_error)
                    .into(weatherIcon);
        } else {
            // No weather found
            // TODO: add default icon
            weatherDescription.setText("Description not found");
            Glide.with(getContext()).load(R.drawable.ic_error)
                    .crossFade()
                    .into(weatherIcon);
        }
        cityHumidity.setText(getString(R.string.humidity, formatter.format(cityWeather.getMain().getHumidity())));
        cityWindSpeed.setText(getString(R.string.wind_speed, formatterWind.format(cityWeather.getWind().getSpeed())));
        cityTemperature.setText(getString(R.string.temperature,
                formatter.format(Utils.fromKevinToCelsius(cityWeather.getMain().getTemp()))));

        return view;
    }
}