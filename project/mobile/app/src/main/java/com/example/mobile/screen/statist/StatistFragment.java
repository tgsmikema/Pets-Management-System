package com.example.mobile.screen.statist;

import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
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
import androidx.fragment.app.Fragment;

import com.example.mobile.databinding.FragmentHomeBinding;
import com.example.mobile.databinding.FragmentStatistBinding;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatistFragment extends Fragment{

    private FragmentStatistBinding binding;
    private Spinner spinner;
    private TextView textViewForWeighted;
    private TextView textViewForUnweighted;

    private TextView textViewForWeekDate;

    private ImageButton imageButtonForWeekBack;

    private ImageButton imageButtonForWeekForward;

    private TextView textViewForMonthDate;

    private ImageButton imageButtonForMonthBack;

    private ImageButton imageButtonForMonthForward;

    private List<String> allCentres;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatistBinding.inflate(inflater, container, false);
        initialView();
        initSpinner();
        return binding.getRoot();
    }

    @Override
    public void onResume(){
        super.onResume();
        initialView();
        initSpinner();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initialView() {
        spinner = binding.spinner;
        textViewForWeighted = binding.textWeightedAmount;
        textViewForUnweighted = binding.textUnWeightedAmount;
        textViewForWeekDate = binding.textWeekDate;
        imageButtonForWeekBack = binding.buttonWeekBack;
        imageButtonForWeekForward = binding.buttonWeekForward;
        textViewForMonthDate = binding.textMonthDate;
        imageButtonForMonthBack = binding.buttonMonthBack;
        imageButtonForMonthForward = binding.buttonMonthForward;
    }

    public void initSpinner(){
        allCentres = new ArrayList<>(Arrays.asList("All Centres","Centre1","Centre2","Centre3","Centre4"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, allCentres);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
                TextView curItem = (TextView) parent.getChildAt(0);
                curItem.setTextSize(30);
                curItem.setTypeface(null, Typeface.BOLD);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


}