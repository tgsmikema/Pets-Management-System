package com.example.mobile.screen.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentHomeBinding;
import com.example.mobile.model.Dog;
import com.example.mobile.model.User;
import com.example.mobile.screen.dog.DogFragment;
import com.example.mobile.screen.profile.EditUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Spinner menu;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageButton plusButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //drop down menu to select centers
        //TODO: replace centers_list mock data with db
        menu = root.findViewById(R.id.dropdown_centers);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.centers_list, R.layout.text_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(adapter);

        //search bar
        searchView = root.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Perform search operation with query string
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO: Update search results based on new text
                return false;
            }
        });

        //recycler view
        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //TODO: replace mock data with db
        List<Dog> dogs = new ArrayList<>();
        dogs.add(new Dog("8093", "Kyra", "Border Collie", "Manukau", "14.8", "08/03/23", false, true));
        dogs.add(new Dog("8094", "Luna", "Husky", "Manukau","7.6", "24/03/23", true, false));
        dogs.add(new Dog("8095", "Ted", "Corgi", "Manukau","8.7", "24/03/23", false, false));
        dogs.add(new Dog("8096", "Bella", "Poodle", "Manukau","9.2", "24/03/23", true, true));

        HomeAdaptor listAdaptor = new HomeAdaptor(dogs, this::onItemClick);
        recyclerView.setAdapter(listAdaptor);

        //add button
        plusButton = root.findViewById(R.id.plus_button);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDog addDog = new AddDog();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,addDog).addToBackStack(null).commit();

            }
        });

        return root;
    }

    public void onItemClick(Dog dog) {
        Bundle bundle = new Bundle();
        bundle.putString("name", dog.getName());
        bundle.putString("breed", dog.getBreed());
        bundle.putString("id", dog.getId());
        bundle.putString("location", dog.getLocation());
        bundle.putString("weight", dog.getWeight());
        bundle.putString("date", dog.getDate());
        bundle.putBoolean("flag", dog.isFlag());
        bundle.putBoolean("alert", dog.isAlert());
        DogFragment fragment = new DogFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}