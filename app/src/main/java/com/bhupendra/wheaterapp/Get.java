package com.bhupendra.wheaterapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Get {
    @GET("weather")
    Call<Model> getWeatherData(@Query("q") String city, @Query("appid") String appid);
}
