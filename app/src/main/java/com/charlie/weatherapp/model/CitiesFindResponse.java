package com.charlie.weatherapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CitiesFindResponse extends BaseListResponse {

    private List<CityWeather> list;

    public List<CityWeather> getList() {
        return list;
    }

    private void setList(CityWeather[] list) {
        this.list = Arrays.asList(list);
    }

}
