package com.example.mobile.screen.message;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ChatUserItemBinding;

public class ChatUserViewHolder extends RecyclerView.ViewHolder {

    private ChatUserItemBinding binding;

    private TextView textViewUserName;
    private TextView textViewUserType;


    public ChatUserViewHolder(ChatUserItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        initialView();
    }

    public void initialView(){
        textViewUserName = binding.textChatUsername;
        textViewUserType = binding.textChatUserType;
    }

    public void setUserName(String name){
        textViewUserName.setText(name);
    }

    public String getUserName(){
        return textViewUserName.getText().toString();
    }

    public void setUserType(String type){
        textViewUserType.setText(type);
    }

    public String getUserType(){
        return textViewUserType.getText().toString();
    }
}
