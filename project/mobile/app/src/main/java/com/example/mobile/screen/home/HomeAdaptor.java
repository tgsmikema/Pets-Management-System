package com.example.mobile.screen.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.model.Dog;
import com.example.mobile.screen.dog.DogFragment;
import com.example.mobile.util.TimeUtil;

import java.util.List;

public class HomeAdaptor extends RecyclerView.Adapter<HomeAdaptor.ViewHolder> {

    private static List<Dog> dogList;
    private ItemClickListener clickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView id, name, breed, weight, date;
        private ImageView flag, alert;
        Dog dog;

        public ViewHolder(@NonNull View v) {
            super(v);
            id = v.findViewById(R.id.text_id);
            name = v.findViewById(R.id.text_name);
            breed = v.findViewById(R.id.text_breed);
            weight = v.findViewById(R.id.text_weight);
            date = v.findViewById(R.id.text_date);
            flag = v.findViewById(R.id.image_flag);
            alert = v.findViewById(R.id.image_alert);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Dog thisDog = dogList.get(getAdapterPosition());
            Intent intent = new Intent(view.getContext(), DogFragment.class);
            intent.putExtra("id", thisDog.getId());
            view.getContext().startActivity(intent);
        }
    }

    public HomeAdaptor(List<Dog> dogList, ItemClickListener clickListener) {
        this.dogList = dogList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public HomeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.component_card_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdaptor.ViewHolder vh, int position) {
        //TODO: set content from database
        //e.g vh.name.setText(dogList.get(position).getName());
        Dog dog = dogList.get(position);
        vh.id.setText(String.valueOf(dog.getId()));
        vh.name.setText(dog.getName());
        vh.breed.setText(dog.getBreed());
        vh.weight.setText(String.valueOf(dog.getLastCheckInWeight()));
        vh.date.setText(TimeUtil.getFormatDataStringForDogDate(Long.parseLong(dog.getLastCheckInTimeStamp().trim()) * 1000));
        if (dog.isFlag()) {
            vh.flag.setImageResource(R.drawable.ic_flag_on);
        } else {
            vh.flag.setImageResource(R.drawable.blank);
        }
        if (dog.isAlert()) {
            vh.alert.setImageResource(R.drawable.ic_alert_on);
        } else {
            vh.alert.setImageResource(R.drawable.blank);
        }
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(dog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    public interface ItemClickListener {
        public void onItemClick(Dog dog);
    }
}
