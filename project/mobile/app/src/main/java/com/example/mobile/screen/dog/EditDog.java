package com.example.mobile.screen.dog;

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
import com.example.mobile.databinding.FragmentEditDogBinding;
import com.example.mobile.model.DogEditRequest;
import com.example.mobile.screen.home.HomeFragment;
import com.example.mobile.service.SPCAService;

import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDog extends Fragment {
    private EditText editName;
    private EditText editBreed;
    private Spinner locationSpinner;
    private Button deleteButton;
    private Button saveButton;
    private FragmentEditDogBinding binding;

    private SPCAService spcaService;
    private int centreId;
    private TextView errorMessage;

    int id;
    String dogName ;
    String dogBreed;
    String dogLocation;

    public EditDog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditDogBinding.inflate(inflater, container, false);
        initView();
        return binding.getRoot();
    }

    public void initView(){

        spcaService = new SPCAService();

        errorMessage = binding.editDogErrorMessage;

        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        dogName = bundle.getString("name");
        dogBreed = bundle.getString("breed");
        dogLocation = bundle.getString("location");

        editName = binding.editDogTextEditUsername;
        editName.setText(dogName);
        editBreed = binding.editDogTextEditBreed;
        editBreed.setText(dogBreed);
        locationSpinner = binding.editDogLocationSpinner;
        deleteButton = binding.editDogDelete;
        saveButton = binding.editDogSave;

        //drop down menu to select location
        List<String> collect = SPCApplication.allCentres.stream().map(it -> it.getName()).collect(Collectors.toList());
        collect.remove(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, collect);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for(int i = 0;i<collect.size();i++){
            if(collect.get(i).toLowerCase().trim().equals(dogLocation.trim().toLowerCase())){
                centreId = i + 1;
                break;
            }
        }

        locationSpinner.setAdapter(adapter);
        locationSpinner.setSelection(centreId - 1);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                centreId = i + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle save button click
                //TODO: update user in db
                String name = editName.getEditableText().toString().trim();
                String breed = editBreed.getEditableText().toString().trim();
                String location = SPCApplication.allCentres.get(centreId - 1).getName();

                if(name.length() == 0 || breed.length() == 0 || location.length() == 0) {
                    errorMessage.setText("Please fill all information");
                    return;
                }

                dogName = name;
                dogBreed = breed;
                dogLocation = location;

                System.out.println(dogLocation);

                Call<ResponseBody> responseBodyCall = spcaService.editDog(SPCApplication.currentUser.getToken(), new DogEditRequest(id, centreId, name, breed));
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                          if(response.isSuccessful()){
                              goToDogFragment();
                          }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle delete button click
                //TODO: delete user in db
                if(SPCApplication.currentUser.getUserType().equals("volunteer")){
                    errorMessage.setText("You can not delete a dog");
                    return;
                }
                Call<ResponseBody> responseBodyCall = spcaService.deleteDog(SPCApplication.currentUser.getToken(), id);

                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                         goToHomeFragment();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void goToHomeFragment(){
        HomeFragment homeFragment = new HomeFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
    }

    public void goToDogFragment(){
        DogFragment dogFragment = new DogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        dogFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,dogFragment).commit();
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
