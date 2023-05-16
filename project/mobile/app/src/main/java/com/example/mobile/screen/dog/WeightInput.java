package com.example.mobile.screen.dog;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentWeightInputBinding;
import com.example.mobile.model.DogWeight;
import com.example.mobile.service.SPCAService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeightInput extends Fragment {
    private TextView subtitle;
    private Button cancelButton;
    private Button saveButton;
    private FragmentWeightInputBinding binding;

    private TextView textWeight;
    private TextView textWaitingMessage;
    int dogId;
    String dogName;
    int scaleId;
    private SPCAService spcaService;

    double dogWeight;

    Handler handler;

    Runnable runnable;

    public WeightInput() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWeightInputBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        dogId = bundle.getInt("id");
        dogName = bundle.getString("dogName");
        scaleId = bundle.getInt("scaleId");

        dogWeight = 0.0;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                fetchCurrentDogWeight();
                handler.postDelayed(this,2000);
            }
        };
        handler.postDelayed(runnable, 2000);

        subtitle = root.findViewById(R.id.weight_input_subtitle);
        subtitle.setText("Place " + dogName + " on " + SPCApplication.allScales.get(scaleId - 1).getName());
        cancelButton = root.findViewById(R.id.weight_input_cancel);
        saveButton = root.findViewById(R.id.weight_input_save);
        textWeight = binding.weightInputWeight;
        textWaitingMessage = binding.weightWaitingMessage;

        spcaService = new SPCAService();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(dogWeight - 0.0 < 0.001){
                    return;
                }
                // Handle save button click
                Call<ResponseBody> saveWeight = spcaService.getSaveWeight(SPCApplication.currentUser.getToken(), dogId);
                saveWeight.enqueue(new Callback<ResponseBody>() {
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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDogFragment();
            }
        });

        return root;
    }

    public void fetchCurrentDogWeight(){
        Call<DogWeight> currentDogWeight = spcaService.getCurrentDogWeight(SPCApplication.currentUser.getToken(), dogId);
        currentDogWeight.enqueue(new Callback<DogWeight>() {
            @Override
            public void onResponse(Call<DogWeight> call, Response<DogWeight> response) {
                if(response.isSuccessful()){
                    DogWeight body = response.body();
                    dogWeight = body.getWeight();
                    if(dogWeight - 0.0 > 0.1){
                        setWeight();
                        handler.removeCallbacks(runnable);
                    }
                }
            }

            @Override
            public void onFailure(Call<DogWeight> call, Throwable t) {

            }
        });
    }

    public void setWeight(){
        getActivity().runOnUiThread(() -> {
            textWaitingMessage.setText("");
            textWeight.setText(String.valueOf(dogWeight));
        });
    }

    public void goToDogFragment(){
        DogFragment dogFragment = new DogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", dogId);
        dogFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,dogFragment).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        handler.removeCallbacks(runnable);
    }
}
