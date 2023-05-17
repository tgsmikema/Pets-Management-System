package com.example.mobile.service;

import com.example.mobile.SPCApplication;
import com.example.mobile.model.Centre;
import com.example.mobile.model.ChangePasswordRequest;
import com.example.mobile.model.Dog;
import com.example.mobile.model.DogDetail;
import com.example.mobile.model.DogEditRequest;
import com.example.mobile.model.DogRegisterRequest;
import com.example.mobile.model.DogWeight;
import com.example.mobile.model.DogWeightHistory;
import com.example.mobile.model.EditUserRequest;
import com.example.mobile.model.InvokeRequest;
import com.example.mobile.model.Message;
import com.example.mobile.model.MessageRequest;
import com.example.mobile.model.Scale;
import com.example.mobile.model.UserRegisterRequest;
import com.example.mobile.model.TimeWeightRequest;
import com.example.mobile.model.User;
import com.example.mobile.model.WeightData;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;


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

    public Call<List<User>> getAllUsers(String token){
         initRetrofit(token);
         UtilService utilService = retrofit.create(UtilService.class);
         Call<List<User>> call = utilService.getAllUsers();
         return call;
    }

    public Call<ResponseBody> register(String token, UserRegisterRequest registerRequest){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<ResponseBody> responseBodyCall = utilService.register(registerRequest);
        return responseBodyCall;
    }

    public Call<ResponseBody> editUser(String token, EditUserRequest editUserRequest){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<ResponseBody> responseBodyCall = utilService.editUser(editUserRequest);
        return responseBodyCall;
    }

    public Call<ResponseBody> deletedUser(String token,int userId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<ResponseBody> responseBodyCall = utilService.deleteUser(userId);
        return responseBodyCall;
    }

    public Call<ResponseBody> changePassword(String token, ChangePasswordRequest changePasswordRequest){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        Call<ResponseBody> responseBodyCall = utilService.changePassword(changePasswordRequest);
        return responseBodyCall;
    }

    public Call<List<Dog>> getAllDogFromOneCentre(String token, int centreId){
         initRetrofit(token);
         UtilService utilService = retrofit.create(UtilService.class);
         if(centreId == 0){
             return utilService.getAllDogFromAllCentres();
         }else{
             return utilService.getAllDogFromOneCentre(centreId);
         }
    }

    public Call<List<Dog>> getAllDogFromNonAdminUser(String token){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.getAllDogForNonAdminUser();
    }

    public Call<ResponseBody> addNewDog(String token, DogRegisterRequest dogRegisterRequest){
         initRetrofit(token);
         UtilService utilService = retrofit.create(UtilService.class);
         return utilService.addNewDog(dogRegisterRequest);
    }

    public Call<ResponseBody> toggleFlagged(String token, int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.toggleFlag(dogId);
    }

    public Call<ResponseBody> toggleAlert(String token,int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.toggleAlert(dogId);
    }

    public Call<List<DogWeightHistory>> getWeightHistory(String token, int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.getWeightHistory(dogId);
    }

    public Call<ResponseBody> deleteDog(String token, int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.deleteDog(dogId);
    }

    public Call<ResponseBody> editDog(String token, DogEditRequest dogEditRequest){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.editDog(dogEditRequest);
    }

    public Call<DogDetail> getDogDetailFromAllCentre(String token,int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.getDogDetailFromAllCentre(dogId);
    }

    public Call<DogDetail> getDogDetailFromOwnCentre(String token, int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.getDogDetailFromOwnCentre(dogId);
    }

    public Call<List<Scale>> getAllScale(String token){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.getScaleList();
    }

    public Call<ResponseBody> getSaveWeight(String token,int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.saveCurrentWeight(dogId);
    }

    public Call<ResponseBody> invokeRequest(String token, InvokeRequest invokeRequest){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.invokeScaleRequest(invokeRequest);
    }

    public Call<DogWeight> getCurrentDogWeight(String token, int dogId){
        initRetrofit(token);
        UtilService utilService = retrofit.create(UtilService.class);
        return utilService.getCurrentDogWeight(dogId);
    }
}
