package com.example.mobile.screen.message.ChatPage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentChatWithPeopleBinding;
import com.example.mobile.model.Message;
import com.example.mobile.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatFragment extends Fragment {

    private FragmentChatWithPeopleBinding binding;
    private TextView chatUserName;
    private TextView chatUserType;

    private RecyclerView chatContentRecyclerView;

    private ChatContentAdapter adapter;

    private EditText sendChatText;
    private ImageButton sendChatButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatWithPeopleBinding.inflate(inflater, container, false);
        Bundle bundle = getArguments();
        initialView(bundle);
        return binding.getRoot();
    }


    public void initialView(Bundle bundle){
        chatUserName = binding.chatPageUserName;
        chatUserType = binding.chatPageUserType;
        chatContentRecyclerView = binding.chatContentRecyclerview;
        sendChatText = binding.sendChatText;
        sendChatButton = binding.sendChatButton;

        chatUserName.setText(bundle.getString("name"));
        chatUserType.setText(bundle.getString("type"));

        List<Message> messages = new ArrayList<>(Arrays.asList(
                new Message(1,"10:01 14/03/2022","hello mike, how are you?", SPCApplication.currentUser,new User("John","admin")),
                new Message(2,"10:01 14/03/2022","I am fine thank you, and you?", new User("John","admin"),SPCApplication.currentUser),
                new Message(3,"10:01 14/03/2022","Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid?", SPCApplication.currentUser,new User("John","admin")),
                new Message(4,"10:01 14/03/2022","hello mike, how are you?", SPCApplication.currentUser,new User("John","admin")),
                new Message(5,"10:01 14/03/2022","Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis",new User("John","admin"),SPCApplication.currentUser),
                new Message(6,"10:01 14/03/2022","hello mike, how are you?", SPCApplication.currentUser,new User("John","admin")),
                new Message(7,"10:01 14/03/2022","hello mike, how are you?",new User("John","admin"),SPCApplication.currentUser),
                new Message(8,"10:01 14/03/2022","hello mike, how are you?", SPCApplication.currentUser,new User("John","admin")),
                new Message(9,"10:01 14/03/2022","hello mike, how are you?",new User("John","admin"),SPCApplication.currentUser),
                new Message(10,"10:01 14/03/2022","hello mike, how are you?", SPCApplication.currentUser,new User("John","admin"))
        ));
        adapter = new ChatContentAdapter(messages);
        chatContentRecyclerView.setAdapter(adapter);
        chatContentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sendChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Bundle bundle = getArguments();
        initialView(bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
