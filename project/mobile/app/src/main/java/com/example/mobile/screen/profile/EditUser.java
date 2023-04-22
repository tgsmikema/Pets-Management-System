package com.example.mobile.screen.profile;
import android.content.Intent;
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
import com.example.mobile.databinding.FragmentEditUserBinding;

public class EditUser extends Fragment {

    private EditText editTextName;
    private EditText editTextJob;
    private Spinner accessSpinner;
    private Button deleteButton;
    private Button saveButton;
    private FragmentEditUserBinding binding;

    public EditUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        String userName = bundle.getString("name");
        String userJob = bundle.getString("job");

        editTextName = root.findViewById(R.id.edit_user_text_edit_username);
        editTextName.setText(userName);
        editTextJob = root.findViewById(R.id.edit_user_text_edit_job);
        editTextJob.setText(userJob);
        accessSpinner = root.findViewById(R.id.edit_user_access_spinner);
        deleteButton = root.findViewById(R.id.edit_user_delete);
        saveButton = root.findViewById(R.id.edit_user_save);

        //drop down menu to select access level
        accessSpinner = root.findViewById(R.id.edit_user_access_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.access_level_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accessSpinner.setAdapter(adapter);

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