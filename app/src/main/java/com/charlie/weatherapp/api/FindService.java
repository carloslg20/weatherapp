package com.charlie.weatherapp.api;

import com.charlie.weatherapp.model.CitiesFindResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FindService {
    @GET("find")
    Call<CitiesFindResponse> listCities(@Query("lat") String lat,
                                        @Query("lon") String lon,
                                        @Query("cnt") int counter);
}
