package com.example.mobile.screen.profile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentProfileBinding;
import com.example.mobile.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements ProfileAdaptor.ItemClickListener{

    private FragmentProfileBinding binding;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageButton plusButton;
    private Button changePasswordButton;
    private Button logoutButton;
    private TextView name, job, userType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        name = root.findViewById(R.id.profile_name);
        job = root.findViewById(R.id.profile_job);
        userType = root.findViewById(R.id.profile_roles);
        //TODO: update name, job, userType with db

        //search bar
        searchView = root.findViewById(R.id.search_profile);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Perform search operation with query string
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Update search results based on new text
                return false;
            }
        });

        //recycler view
        recyclerView = root.findViewById(R.id.recycler_view_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: replace mock data with db
        List<User> users = new ArrayList<>();
        users.add(new User("John Doe", "Electrical Engineer at UOA", "Admin"));
        users.add(new User("Jane Doe", "Software Engineer at UOA", "Volunteer"));
        users.add(new User("Anna Verdi", "Vet at Manukau", "Vet"));
        users.add(new User("Nomen Nescio", "Electrical Engineer at UOA", "Volunteer"));
        users.add(new User("Fulano de Tal", "Manager", "Admin"));

        ProfileAdaptor listAdaptor = new ProfileAdaptor(users, this);
        recyclerView.setAdapter(listAdaptor);

        //add button
        plusButton = root.findViewById(R.id.plus_button_profile);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser addUser = new AddUser();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,addUser).commit();
            }
        });

        //change password button
        changePasswordButton = root.findViewById(R.id.password);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: go to change password screen
            }
        });

        //logout button
        logoutButton = root.findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: logout
            }
        });

        return root;
    }

    public void onItemClick(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("name", user.getUserName());
        bundle.putString("job", user.getJob());
        bundle.putString("userType", user.getUserType());
        bundle.putInt("id", user.getId());
        EditUser editUser = new EditUser();
        editUser.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, editUser);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}