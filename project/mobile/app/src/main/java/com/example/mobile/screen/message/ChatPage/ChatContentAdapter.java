package com.example.mobile.screen.message.ChatPage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.ChatMessageLeftBinding;
import com.example.mobile.databinding.ChatMessageRightBinding;
import com.example.mobile.model.Message;

import java.util.List;

public class ChatContentAdapter extends RecyclerView.Adapter<ChatContentItemViewHolder> {

    private static final int TYPE_LEFT= 1;
    private static final int TYPE_RIGHT = 2;
    private List<Message> messages;

    public ChatContentAdapter(List<Message> messages){
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatContentItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_LEFT:
                return new ChatLeftContentViewHolder(ChatMessageLeftBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false));
            case TYPE_RIGHT:
                return new ChatRightContentViewHolder(ChatMessageRightBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false));
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatContentItemViewHolder holder, int position) {
         Message message = messages.get(position);
         holder.setChatTime(message.getDate());
         holder.setChatContent(message.getMessageContent());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getFromUser().getId() == SPCApplication.currentUser.getId()) {
            return TYPE_RIGHT;
        } else {
            return TYPE_LEFT;
        }
    }
}
