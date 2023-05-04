package com.example.mobile.screen.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ChatUserItemBinding;
import com.example.mobile.model.User;

import java.util.List;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserViewHolder> {

    private List<User> chatUserList;
    private OnItemClickListener listener;

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
         holder.setUserName(user.getFirstName() + " " + user.getLastName());
         holder.setUserType(user.getUserType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(user);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
