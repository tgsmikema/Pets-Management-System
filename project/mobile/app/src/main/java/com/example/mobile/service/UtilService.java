package com.example.mobile.service;

import com.example.mobile.model.Centre;
import com.example.mobile.model.ChangePasswordRequest;
import com.example.mobile.model.Dog;
import com.example.mobile.model.DogRegisterRequest;
import com.example.mobile.model.EditUserRequest;
import com.example.mobile.model.Message;
import com.example.mobile.model.MessageRequest;
import com.example.mobile.model.UserRegisterRequest;
import com.example.mobile.model.TimeWeightRequest;
import com.example.mobile.model.User;
import com.example.mobile.model.WeightData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @GET("user/getAllUsers")
    Call<List<User>>  getAllUsers();

    @POST("user/register")
    Call<ResponseBody> register(@Body UserRegisterRequest registerRequest);

    @POST("user/editUserAccess")
    Call<ResponseBody> editUser(@Body EditUserRequest editUserRequest);

    @DELETE("user/delete")
    Call<ResponseBody> deleteUser(@Query("userId") int id);

    @POST("user/changePassword")
    Call<ResponseBody> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @GET("dog/adminListAllCentres")
    Call<List<Dog>> getAllDogFromAllCentres();

    @GET("dog/adminListOneCentre")
    Call<List<Dog>> getAllDogFromOneCentre(@Query("centreId") int centreId);

    @GET("dog/userListOwnCentre")
    Call<List<Dog>> getAllDogForNonAdminUser();

    @POST("dog/register")
    Call<ResponseBody> addNewDog(@Body DogRegisterRequest dogRegisterRequest);
}
