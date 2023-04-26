package com.example.mobile.screen.dog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.databinding.FragmentDogBinding;
import com.example.mobile.screen.statist.StatistFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DogFragment extends Fragment {

    static class LineChartData {
        private int idx;
        private double amount;

        public LineChartData(int idx, double amount){
            this.amount = amount;
            this.idx = idx;
        }
        public void setIdx(int idx){
            this.idx = idx;
        }
        public int getIdx(){
            return idx;
        }

        public void setAmount(int amount){
            this.amount = amount;
        }
        public double getAmount(){
            return amount;
        }

    }

    public class ListItem {
        private String weight;
        private String date;

        public ListItem(String weight, String date) {
            this.weight = weight;
            this.date = date;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    private FragmentDogBinding binding;
    private TextView name, breed, location, id, weight, date;
    private ImageButton isAlert, isFlagged, edit, add;
    private LineChart lineChart;
    private ListView list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        String dogId = bundle.getString("id");
        String dogName = bundle.getString("name");
        String dogBreed = bundle.getString("breed");
        String dogLocation = bundle.getString("location");
        String dogWeight = bundle.getString("weight");
        String dogDate = bundle.getString("date");
        final boolean[] dogAlert = {bundle.getBoolean("alert")};
        final boolean[] dogFlagged = {bundle.getBoolean("flagged")};

        id = root.findViewById(R.id.dog_id);
        id.setText(dogId);
        name = root.findViewById(R.id.dog_name);
        name.setText(dogName);
        breed = root.findViewById(R.id.dog_breed);
        breed.setText(dogBreed);
        location = root.findViewById(R.id.dog_location);
        location.setText(dogLocation);
        weight = root.findViewById(R.id.dog_weight);
        weight.setText(dogWeight);
        date = root.findViewById(R.id.dog_date);
        date.setText(dogDate);
        isAlert = root.findViewById(R.id.button_alert);
        if (dogAlert[0]) {
            isAlert.setImageResource(R.drawable.ic_alert_on);
        }
        else {
            isAlert.setImageResource(R.drawable.ic_alert_off);
        }
        isFlagged = root.findViewById(R.id.button_flag);
        if (dogFlagged[0]) {
            isFlagged.setImageResource(R.drawable.ic_flag_on);
        }
        else {
            isFlagged.setImageResource(R.drawable.ic_flag_off);
        }

        //TODO: get/set data from database
        isFlagged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogFlagged[0]) {
                    isFlagged.setImageResource(R.drawable.ic_flag_off);
                    dogFlagged[0] = false;
                }
                else {
                    isFlagged.setImageResource(R.drawable.ic_flag_on);
                    dogFlagged[0] = true;
                }
            }
        });
        isAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dogAlert[0]) {
                    isAlert.setImageResource(R.drawable.ic_alert_off);
                    dogAlert[0] = false;
                }
                else {
                    isAlert.setImageResource(R.drawable.ic_alert_on);
                    dogAlert[0] = true;
                }
            }
        });


        edit = root.findViewById(R.id.dog_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDog editDog = new EditDog();
                Bundle bundle = new Bundle();
                bundle.putString("id", dogId);
                bundle.putString("name", dogName);
                bundle.putString("breed", dogBreed);
                editDog.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, editDog)
                        .addToBackStack(null)
                        .commit();
            }
        });
        add = root.findViewById(R.id.dog_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Weight weight = new Weight();
                Bundle bundle = new Bundle();
                bundle.putString("name", dogName);
                bundle.putString("breed", dogBreed);
                bundle.putString("id", dogId);
                bundle.putString("location", dogLocation);
                bundle.putString("weight", dogWeight);
                bundle.putString("date", dogDate);
                bundle.putBoolean("flag", dogFlagged[0]);
                bundle.putBoolean("alert", dogAlert[0]);
                weight.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, weight)
                        .addToBackStack(null)
                        .commit();
            }

        });

        //line chart
        lineChart = root.findViewById(R.id.dog_chart);
        Description description = new Description();
        description.setText("Weight history");
        description.setTextSize(13);
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextSize(12);

        lineChart.setNoDataText("No data available");

        //TODO: get data from database
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[] {"Dec", "Jan", "Feb", "Mar", "Apr"}));
        xAxis.setGranularity(1);
        xAxis.setTextSize(13);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextSize(13);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setTextSize(13);
        List<DogFragment.LineChartData> dataObjects = new ArrayList<>(Arrays.asList(
                new DogFragment.LineChartData(0,1.2),
                new DogFragment.LineChartData(1,4.8),
                new DogFragment.LineChartData(2,7.6),
                new DogFragment.LineChartData(3,8.3),
                new DogFragment.LineChartData(4,10.1),
                new DogFragment.LineChartData(5,12)
        ));

        List<Entry> entries = new ArrayList<Entry>();
        for (DogFragment.LineChartData data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new Entry(data.getIdx(), (float) data.getAmount()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "weight"); // add entries to dataset
        dataSet.setColor(Color.rgb(255,192,203));
        dataSet.setLineWidth(2);
        dataSet.setValueTextSize(13);
        dataSet.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
        lineChart.notifyDataSetChanged();


        //list view
        //TODO: get data from database
        list = root.findViewById(R.id.weight_list);
        List<ListItem> itemList = new ArrayList<>();
        itemList.add(new ListItem("10 kg", "10/03/23"));
        itemList.add(new ListItem("8 kg", "7/03/23"));
        itemList.add(new ListItem("12 kg", "1/03/23"));

        ArrayAdapter<ListItem> adapter = new ArrayAdapter<ListItem>(getActivity(), android.R.layout.simple_list_item_1, itemList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                // Get the current ListItem object
                ListItem listItem = itemList.get(position);
                // Set the weight and date values to the TextView
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(listItem.getWeight() + " (" + listItem.getDate() + ")");

                return view;
            }
        };
        // Set the adapter to the ListView
        list.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}