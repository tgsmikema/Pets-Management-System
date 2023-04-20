package com.example.mobile.screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.ActivityLoginBinding;
import com.example.mobile.model.User;
import com.example.mobile.service.SPCAService;
import com.google.android.material.textfield.TextInputEditText;

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
            dialogs.show();
            Call<User> call = spcaService.login(username, password);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    dialogs.dismiss();
                    if (response.isSuccessful()) {
                        User user = response.body();
                        //set the global current user
                        SPCApplication.currentUser = user;
                        textViewErrorMessage.setText("");
                        goToMainActivity();
                    } else {
                        // Handle error
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
    }

    //show the error message, as we will process the UI thread, we will use runOnUiThread method
    public void showErrorMessage(String message) {
        runOnUiThread(() -> textViewErrorMessage.setText(message));
    }

    public void goToMainActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}