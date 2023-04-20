package com.example.mobile.screen.message;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ChatUserItemBinding;
import com.example.mobile.model.User;

import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserViewHolder> {

    private List<User> chatUserList;

    public ChatUserAdapter(List<User> chatUserList) {
        this.chatUserList = chatUserList;
    }

    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatUserViewHolder(ChatUserItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder holder, int position) {
         User user = chatUserList.get(position);
         holder.setUserName(user.getUserName());
         holder.setUserType(user.getUserType());
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }
}
