package com.example.mobile.screen.home;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentHomeBinding;
import com.example.mobile.model.Dog;
import com.example.mobile.screen.dog.DogFragment;
import com.example.mobile.service.SPCAService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Spinner spinner;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ImageButton plusButton;
    private Button nameButton,lastCheckButton;
    private int centreId;
    private List<Dog> dogs;

    private List<Dog> curDogs;
    private List<String> allCentres;

    private boolean nameAscendSort;

    private boolean LastCheckAscendSort;
    private SPCAService spcaService;

    int dogId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        centreId = SPCApplication.currentUser.getUserType().equals("admin") ? 0 : SPCApplication.currentUser.getCentreId();
        spcaService = new SPCAService();
        dogs = new ArrayList<>();
        nameAscendSort = false;
        LastCheckAscendSort = false;
        //drop down menu to select centers
        initView();

        return binding.getRoot();
    }

    public void initView(){
        spinner = binding.dropdownCenters;
        initSpinner();
        //search bar
        searchView = binding.search;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO: Perform search operation with query string
                List<Dog> collect = dogs.stream().filter(it -> {
                    return it.getName().trim().toLowerCase().contains(query) ||
                            it.getBreed().trim().toLowerCase().contains(query);
                }).collect(Collectors.toList());

                curDogs = collect;
                initAdaptor();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false); // clear text
                curDogs = dogs;
                initAdaptor();
                return false;
            }
        });
        //recycler view
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchDogData();

        //add button
        plusButton = binding.plusButton;

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDog addDog = new AddDog();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,addDog).addToBackStack(null).commit();
            }
        });

        nameButton = binding.filterName;
        lastCheckButton = binding.filterLastcheck;

        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(curDogs, Comparator.comparing(a -> a.getName().trim().toLowerCase()));
                if(nameAscendSort) {
                    Collections.reverse(curDogs);
                }
                initAdaptor();
                nameAscendSort = !nameAscendSort;
            }
        });

        lastCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curDogs.sort((a, b) -> a.getLastCheckInTimeStamp().compareTo(b.getLastCheckInTimeStamp()));
                if(LastCheckAscendSort){
                    Collections.reverse(curDogs);
                }
                initAdaptor();
                LastCheckAscendSort = !LastCheckAscendSort;
            }
        });
    }

    public void fetchDogData(){

        if(!SPCApplication.currentUser.getUserType().equals("admin")){
            Call<List<Dog>> allDogFromNonAdminUser = spcaService.getAllDogFromNonAdminUser(SPCApplication.currentUser.getToken());
            allDogFromNonAdminUser.enqueue(new Callback<List<Dog>>() {
                @Override
                public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                     dogs = response.body();
                     curDogs = dogs;
                     initAdaptor();
                }

                @Override
                public void onFailure(Call<List<Dog>> call, Throwable t) {

                }
            });
            return;
        }

        Call<List<Dog>> allDogFromAllCentres = spcaService.getAllDogFromOneCentre(SPCApplication.currentUser.getToken(),centreId);
        allDogFromAllCentres.enqueue(new Callback<List<Dog>>() {
            @Override
            public void onResponse(Call<List<Dog>> call, Response<List<Dog>> response) {
                List<Dog> body = response.body();
                dogs = body;
                curDogs = dogs;
                initAdaptor();
            }

            @Override
            public void onFailure(Call<List<Dog>> call, Throwable t) {

            }
        });
    }

    public void initSpinner(){
        if (SPCApplication.currentUser.getUserType().equals("admin")) {
            allCentres = SPCApplication.allCentres.stream().map(it -> it.getName()).collect(Collectors.toList());
        } else {
            allCentres = new ArrayList<String>();
            allCentres.add(SPCApplication.allCentres.get(SPCApplication.currentUser.getCentreId()).getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.text_spinner, allCentres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                TextView curItem = (TextView) parent.getChildAt(0);
                curItem.setTextSize(30);
                curItem.setTypeface(null, Typeface.BOLD);
                if (SPCApplication.currentUser.getUserType().equals("admin")) {
                    centreId = position;
                    fetchDogData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void initAdaptor(){
        HomeAdaptor listAdaptor = new HomeAdaptor(curDogs, this::onItemClick);
        recyclerView.setAdapter(listAdaptor);
    }

    public void onItemClick(Dog dog) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", dog.getId());
        DogFragment fragment = new DogFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume(){
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}