package com.example.mobile.screen.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentProfileBinding;
import com.example.mobile.model.User;
import com.example.mobile.screen.LoginActivity;
import com.example.mobile.service.SPCAService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements ProfileAdaptor.ItemClickListener {

    private FragmentProfileBinding binding;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageButton plusButton;
    private Button changePasswordButton;
    private Button logoutButton;
    private TextView name, userType;

    private List<User> users;

    private ProfileAdaptor listAdaptor;

    private ConstraintLayout adminSection;

    private SPCAService spcaService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);

        //TODO: replace mock data with db
        spcaService = new SPCAService();
        users = new ArrayList<>();

        initView();
        return binding.getRoot();
    }

    private void initView() {
        name = binding.profileName;
        userType = binding.profileRoles;

        String username = SPCApplication.currentUser.getFirstName() + " " + SPCApplication.currentUser.getLastName();

        name.setText(username);
        userType.setText(SPCApplication.currentUser.getUserType());


        //change password button
        changePasswordButton = binding.password;
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword changePassword = new ChangePassword();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, changePassword).commit();
            }
        });


        //logout button
        logoutButton = binding.logout;
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPCApplication.currentUser = null;
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        //admin section
        adminSection = binding.adminSection;

        if(!SPCApplication.currentUser.getUserType().equals("admin")){
            adminSection.removeAllViews();
            TextView textView = new TextView(getActivity());
            textView.setText("You are not allow to view other users information, ask for permission from admin");
            textView.setTextColor(Color.RED);
            textView.setTextSize(20);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );

            textView.setLayoutParams(layoutParams);

            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.horizontalBias = 0.5f;
            layoutParams.verticalBias = 0.5f;

            adminSection.addView(textView);
            return;
        }

        //search bar
        searchView = binding.searchProfile;

        searchView.setQueryHint("Search...");
        searchView.setQuery("", false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Perform search operation with query string
                List<User> tempUsers = users.stream().filter(it -> {
                    return it.getLastName().contains(query)
                            || it.getFirstName().contains(query)
                            || it.getUserType().contains(query)
                            || it.getEmail().contains(query);
                }).collect(Collectors.toList());
                initAdaptor(tempUsers);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Update search results based on new text
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false); // clear text
                initAdaptor(users);
                return false;
            }
        });


        //recycler view
        recyclerView = binding.recyclerViewProfile;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initial adaptor for recycler view
        initAdaptor(users);

        //add button
        plusButton = binding.plusButtonProfile;
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUser addUser = new AddUser();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, addUser).commit();
            }
        });
        fetchAllUserData();
    }

    public void initAdaptor(List<User> users) {
        listAdaptor = new ProfileAdaptor(users, this);
        recyclerView.setAdapter(listAdaptor);
    }

    public void fetchAllUserData() {
        Call<List<User>> allUsers = spcaService.getAllUsers(SPCApplication.currentUser.getToken());
        allUsers.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> body = response.body();
                users = body;
                initAdaptor(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    public void onItemClick(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("name", user.getFirstName() + " " +user.getLastName());
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
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}