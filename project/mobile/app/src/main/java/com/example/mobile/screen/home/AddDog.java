package com.example.mobile.screen.home;
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
import com.example.mobile.databinding.FragmentAddDogBinding;
import com.example.mobile.model.DogRegisterRequest;
import com.example.mobile.service.SPCAService;

import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDog extends Fragment {

    private EditText editTextName;
    private EditText editTextBreed;
    private Spinner locationSpinner;
    private Button cancelButton;
    private Button createButton;

    private TextView errorMessage;

    private int centreId;
    private FragmentAddDogBinding binding;

    private SPCAService spcaService;
    public AddDog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddDogBinding.inflate(inflater, container, false);
        centreId = 1;
        spcaService = new SPCAService();
        initView();
        return binding.getRoot();
    }

    private void initView(){

        errorMessage = binding.newDogErrorMessage;
        //drop down menu to select centers
        locationSpinner = binding.locationSpinner;
        List<String> collect = SPCApplication.allCentres.stream().map(it -> it.getName()).collect(Collectors.toList());
        collect.remove(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, collect);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 centreId = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editTextName = binding.textEditName;
        editTextBreed = binding.textEditBreed;
        cancelButton = binding.cancel;
        createButton = binding.create;

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHomeFragment();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle create button click
                //TODO: add dog to db
                if(editTextName.getEditableText().toString().trim().length() ==0 || editTextBreed.getEditableText().toString().trim().length() == 0){
                    errorMessage.setText("Please fill all information");
                    return;
                }

                addNewDog(new DogRegisterRequest(editTextName.getEditableText().toString().trim(),
                        editTextBreed.getEditableText().toString().trim(),
                        centreId));
            }
        });
    }

    private void addNewDog(DogRegisterRequest dogRegisterRequest){
        Call<ResponseBody> responseBodyCall = spcaService.addNewDog(SPCApplication.currentUser.getToken(), dogRegisterRequest);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    goToHomeFragment();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void goToHomeFragment(){
        HomeFragment homeFragment = new HomeFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
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