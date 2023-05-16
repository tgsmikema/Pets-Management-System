package com.example.mobile.screen.dog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentDogBinding;
import com.example.mobile.model.DogDetail;
import com.example.mobile.model.DogWeightHistory;
import com.example.mobile.service.SPCAService;
import com.example.mobile.util.TimeUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DogFragment extends Fragment {

    private static class LineChartData {
        private int idx;
        private double amount;

        public LineChartData(int idx, double amount) {
            this.amount = amount;
            this.idx = idx;
        }

        public void setIdx(int idx) {
            this.idx = idx;
        }

        public int getIdx() {
            return idx;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getAmount() {
            return amount;
        }
    }

    private static class ListItem {
        private double weight;
        private String date;

        public ListItem(double weight, String date) {
            this.weight = weight;
            this.date = date;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
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
    private List<ListItem> itemList;

    private ArrayAdapter<ListItem> adapter;

    int dogId;
    String dogName;
    String dogBreed;
    String dogLocation;
    Double dogWeight;
    String dogDate;
    boolean dogAlert;
    boolean dogFlagged;
    SPCAService spcaService;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDogBinding.inflate(inflater, container, false);
        itemList = new ArrayList<>();
        spcaService = new SPCAService();
        initView();
        return binding.getRoot();
    }

    public void initView() {

        Bundle bundle = getArguments();
        dogId = bundle.getInt("id");
        dogName = "";
        dogBreed = "";
        dogLocation = "";
        dogWeight = 0.0;
        dogDate = "";
        dogAlert = false;
        dogFlagged = false;

        id = binding.dogId;
        name = binding.dogName;
        breed = binding.dogBreed;
        location = binding.dogLocation;
        weight = binding.dogWeight;
        date = binding.dogDate;
        isAlert = binding.buttonAlert;
        isFlagged = binding.buttonFlag;

        fetchDogDetailData();

        isFlagged.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SPCApplication.currentUser.getUserType().toLowerCase().trim().equals("volunteer")){
                    return;
                }

                Call<ResponseBody> responseBodyCall = spcaService.toggleFlagged(SPCApplication.currentUser.getToken(), dogId);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                if (dogFlagged) {
                    isFlagged.setImageResource(R.drawable.ic_flag_off);
                    dogFlagged = false;
                } else {
                    isFlagged.setImageResource(R.drawable.ic_flag_on);
                    dogFlagged = true;
                }
            }
        });
        isAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SPCApplication.currentUser.getUserType().equals("volunteer")){
                    return;
                }

                Call<ResponseBody> responseBodyCall = spcaService.toggleAlert(SPCApplication.currentUser.getToken(), dogId);
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

                if (dogAlert) {
                    isAlert.setImageResource(R.drawable.ic_alert_off);
                    dogAlert = false;
                } else {
                    isAlert.setImageResource(R.drawable.ic_alert_on);
                    dogAlert = true;
                }
            }
        });

        edit = binding.dogEdit;
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDog editDog = new EditDog();
                Bundle bundle = new Bundle();
                bundle.putInt("id", dogId);
                bundle.putString("name", dogName);
                bundle.putString("breed", dogBreed);
                bundle.putString("location",dogLocation);
                editDog.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, editDog)
                        .addToBackStack(null)
                        .commit();
            }
        });
        add = binding.dogAdd;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Weight weight = new Weight();
                Bundle bundle = new Bundle();
                bundle.putInt("id", dogId);
                bundle.putString("name",dogName);
                weight.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, weight)
                        .addToBackStack(null)
                        .commit();
            }
        });

        lineChart = binding.dogChart;
        initLineChart();

        //list view
        //TODO: get data from database
        list = binding.weightList;
        initWeightListView();
    }

    public void fetchDogDetailData(){
        Call<DogDetail> call;
        if(SPCApplication.currentUser.getUserType().equals("admin")){
            call = spcaService.getDogDetailFromAllCentre(SPCApplication.currentUser.getToken(),dogId);
        }else{
            call = spcaService.getDogDetailFromOwnCentre(SPCApplication.currentUser.getToken(),dogId);
        }

        call.enqueue(new Callback<DogDetail>() {
            @Override
            public void onResponse(Call<DogDetail> call, Response<DogDetail> response) {
                DogDetail body = response.body();
                dogAlert = body.isAlert();
                dogFlagged = body.isFlag();
                dogBreed = body.getBreed();
                dogName = body.getName();
                dogDate = body.getLastCheckInTimeStamp();
                dogLocation = SPCApplication.allCentres.get(body.getCentreId()).getName();
                dogWeight = body.getLastCheckInWeight();
                initDogDetail();
            }

            @Override
            public void onFailure(Call<DogDetail> call, Throwable t) {

            }
        });
    }

    public void initDogDetail(){
        getActivity().runOnUiThread(() -> {
            id.setText(String.valueOf(dogId));
            name.setText(dogName);
            breed.setText(dogBreed);
            location.setText(dogLocation);
            weight.setText(String.valueOf(dogWeight));
            date.setText(TimeUtil.getFormatDataStringForDogDate(Long.parseLong(dogDate) * 1000));
            if (dogAlert) {
                isAlert.setImageResource(R.drawable.ic_alert_on);
            } else {
                isAlert.setImageResource(R.drawable.ic_alert_off);
            }

            if (dogFlagged) {
                isFlagged.setImageResource(R.drawable.ic_flag_on);
            } else {
                isFlagged.setImageResource(R.drawable.ic_flag_off);
            }
        });
    }

    public void initLineChart() {
        //line chart
        Description description = new Description();
        description.setText("Weight history");
        description.setTextSize(13);
        lineChart.setDescription(description);
        Legend legend = lineChart.getLegend();
        legend.setTextSize(12);

        if(itemList.size() == 0){
            lineChart.setNoDataText("No data available");
        }

        //TODO: get data from database
        XAxis xAxis = lineChart.getXAxis();
        List<String> dateString = itemList.stream().map(it -> it.getDate()).collect(Collectors.toList());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateString));
        xAxis.setGranularity(1);
        xAxis.setTextSize(13);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextSize(13);
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setTextSize(13);
        List<DogFragment.LineChartData> dataObjects = new ArrayList<>();
        for(int i = 0;i<itemList.size();i++){
            dataObjects.add(new LineChartData(i,itemList.get(i).getWeight()));
        }

        List<Entry> entries = new ArrayList<Entry>();
        for (DogFragment.LineChartData data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new Entry(data.getIdx(), (float) data.getAmount()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "weight"); // add entries to dataset
        dataSet.setColor(Color.rgb(255, 192, 203));
        dataSet.setLineWidth(2);
        dataSet.setValueTextSize(13);
        dataSet.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
        lineChart.notifyDataSetChanged();
    }

    public void initWeightListView() {
        Call<List<DogWeightHistory>> weightHistory = spcaService.getWeightHistory(SPCApplication.currentUser.getToken(), dogId);
        weightHistory.enqueue(new Callback<List<DogWeightHistory>>() {
            @Override
            public void onResponse(Call<List<DogWeightHistory>> call, Response<List<DogWeightHistory>> response) {
                List<DogWeightHistory> body = response.body();
                List<ListItem> collect = body.stream().map(it ->
                        new ListItem(
                                it.getDogWeight(),
                                TimeUtil.getFormatDataStringForSimplyDogDate(Long.parseLong(it.getTimeStamp()) * 1000))).collect(Collectors.toList());
                itemList = collect;
                Collections.reverse(itemList);
                initListViewAdaptor();
                initLineChart();
            }

            @Override
            public void onFailure(Call<List<DogWeightHistory>> call, Throwable t) {

            }
        });
    }

    public void initListViewAdaptor() {
        adapter = new ArrayAdapter<ListItem>(getActivity(), android.R.layout.simple_list_item_1, itemList) {
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

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, list);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        // Set the calculated height to the ListView
        ViewGroup.LayoutParams layoutParams = list.getLayoutParams();
        layoutParams.height = totalHeight + (list.getDividerHeight() * (adapter.getCount() - 1));
        list.setLayoutParams(layoutParams);

        // Set the adapter to the ListView
        list.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}