package com.charlie.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wind implements Parcelable {

    public Wind() {
    }

    //Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    private Double speed;
    //Wind direction, degrees (meteorological)
    private Double deg;


    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.speed);
        dest.writeValue(this.deg);
    }

    protected Wind(Parcel in) {
        this.speed = (Double) in.readValue(Double.class.getClassLoader());
        this.deg = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Wind> CREATOR = new Parcelable.Creator<Wind>() {
        @Override
        public Wind createFromParcel(Parcel source) {
            return new Wind(source);
        }

        @Override
        public Wind[] newArray(int size) {
            return new Wind[size];
        }
    };
}
