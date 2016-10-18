package com.charlie.weatherapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.charlie.weatherapp.R;
import com.charlie.weatherapp.model.CityWeather;

public class WeatherDetailActivity extends AppCompatActivity {

    private static final String EXTRA_CITY_WEATHER = "cityWeather";

    public static Intent newIntent(Context context, CityWeather cityWeather) {
        Intent intent = new Intent(context, WeatherDetailActivity.class);
        intent.putExtra(EXTRA_CITY_WEATHER, cityWeather);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            CityWeather cityWeather = intent.getParcelableExtra(EXTRA_CITY_WEATHER);
            getSupportActionBar().setTitle(cityWeather.getName());
            Fragment fragment = WeatherDetailFragment.getInstance(cityWeather, false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, fragment, WeatherDetailFragment.TAG)
                    .commit();
        }
    }

}
