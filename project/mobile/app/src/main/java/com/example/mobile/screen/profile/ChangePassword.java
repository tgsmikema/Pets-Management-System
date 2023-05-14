package com.example.mobile.screen.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentChangePasswordBinding;
import com.example.mobile.model.ChangePasswordRequest;
import com.example.mobile.service.SPCAService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends Fragment {

    private FragmentChangePasswordBinding binding;

    private EditText oldPassword,newPassword,confirmedPassword;

    private TextView errorMessage;

    private Button cancel, save;

    private SPCAService spcaService;

    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        spcaService = new SPCAService();
        initView();
        return binding.getRoot();
    }

    public void initView(){
        oldPassword = binding.textEditOldPassword;
        newPassword = binding.textEditNewPassword;
        confirmedPassword = binding.textEditConfirmedPassword;
        errorMessage = binding.changePasswordErrorMessage;
        save = binding.changePasswordBtn;
        cancel = binding.cancelPasswordBtn;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfileFragment();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkFillAllInfo()){
                    errorMessage.setText("Please fill all information");
                    return;
                }
                if(!checkConsistenceWithNewPasswordAndConfirmed()){
                    errorMessage.setText("The new password is not same as confirmed password");
                    return;
                }

                changePassword(new ChangePasswordRequest(oldPassword.getEditableText().toString().trim(),newPassword.getEditableText().toString().trim()));
            }
        });
    }

    public boolean checkFillAllInfo(){
        return oldPassword.getEditableText().toString().trim().length() != 0 &&
                newPassword.getEditableText().toString().trim().length() != 0 &&
                confirmedPassword.getEditableText().toString().trim().length() != 0;
    }

    public boolean checkConsistenceWithNewPasswordAndConfirmed(){
        return newPassword.getEditableText().toString().trim().equals(confirmedPassword.getEditableText().toString().trim());
    }

    private void changePassword(ChangePasswordRequest changePasswordRequest){
        Call<ResponseBody> responseBodyCall = spcaService.changePassword(SPCApplication.currentUser.getToken(), changePasswordRequest);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 if(response.isSuccessful()){
                     try {
                         String body = response.body().string();
                         SPCApplication.currentUser.setUserToken(body);
                         goToProfileFragment();
                     }catch (Exception e){
                         System.out.println(e);
                     }

                 }else{
                     setErrorMessage();
                 }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setErrorMessage(){
        getActivity().runOnUiThread(()->{
            errorMessage.setText("The Old password is not correct");
        });
    }

    public void goToProfileFragment(){
        ProfileFragment profileFragment = new ProfileFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
