package com.example.mobile.screen.dog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentWeightBinding;
import com.example.mobile.model.InvokeRequest;
import com.example.mobile.service.SPCAService;

import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Weight extends Fragment {

    private TextView subtitle;
    private Spinner locationSpinner;
    private Button cancelButton;
    private Button startButton;
    private FragmentWeightBinding binding;
    int dogId;
    String dogName;
    int scaleId;
    SPCAService spcaService;
    public Weight() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWeightBinding.inflate(inflater, container, false);
        spcaService = new SPCAService();
        initView();
        return binding.getRoot();
    }

    public void initView(){

        scaleId = 1;
        Bundle bundle = getArguments();
        dogId = bundle.getInt("id");
        dogName = bundle.getString("name");
        locationSpinner = binding.weightLocationSpinner;
        cancelButton = binding.weightCancel;
        startButton = binding.weightStart;

        //drop down menu to select location
        List<String> collect = SPCApplication.allScales.stream().map(it -> it.getName()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, collect);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item from the spinner
                scaleId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inovkeRequest();
                WeightInput weightInput = new WeightInput();
                Bundle bundle = new Bundle();
                bundle.putInt("id", dogId);
                bundle.putInt("scaleId", scaleId);
                bundle.putString("dogName",dogName);
                weightInput.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,weightInput).commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DogFragment dogFragment = new DogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", dogId);
                dogFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,dogFragment).commit();
            }
        });
    }

    public void inovkeRequest(){
        Call<ResponseBody> responseBodyCall = spcaService.invokeRequest(SPCApplication.currentUser.getToken(), new InvokeRequest(dogId, scaleId));
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
