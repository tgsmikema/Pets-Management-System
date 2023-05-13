package com.example.mobile.screen.profile;
import android.os.Bundle;
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
import com.example.mobile.databinding.FragmentAddUserBinding;
import com.example.mobile.model.UserRegisterRequest;
import com.example.mobile.service.SPCAService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUser extends Fragment {

    private EditText editTextUserName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;

    private TextView errorMessage;

    private Spinner centreSpinner;
    private Spinner accessSpinner;
    private Button cancelButton;
    private Button createButton;
    private FragmentAddUserBinding binding;

    private int centreId;

    private String accessType;

    private SPCAService spcaService;

    public AddUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        spcaService = new SPCAService();
        initView();
        return binding.getRoot();
    }

    public void initView(){
        //drop down menu to select access level
        accessSpinner = binding.accessSpinner;
        //drop down menu to select access level
        List<String> allTypes = new ArrayList<String>(Arrays.asList("admin","vet","volunteer"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, allTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accessSpinner.setAdapter(adapter);
        accessSpinner.setSelection(0);
        accessType = allTypes.get(0);

        accessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                accessType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        centreSpinner = binding.centreIdSpinner;
        List<String> allCentres = SPCApplication.allCentres.stream().map(it -> it.getName()).collect(Collectors.toList());
        allCentres.remove(0);
        ArrayAdapter<String> adapterForCentre = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, allCentres);
        adapterForCentre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        centreSpinner.setAdapter(adapterForCentre);
        centreSpinner.setSelection(0);
        centreId = 1;

        centreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                centreId = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editTextEmail = binding.textEditEmail;
        editTextUserName = binding.textEditUsername;
        editTextLastName = binding.textEditLastName;
        editTextFirstName = binding.textEditFirstName;
        editTextPassword = binding.textEditPassword;

        errorMessage = binding.newUserErrorMessage;

        cancelButton = binding.cancel;
        createButton = binding.create;

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToProfileFragment();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle create button click
                //TODO: add User to db
                if(!checkFillAllInformation()){
                    errorMessage.setText("Please fill all information");
                    return;
                }
                createNewUser(new UserRegisterRequest(
                        editTextUserName.getEditableText().toString(),
                        editTextEmail.getEditableText().toString(),
                        editTextPassword.getEditableText().toString(),
                        accessType,
                        editTextFirstName.getEditableText().toString(),
                        editTextLastName.getEditableText().toString(),
                        centreId
                ));
            }
        });
    }

    private boolean checkFillAllInformation(){
        return editTextUserName.getEditableText().toString().trim().length() != 0 &&
                editTextEmail.getEditableText().toString().trim().length() != 0 &&
                editTextPassword.getEditableText().toString().trim().length() != 0 &&
                editTextFirstName.getEditableText().toString().trim().length() != 0 &&
                editTextLastName.getEditableText().toString().trim().length() != 0;
    }

    private void createNewUser(UserRegisterRequest registerRequest){
        Call<ResponseBody> register = spcaService.register(SPCApplication.currentUser.getToken(), registerRequest);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    goToProfileFragment();
                }else{
                    setErrorMessage();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                setErrorMessage();
            }
        });
    }

    private void setErrorMessage(){
        getActivity().runOnUiThread(()->{
            errorMessage.setText("The username have already exists");
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