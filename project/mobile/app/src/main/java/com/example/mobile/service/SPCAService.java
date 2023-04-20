package com.example.mobile.service;

import com.example.mobile.SPCApplication;
import com.example.mobile.model.User;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SPCAService {
    private  OkHttpClient client;
    private Retrofit retrofit;

    public Call<User> login(String username, String password){
         client = new OkHttpClient.Builder()
                 .addInterceptor(new BasicAuthInterceptor(username,password))
                 .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SPCApplication.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.login();
        return call;
    }



}
