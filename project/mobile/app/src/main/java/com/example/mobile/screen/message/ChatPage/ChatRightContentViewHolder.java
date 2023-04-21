package com.example.mobile.screen.message.ChatPage;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.databinding.ChatMessageRightBinding;

public class ChatRightContentViewHolder extends ChatContentItemViewHolder {

    private ChatMessageRightBinding binding;
    private TextView chatTime;
    private TextView chatContent;

    public ChatRightContentViewHolder(ChatMessageRightBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        initialView();
    }

    public void initialView(){
        chatTime = binding.chatTime;
        chatContent = binding.chatContent;
    }

    public String getChatTime() {
        return chatTime.getText().toString();
    }

    public void setChatTime(String chatTime) {
        this.chatTime.setText(chatTime);
    }

    public String getChatContent() {
        return chatContent.getText().toString();
    }

    public void setChatContent(String chatContent) {
        this.chatContent.setText(chatContent);
    }
}
