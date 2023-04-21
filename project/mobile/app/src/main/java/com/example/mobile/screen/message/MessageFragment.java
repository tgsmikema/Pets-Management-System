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
import com.example.mobile.databinding.FragmentHomeBinding;
import com.example.mobile.databinding.FragmentMessageBinding;
import com.example.mobile.model.User;
import com.example.mobile.screen.home.HomeFragment;
import com.example.mobile.screen.message.ChatPage.ChatFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageFragment extends Fragment implements OnItemClickListener {

    private FragmentMessageBinding binding;
    private Button buttonNewMessage;
    private RecyclerView chatUserRecyclerView;

    private ChatUserAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessageBinding.inflate(inflater, container, false);
        initialView();
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

    public void initialView() {

        List<User> userList = new ArrayList<>(Arrays.asList(
                new User("Mike Ma", "admin"),
                new User("Anna Verdi", "vet"),
                new User("John Doe", "volunteer"),
                new User("Fulano de Tal", "admin"),
                new User("Qingyang Li", "vet"),
                new User("Zhang San", "volunteer"),
                new User("Nomen Nescio", "vet"),
                new User("Hanako Yamada", "admin"),
                new User("Jake Ma", "admin")
        ));
        buttonNewMessage = binding.buttonNewMessage;
        buttonNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnChattedUserListDialog();
            }
        });
        chatUserRecyclerView = binding.chatUserRecyclerview;
        adapter = new ChatUserAdapter(userList);
        adapter.setOnItemClickListener(this);
        chatUserRecyclerView.setAdapter(adapter);
        chatUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(User user) {
        Bundle bundle = new Bundle();
        bundle.putString("name", user.getUserName());
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
        List<User> userList = new ArrayList<>(Arrays.asList(
                SPCApplication.currentUser,
                new User("Qingyang", "admin"),
                new User("Sam", "Vet"),
                new User("Frank", "volunteer"),
                new User("Amy", "volunteer"),
                new User("harry", "vet")
        ));


        new MaterialDialog.Builder(getContext())
                // Set the title and message for the dialog
                .title("select a user to start new message")
                // Set the items for the dialog using the userList data
                .items(userList.stream().map(it -> it.getUserName()).collect(Collectors.toList()))
                // Set a positive button to confirm the user selection
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        // Get the selected user
                        User selectedUser = userList.get(position);
                        // Navigate to the new fragment with the selected user information
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        ChatFragment chatFragment = new ChatFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("name", selectedUser.getUserName());
                        bundle.putString("type", selectedUser.getUserType());
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