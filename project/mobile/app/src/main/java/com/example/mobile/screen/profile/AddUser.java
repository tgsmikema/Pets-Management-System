package com.example.mobile.screen.profile;
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
import com.example.mobile.databinding.FragmentAddUserBinding;

public class AddUser extends Fragment {

    private EditText editTextName;
    private EditText editTextJob;
    private Spinner accessSpinner;
    private Button cancelButton;
    private Button createButton;
    private FragmentAddUserBinding binding;

    public AddUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //drop down menu to select access level
        accessSpinner = root.findViewById(R.id.access_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.access_level_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accessSpinner.setAdapter(adapter);

        editTextName = root.findViewById(R.id.text_edit_username);
        editTextJob = root.findViewById(R.id.text_edit_job);
        accessSpinner = root.findViewById(R.id.access_spinner);
        cancelButton = root.findViewById(R.id.cancel);
        createButton = root.findViewById(R.id.create);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle create button click
                //TODO: add User to db
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