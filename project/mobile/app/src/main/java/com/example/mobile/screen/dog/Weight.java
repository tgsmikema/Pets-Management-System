package com.example.mobile.screen.dog;

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
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentEditDogBinding;
import com.example.mobile.databinding.FragmentWeightBinding;
import com.example.mobile.model.Dog;
import com.example.mobile.screen.profile.ProfileFragment;

public class Weight extends Fragment {

    private TextView subtitle;
    private Spinner locationSpinner;
    private Button cancelButton;
    private Button saveButton;
    private FragmentWeightBinding binding;

    public Weight() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentWeightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        String dogId = bundle.getString("id");
        String dogName = bundle.getString("name");
        String dogBreed = bundle.getString("breed");
        String dogLocation = bundle.getString("location");
        String dogWeight = bundle.getString("weight");
        String dogDate = bundle.getString("date");
        boolean dogAlert = bundle.getBoolean("alert");
        boolean dogFlagged = bundle.getBoolean("flagged");

        subtitle = root.findViewById(R.id.weight_subtitle);
        subtitle.setText("Place " + dogName + " on the scales");
        locationSpinner = root.findViewById(R.id.weight_location_spinner);
        cancelButton = root.findViewById(R.id.weight_cancel);
        saveButton = root.findViewById(R.id.weight_save);

        //drop down menu to select location
        locationSpinner = root.findViewById(R.id.weight_location_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.centers_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle save button click
                //TODO: update user in db
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DogFragment dogFragment = new DogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", dogName);
                bundle.putString("breed", dogBreed);
                bundle.putString("id", dogId);
                bundle.putString("location", dogLocation);
                bundle.putString("weight", dogWeight);
                bundle.putString("date", dogDate);
                bundle.putBoolean("flag", dogFlagged);
                bundle.putBoolean("alert", dogAlert);
                dogFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,dogFragment).commit();
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
