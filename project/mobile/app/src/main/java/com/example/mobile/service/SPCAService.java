package com.example.mobile.service;

import com.example.mobile.SPCApplication;
import com.example.mobile.model.Centre;
import com.example.mobile.model.Message;
import com.example.mobile.model.MessageRequest;
import com.example.mobile.model.TimeWeightRequest;
import com.example.mobile.model.User;
import com.example.mobile.model.WeightData;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SPCAService {
    private  OkHttpClient client;
    private Retrofit retrofit;

    public void initRetrofit(String username,String password){
        client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(username,password))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SPCApplication.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void initRetrofit(String token){
        client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(SPCApplication.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<User> login(String username, String password){
        initRetrofit(username,password);
        UserService userService = retrofit.create(UserService.class);
        Call<User> call = userService.login();
        return call;
    }

    public Call<List<Centre>> fetchAllCentres(String token){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<List<Centre>> call = utilService.fetchAllCentres();
        return call;
    }

    public Call<WeightData> fetchThisWeekStatist(String token, int centreId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<WeightData> weekWeightDataCall = utilService.fetchThisWeekStatic(centreId);
        return weekWeightDataCall;
    }

    public Call<List<WeightData>> fetchWeekData(String token, TimeWeightRequest body){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<List<WeightData>> listCall = utilService.fetchWeekData(body);
        return listCall;
    }

    public Call<List<WeightData>> fetchMonthData(String token, TimeWeightRequest body){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<List<WeightData>> listCall = utilService.fetchMonthData(body);
        return listCall;
    }

    public Call<List<User>> fetchAllChattedUsers(String token, int userId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<List<User>> listCall = utilService.fetchAllChattedUser(userId);
        return listCall;
    }

    public Call<List<User>> fetchAllUnChattedUsers(String token, int userId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<List<User>> listCall = utilService.fetchAllUnChattedUser(userId);
        return listCall;
    }

    public Call<List<Message>> fetchChatHistory(String token, int userId,int chatToUserId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<List<Message>> listCall = utilService.fetchChatHistory(userId, chatToUserId);
        return listCall;
    }


    public Call<ResponseBody> sendMessage(String token, int userId, int chatToUserId, String messageContent){
         initRetrofit(token);
         UtilService utilService = retrofit.create(UtilService.class);
        Call<ResponseBody> responseCall = utilService.sendMessage(new MessageRequest(userId, chatToUserId, messageContent));
        return responseCall;
    }

}
