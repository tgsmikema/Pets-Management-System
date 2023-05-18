package com.example.mobile.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.ActivityLoginBinding;
import com.example.mobile.model.Centre;
import com.example.mobile.model.Scale;
import com.example.mobile.model.User;
import com.example.mobile.service.SPCAService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Button loginButton;

    private TextInputEditText editTextUserName;
    private TextInputEditText editTextPassword;

    private TextView textViewErrorMessage;

    //used to show the loading dialogs
    private MaterialDialog dialogs;

    private SPCAService spcaService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialView();
        spcaService = new SPCAService();
        dialogs = new MaterialDialog.Builder(this).content("Please wait").progress(true, 0).cancelable(false).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialView();
    }

    public void initialView() {
        editTextPassword = binding.editTextLoginPassword;
        editTextUserName = binding.editTextLoginUsername;
        loginButton = binding.buttonLogin;
        textViewErrorMessage = binding.errorMessage;

        //setOnClickLister for the login button
        loginButton.setOnClickListener((event) -> {
            onLoginClick();
        });
    }

    public void onLoginClick() {
        String username = editTextUserName.getEditableText().toString();
        String password = editTextPassword.getEditableText().toString();
        if (username.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your username", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
        } else {
            userLogin(username,password,false);
        }
    }

    //show the error message, as we will process the UI thread, we will use runOnUiThread method
    public void showErrorMessage(String message) {
        runOnUiThread(() -> textViewErrorMessage.setText(message));
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void userLogin(String username,String password,boolean hasInfo){
        dialogs.show();
        Call<User> call = spcaService.login(username, password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    //set the global current user
                    SPCApplication.currentUser = user;
                    textViewErrorMessage.setText("");
                    editTextUserName.setText("");
                    editTextPassword.setText("");
                    fetchAllScales();
                    fetchAllCentres();
                } else {
                    // Handle error
                    dialogs.dismiss();
                    showErrorMessage("Your username or password is invalid");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle error
                dialogs.dismiss();
                showErrorMessage(t.toString());
            }
        });
    }

    public void fetchAllScales(){
        Call<List<Scale>> allScale = spcaService.getAllScale(SPCApplication.currentUser.getToken());
        allScale.enqueue(new Callback<List<Scale>>() {
            @Override
            public void onResponse(Call<List<Scale>> call, Response<List<Scale>> response) {
                List<Scale> body = response.body();
                if(body != null){
                    SPCApplication.allScales = body;
                }
            }

            @Override
            public void onFailure(Call<List<Scale>> call, Throwable t) {

            }
        });
    }

    public void fetchAllCentres(){
        Call<List<Centre>> listCall = spcaService.fetchAllCentres(SPCApplication.currentUser.getToken());
        listCall.enqueue(new Callback<List<Centre>>() {
            @Override
            public void onResponse(Call<List<Centre>> call, Response<List<Centre>> response) {
                dialogs.dismiss();
                List<Centre> res = response.body();
                if(res != null){
                    res.add(0,new Centre(0,"All Centres"));
                    SPCApplication.allCentres = res;
                }
                goToMainActivity();
            }
            @Override
            public void onFailure(Call<List<Centre>> call, Throwable t) {
                dialogs.dismiss();
            }
        });
    }

}