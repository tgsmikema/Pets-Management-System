package com.example.mobile.screen.profile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdaptor extends RecyclerView.Adapter<ProfileAdaptor.ViewHolder> {

    private static List<User> userList = new ArrayList<>();
    private ItemClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, access,email;

        public ViewHolder(@NonNull View v) {
            super(v);
            name = v.findViewById(R.id.text_name);
            access = v.findViewById(R.id.text_access);
            email = v.findViewById(R.id.text_email);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User thisUser = userList.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(), EditUser.class);
            intent.putExtra("id", thisUser.getId());
            view.getContext().startActivity(intent);
        }
    }

    public ProfileAdaptor(List<User> userList, ItemClickListener clickListener) {
        this.userList = userList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ProfileAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_card_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdaptor.ViewHolder vh, int position) {
        User user = userList.get(position);
        vh.name.setText(user.getFirstName() + " " + user.getLastName());
        vh.access.setText(user.getUserType());
        vh.email.setText(user.getEmail());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface ItemClickListener {
        public void onItemClick(User user);
    }
}
