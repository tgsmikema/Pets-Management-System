package com.example.mobile.screen.profile;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentEditUserBinding;
import com.example.mobile.model.EditUserRequest;
import com.example.mobile.service.SPCAService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUser extends Fragment {

    private EditText editTextName;

    private TextView errorMessage;
    private Spinner accessSpinner;
    private Button deleteButton;
    private Button saveButton;
    private FragmentEditUserBinding binding;

    private String spinnerValue;

    private SPCAService spcaService;

    public EditUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        spcaService = new SPCAService();
        initView();
        return binding.getRoot();
    }

    public void initView(){
        Bundle bundle = getArguments();
        String userName = bundle.getString("name");
        String userType = bundle.getString("userType");

        spinnerValue = userType;

        editTextName = binding.editUserTextEditUsername;
        errorMessage = binding.editUserErrorMessage;
        editTextName.setText(userName);
        accessSpinner = binding.editUserAccessSpinner;
        deleteButton = binding.editUserDelete;
        saveButton = binding.editUserSave;

        //drop down menu to select access level
        List<String> allTypes = new ArrayList<String>(Arrays.asList("admin","vet","volunteer"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, allTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accessSpinner.setAdapter(adapter);

        int initialSelection = -1;
        for(int i = 0;i<allTypes.size();i++){
            if(allTypes.get(i).equals(userType)){
                initialSelection = i;
                break;
            }
        }
        accessSpinner.setSelection(initialSelection);

        accessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle save button click
                //TODO: update user in db
                Bundle bundle = getArguments();
                int id = bundle.getInt("id");
                saveEditUser(new EditUserRequest(id,spinnerValue));
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle delete button click
                //TODO: delete user in db
                Bundle bundle = getArguments();
                int id = bundle.getInt("id");
                if(id == SPCApplication.currentUser.getId()){
                    errorMessage.setText("You can not delete yourself");
                    return;
                }
                if(userType.equals("admin")){
                    errorMessage.setText("You can not delete admin");
                    return;
                }
                deleteUser();
            }
        });
    }


    public void saveEditUser(EditUserRequest editUserRequest){
        Call<ResponseBody> responseBodyCall = spcaService.editUser(SPCApplication.currentUser.getToken(), editUserRequest);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    goToProfileFragment();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void deleteUser(){
        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        Call<ResponseBody> responseBodyCall = spcaService.deletedUser(SPCApplication.currentUser.getToken(), id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                 if(response.isSuccessful()){
                     goToProfileFragment();
                 }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
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