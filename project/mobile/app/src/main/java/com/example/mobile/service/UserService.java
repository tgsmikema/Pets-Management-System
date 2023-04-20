package com.example.mobile.service;

import com.example.mobile.model.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {
    @GET("user/login")
    Call<User> login();
}
