package com.example.mobile.screen.message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.FragmentHomeBinding;
import com.example.mobile.databinding.FragmentMessageBinding;
import com.example.mobile.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageFragment extends Fragment {

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
    public void onResume(){
        super.onResume();
        initialView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initialView(){

        List<User> userList = new ArrayList<>(Arrays.asList(
                new User("Mike Ma","admin"),
                new User("Anna Verdi","vet"),
                new User("John Doe","volunteer"),
                new User("Fulano de Tal","admin"),
                new User("Qingyang Li","vet"),
                new User("Zhang San","volunteer"),
                new User("Nomen Nescio","vet"),
                new User("Hanako Yamada","admin"),
                new User("Jake Ma","admin")
                ));
        buttonNewMessage = binding.buttonNewMessage;
        chatUserRecyclerView = binding.chatUserRecyclerview;
        adapter = new ChatUserAdapter(userList);
        chatUserRecyclerView.setAdapter(adapter);
        chatUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}