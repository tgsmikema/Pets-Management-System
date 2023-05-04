package com.example.mobile.screen.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentMessageBinding;
import com.example.mobile.model.User;
import com.example.mobile.screen.message.ChatPage.ChatFragment;
import com.example.mobile.service.SPCAService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageFragment extends Fragment implements OnItemClickListener {

    private FragmentMessageBinding binding;
    private Button buttonNewMessage;
    private RecyclerView chatUserRecyclerView;

    private ChatUserAdapter adapter;

    private SPCAService spcaService;

    private List<User> chattedUserList;

    private List<User> unChattedUserList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessageBinding.inflate(inflater, container, false);
        initialView();
        spcaService = new SPCAService();
        chattedUserList = new ArrayList<>();
        unChattedUserList = new ArrayList<>();
        fetchAllChattedUsers();
        fetchAllUnChattedUsers();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void fetchAllChattedUsers(){
        Call<List<User>> listCall = spcaService.fetchAllChattedUsers(SPCApplication.currentUser.getToken(), SPCApplication.currentUser.getId());
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> body = response.body();
                chattedUserList = body;
                initRecycleViewAdapter();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    public void fetchAllUnChattedUsers(){
        Call<List<User>> listCall = spcaService.fetchAllUnChattedUsers(SPCApplication.currentUser.getToken(), SPCApplication.currentUser.getId());
        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> body = response.body();
                unChattedUserList = body;
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
    public void initialView() {

        buttonNewMessage = binding.buttonNewMessage;
        buttonNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnChattedUserListDialog();
            }
        });
        chatUserRecyclerView = binding.chatUserRecyclerview;
        initRecycleViewAdapter();
    }

    public void initRecycleViewAdapter(){
        adapter = new ChatUserAdapter(chattedUserList);
        adapter.setOnItemClickListener(this);
        chatUserRecyclerView.setAdapter(adapter);
        chatUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("name", user.getFirstName() + " " + user.getLastName());
        bundle.putString("type", user.getUserType());
        bundle.putInt("id", user.getId());
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showUnChattedUserListDialog() {
        new MaterialDialog.Builder(getContext())
                // Set the title and message for the dialog
                .title("select a user to start new message")
                // Set the items for the dialog using the userList data
                .items(unChattedUserList.stream().map(it -> it.getFirstName() + " " + it.getLastName() + " (" + it.getUserType().trim() + ")").collect(Collectors.toList()))
                // Set a positive button to confirm the user selection
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        // Get the selected user
                        User selectedUser = unChattedUserList.get(position);
                        // Navigate to the new fragment with the selected user information
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        ChatFragment chatFragment = new ChatFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", selectedUser.getFirstName() + " " + selectedUser.getLastName());
                        bundle.putString("type", selectedUser.getUserType());
                        bundle.putInt("id",selectedUser.getId());
                        chatFragment.setArguments(bundle);
                        transaction.replace(R.id.container, chatFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                })
                // Set a negative button to cancel the dialog
                .negativeText(R.string.cancel)
                .show();
    }
}