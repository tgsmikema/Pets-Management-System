package com.example.mobile.service;

import com.example.mobile.model.Centre;
import com.example.mobile.model.TimeWeightRequest;
import com.example.mobile.model.WeightData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UtilService {

    @GET("util/listAllCentres")
    Call<List<Centre>> fetchAllCentres();

    @GET("util/thisWeekStats")
    Call<WeightData> fetchThisWeekStatic(@Query("centerId") int centreId);

    @POST("util/weeklyStats")
    Call<List<WeightData>> fetchWeekDate(@Body TimeWeightRequest body);

}
