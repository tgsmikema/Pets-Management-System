package com.example.mobile.screen.dog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentEditDogBinding;

public class EditDog extends Fragment {

    private EditText editName;
    private EditText editBreed;
    private Spinner locationSpinner;
    private Button deleteButton;
    private Button saveButton;
    private FragmentEditDogBinding binding;

    public EditDog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditDogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        String dogName = bundle.getString("name");
        String dogBreed = bundle.getString("breed");

        editName = root.findViewById(R.id.edit_dog_text_edit_username);
        editName.setText(dogName);
        editBreed = root.findViewById(R.id.edit_dog_text_edit_breed);
        editBreed.setText(dogBreed);
        locationSpinner = root.findViewById(R.id.edit_dog_location_spinner);
        deleteButton = root.findViewById(R.id.edit_dog_delete);
        saveButton = root.findViewById(R.id.edit_dog_save);

        //drop down menu to select location
        locationSpinner = root.findViewById(R.id.edit_dog_location_spinner);
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

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle delete button click
                //TODO: delete user in db
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
