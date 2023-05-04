package com.example.mobile.service;

import com.example.mobile.model.Centre;
import com.example.mobile.model.Message;
import com.example.mobile.model.MessageRequest;
import com.example.mobile.model.TimeWeightRequest;
import com.example.mobile.model.User;
import com.example.mobile.model.WeightData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
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
    Call<List<WeightData>> fetchWeekData(@Body TimeWeightRequest body);

    @POST("util/monthlyStatus")
    Call<List<WeightData>> fetchMonthData(@Body TimeWeightRequest body);

    @GET("chat/getAlreadyMessagedPeopleList")
    Call<List<User>> fetchAllChattedUser(@Query("currentUserId") int userId);

    @GET("chat/getNeverMessagedPeopleList")
    Call<List<User>> fetchAllUnChattedUser(@Query("currentUserId") int userId);

    @GET("chat/getChatHistory")
    Call<List<Message>> fetchChatHistory(@Query("currentUserId") int userId, @Query("chatToUserId") int chatToUserId);

    @POST("chat/send")
    Call<ResponseBody> sendMessage(@Body MessageRequest messageRequest);

}
