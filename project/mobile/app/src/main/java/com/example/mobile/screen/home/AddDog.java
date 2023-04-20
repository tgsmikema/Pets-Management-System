package com.example.mobile.screen.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.adaptor.HomeAdaptor;
import com.example.mobile.databinding.FragmentAddDogBinding;
import com.example.mobile.databinding.FragmentHomeBinding;

public class AddDog extends Fragment {

    private EditText editTextName;
    private EditText editTextBreed;
    private Spinner locationSpinner;
    private Button cancelButton;
    private Button createButton;
    private FragmentAddDogBinding binding;
    private Spinner locations;

    public AddDog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddDogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //drop down menu to select centers
        //TODO: replace centers_list mock data with db
        locations = root.findViewById(R.id.location_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.centers_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locations.setAdapter(adapter);

        editTextName = root.findViewById(R.id.text_edit_name);
        editTextBreed = root.findViewById(R.id.text_edit_breed);
        locationSpinner = root.findViewById(R.id.location_spinner);
        cancelButton = root.findViewById(R.id.cancel);
        createButton = root.findViewById(R.id.create);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle create button click
                //TODO: add dog to db
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}