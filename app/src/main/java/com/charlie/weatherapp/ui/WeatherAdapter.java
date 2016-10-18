package com.charlie.weatherapp.ui;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherHolder> {

    private final LayoutInflater layoutInflater;
    private ArrayList<CityWeather> weatherArrayLis;
    private WeatherClickListener listener;
    private Context context;

    public WeatherAdapter(Context context, List<CityWeather> list, WeatherClickListener listener) {
        if (list != null) {
            weatherArrayLis = new ArrayList(list);
        } else {
            weatherArrayLis = new ArrayList();
        }
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public WeatherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createWeatherHolder(layoutInflater.inflate(R.layout.weather_card, parent, false), listener);
    }

    @Override
    public int getItemCount() {
        return weatherArrayLis.size();
    }

    @Override
    public void onBindViewHolder(WeatherHolder holder, int position) {
        CityWeather weather = weatherArrayLis.get(position);
        holder.bind(weather, context);
    }

    public void setDataSet(List<CityWeather> list) {
        weatherArrayLis.clear();
        weatherArrayLis.addAll(list);
        notifyDataSetChanged();
    }

    private CityWeather getItem(int position) {
        return weatherArrayLis.get(position);
    }

    private WeatherHolder createWeatherHolder(View itemView, final WeatherClickListener listener) {
        final WeatherHolder holder = new WeatherHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    CityWeather cityWeather = getItem(holder.getAdapterPosition());
                    listener.onWeatherClick(v, cityWeather);
                }
            }
        });
        return holder;
    }

    public interface WeatherClickListener {
        void onWeatherClick(View view, CityWeather cityWeather);
    }

    static class WeatherHolder extends RecyclerView.ViewHolder {
        private static NumberFormat formatter = new DecimalFormat("##");
        private CardView cardView;
        private TextView cityName;
        private TextView cityTemperature;
        private TextView weatherDescription;
        private ImageView temperatureIcon;

        public WeatherHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            temperatureIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
            cityName = (TextView) itemView.findViewById(R.id.cityName);
            cityTemperature = (TextView) itemView.findViewById(R.id.cityTemperature);
            weatherDescription = (TextView) itemView.findViewById(R.id.weatherDescription);
        }

        void bind(CityWeather cityWeather, Context context) {
            if (cityWeather != null) {
                cityName.setText(cityWeather.getName());
                cityTemperature.setText(context.getString(R.string.temperature,
                        formatter.format(Utils.fromKevinToCelsius(cityWeather.getMain().getTemp()))));

                if (cityWeather.getWeather() != null && !cityWeather.getWeather().isEmpty()) {
                    //Use the first item
                    Weather weather = cityWeather.getWeather().get(0);
                    weatherDescription.setText(Utils.uppercaseFirtLetter(weather.getDescription()));
                    Glide.with(context).load(weather.getIconUrl())
                            .crossFade()
                            .error(R.drawable.ic_error)
                            .into(temperatureIcon);
                } else {
                    // No weather found
                    // TODO: add default icon
                    weatherDescription.setText("");
                    Glide.with(context).load(R.drawable.ic_error)
                            .crossFade()
                            .into(temperatureIcon);
                }
            }
        }
    }
}
