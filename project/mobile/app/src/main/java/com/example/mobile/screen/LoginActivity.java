package com.example.mobile.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.Toast;

import com.example.mobile.databinding.ActivityLoginBinding;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Button loginButton;

    private TextInputEditText editTextUserName;
    private TextInputEditText editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initialView();

    }

    public void initialView(){
        editTextPassword = binding.editTextLoginPassword;
        editTextUserName = binding.editTextLoginUsername;
        loginButton = binding.buttonLogin;

        loginButton.setOnClickListener((event)->{
             onLoginClick();
        });
    }

    public void onLoginClick(){
         String username = editTextUserName.getEditableText().toString();
         String password = editTextPassword.getEditableText().toString();
         if(username.equals("")){
             Toast.makeText(getApplicationContext(),"Please enter your username",Toast.LENGTH_SHORT).show();
         } else if(password.equals("")){
             Toast.makeText(getApplicationContext(),"Please enter your password",Toast.LENGTH_SHORT).show();
         }else{
//             OkHttpClient client = new OkHttpClient();

             // Create a new OkHttpClient with an interceptor
             OkHttpClient client = new OkHttpClient.Builder()
                     .addInterceptor(chain -> {
                         // Encode the username and password as a Base64 string
                         String credentials = username + ":" + password;
                         String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                         // Add the Authorization header to the request
                         Request request = chain.request().newBuilder()
                                 .addHeader("Authorization", basic)
                                 .build();

                         // Proceed with the request
                         return chain.proceed(request);
                     })
                     .build();

// Create a new request and execute it
             Request request = new Request.Builder()
                     .url("http://electronicnz-001-site1.ftempurl.com/user/login")
                     .get()
                     .build();

//             String auth = userName+":"+password;
//             String encodedAuth = "Basic " + Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
//             Request request = new Request.Builder()
//                     .url("http://electronicnz-001-site1.ftempurl.com/user/login")
//                     .addHeader("Authorization", encodedAuth)
//                     .build();

             client.newCall(request).enqueue(new Callback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
//                      Toast.makeText(getApplicationContext(),"The username or password is incorrect",Toast.LENGTH_SHORT).show();
                     System.out.println(e);
                 }

                 @Override
                 public void onResponse(Call call, Response response) throws IOException {
                     if(response.isSuccessful()){
                         System.out.println(response.body().source());
                         goToMainActivity();
                     }else{
                         System.out.println("username or password is incorrect");
                     }
                 }
             });
         }
//        MaterialDialogs dialogs = new MaterialDialog
    }


    public void goToMainActivity(){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setClass(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}