package com.example.mobile.screen.message.ChatPage;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ChatContentItemViewHolder extends RecyclerView.ViewHolder {
    public ChatContentItemViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    public abstract String getChatTime();
    public abstract void setChatTime(String chatTime);

    public abstract String getChatContent();

    public abstract void setChatContent(String chatContent);
}
