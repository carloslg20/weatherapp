package com.charlie.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CityWeather implements Parcelable {

    public static final Parcelable.Creator<CityWeather> CREATOR = new Parcelable.Creator<CityWeather>() {
        @Override
        public CityWeather createFromParcel(Parcel source) {
            return new CityWeather(source);
        }

        @Override
        public CityWeather[] newArray(int size) {
            return new CityWeather[size];
        }
    };
    // City ID
    private int id;
    // City name
    private String name;
    //Weather condition codes
    private List<Weather> weather;
    // Info related with the temperature
    private Main main;
    // Wind speed, Wind direction
    private Wind wind;

    public CityWeather() {
    }

    protected CityWeather(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.weather = in.createTypedArrayList(Weather.CREATOR);
        this.main = in.readParcelable(Main.class.getClassLoader());
        this.wind = in.readParcelable(Wind.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.weather);
        dest.writeParcelable(this.main, flags);
        dest.writeParcelable(this.wind, flags);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main implements Parcelable {

        public static final Parcelable.Creator<Main> CREATOR = new Parcelable.Creator<Main>() {
            @Override
            public Main createFromParcel(Parcel source) {
                return new Main(source);
            }

            @Override
            public Main[] newArray(int size) {
                return new Main[size];
            }
        };
        //Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        private double temp;
        //Humidity, %
        private double humidity;

        public Main() {
        }

        protected Main(Parcel in) {
            this.temp = in.readDouble();
            this.humidity = in.readDouble();
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        public double getHumidity() {
            return humidity;
        }

        public void setHumidity(double humidity) {
            this.humidity = humidity;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.temp);
            dest.writeDouble(this.humidity);
        }
    }
}
