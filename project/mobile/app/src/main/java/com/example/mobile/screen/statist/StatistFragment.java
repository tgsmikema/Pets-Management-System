package com.example.mobile.screen.statist;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mobile.R;
import com.example.mobile.SPCApplication;
import com.example.mobile.databinding.FragmentStatistBinding;
import com.example.mobile.model.TimeWeightRequest;
import com.example.mobile.model.WeightData;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatistFragment extends Fragment {

    static class LineChartData {
        private int idx;
        private int amount;

        public LineChartData(int idx, int amount) {
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

        public int getAmount() {
            return amount;
        }

    }

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

    private LineChart weekLineChart;

    private LineChart monthLineChart;

    private List<String> allCentres;

    private SPCAService spcaService;

    private int centreId;

    private volatile boolean isActivityRunning;

    //record the timestamp when you start this timestamp
    private long stableTimeStampForWeek;

    private long curTimeStampForWeek;

    private long stableTimeStampForMonth;

    private long curTimeStampForMonth;

    private List<Integer> weekWeightList;
    private List<Integer> weekUnWeightList;

    private List<Integer> monthWeightList;

    private List<Integer> monthUnWeightList;

    private List<String> listOfMonthDate;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatistBinding.inflate(inflater, container, false);
        centreId = SPCApplication.currentUser.getUserType().equals("admin") ? 0 : SPCApplication.currentUser.getCentreId();
        isActivityRunning = true;
        stableTimeStampForWeek = TimeUtil.curTimeStamp();
        curTimeStampForWeek = TimeUtil.curTimeStamp();
        stableTimeStampForMonth = TimeUtil.getMidDayOfMonth(TimeUtil.getPreviousMonth(TimeUtil.getMidDayOfMonth(TimeUtil.curTimeStamp())));
        curTimeStampForMonth = stableTimeStampForMonth;
        weekWeightList = new ArrayList<>();
        weekUnWeightList = new ArrayList<>();
        monthWeightList = new ArrayList<>();
        monthUnWeightList = new ArrayList<>();
        listOfMonthDate = new ArrayList<>();

        initialView();
        initSpinner();
        initWeekLineChartData();
        initMonthLineChartData();
        fetchNeededData();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        isActivityRunning = true;
        initialView();
        initSpinner();
        initWeekLineChartData();
        initMonthLineChartData();
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityRunning = false;
        binding = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isActivityRunning = false;
        binding = null;
    }

    public void fetchNeededData() {
        spcaService = new SPCAService();
        Call<WeightData> call = spcaService.fetchThisWeekStatist(SPCApplication.currentUser.getToken(), centreId);
        call.enqueue(new Callback<WeightData>() {
            @Override
            public void onResponse(Call<WeightData> call, Response<WeightData> response) {
                WeightData weekWeightData = response.body();
                setThisWeekStatist(weekWeightData.getNoOfDogsWeighted(), weekWeightData.getNoOfDogsUnweighted());
            }

            @Override
            public void onFailure(Call<WeightData> call, Throwable t) {
            }
        });

        fetchWeekDate();
        fetchMonthData();
    }

    public void fetchWeekDate() {
        long minTimestamp = TimeUtil.getPreviousWeekTimeStamp(curTimeStampForWeek) / 1000;
        long maxTimestamp = curTimeStampForWeek / 1000;

        Call<List<WeightData>> listCall = spcaService.fetchWeekData(SPCApplication.currentUser.getToken(), new TimeWeightRequest(centreId, String.valueOf(minTimestamp), String.valueOf(maxTimestamp)));

        listCall.enqueue(new Callback<List<WeightData>>() {
            @Override
            public void onResponse(Call<List<WeightData>> call, Response<List<WeightData>> response) {
                List<WeightData> body = response.body();
                setWeekData(body);
            }

            @Override
            public void onFailure(Call<List<WeightData>> call, Throwable t) {

            }
        });

    }

    public void fetchMonthData() {
        long minTimeStamp = TimeUtil.getFirstDayOfMonth(curTimeStampForMonth) / 1000;
        long maxTimeStamp = TimeUtil.getLastDayOfMonth(curTimeStampForMonth) / 1000;

        Call<List<WeightData>> listCall = spcaService.fetchMonthData(SPCApplication.currentUser.getToken(), new TimeWeightRequest(centreId, String.valueOf(minTimeStamp), String.valueOf(maxTimeStamp)));

        listCall.enqueue(new Callback<List<WeightData>>() {
            @Override
            public void onResponse(Call<List<WeightData>> call, Response<List<WeightData>> response) {
                List<WeightData> body = response.body();
                setMonthData(body);
            }

            @Override
            public void onFailure(Call<List<WeightData>> call, Throwable t) {

            }
        });

    }

    public void setThisWeekStatist(int weight, int unweight) {

        if (!isActivityRunning) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update UI here
                // You can access UI components like TextView, ImageView, etc. here
                textViewForWeighted.setText(String.valueOf(weight));
                textViewForUnweighted.setText(String.valueOf(unweight));
            }
        });
    }

    public void setWeekData(List<WeightData> data) {
        if (!isActivityRunning) {
            return;
        }
        weekWeightList = data.stream().map(it -> it.getNoOfDogsWeighted()).collect(Collectors.toList());
        weekUnWeightList = data.stream().map(it -> it.getNoOfDogsUnweighted()).collect(Collectors.toList());
        initWeekLineChartData();
    }

    public void setMonthData(List<WeightData> data) {
        if (!isActivityRunning) {
            return;
        }
        monthWeightList = data.stream().map(it -> it.getNoOfDogsWeighted()).collect(Collectors.toList());
        monthUnWeightList = data.stream().map(it -> it.getNoOfDogsUnweighted()).collect(Collectors.toList());
        listOfMonthDate = data.stream().map(it -> TimeUtil.getDateSimplyString(Long.parseLong(it.getTimeStamp()) * 1000)).collect(Collectors.toList());
        initMonthLineChartData();
    }

    public String getWeekDateString() {
        long preWeekTimeStamp = TimeUtil.getPreviousWeekTimeStamp(curTimeStampForWeek);
        return TimeUtil.getDateSimplyString(preWeekTimeStamp) + "-" + TimeUtil.getDateSimplyString(curTimeStampForWeek);
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
        weekLineChart = binding.weekChart;
        monthLineChart = binding.monthChart;
        textViewForWeekDate.setText(getWeekDateString());
        textViewForMonthDate.setText(TimeUtil.getMonthSimplyString(curTimeStampForMonth));

        imageButtonForWeekBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curTimeStampForWeek = TimeUtil.getPreviousWeekTimeStamp(curTimeStampForWeek);
                textViewForWeekDate.setText(getWeekDateString());
                fetchWeekDate();
            }
        });

        imageButtonForWeekForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curTimeStampForWeek < stableTimeStampForWeek) {
                    curTimeStampForWeek = TimeUtil.getNextWeekTimeStamp(curTimeStampForWeek);
                    textViewForWeekDate.setText(getWeekDateString());
                    fetchWeekDate();
                }
            }
        });

        imageButtonForMonthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curTimeStampForMonth = TimeUtil.getPreviousMonth(curTimeStampForMonth);
                textViewForMonthDate.setText(TimeUtil.getMonthSimplyString(curTimeStampForMonth));
                fetchMonthData();
            }
        });


        imageButtonForMonthForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curTimeStampForMonth < stableTimeStampForMonth){
                    curTimeStampForMonth = TimeUtil.getNextMonth(curTimeStampForMonth);
                    textViewForMonthDate.setText(TimeUtil.getMonthSimplyString(curTimeStampForMonth));
                    fetchMonthData();
                }
            }
        });
    }

    public void initSpinner() {
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
                    fetchNeededData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void initWeekLineChartData() {

        Description description = new Description();
        description.setText("week statistic");
        description.setTextSize(13);
        weekLineChart.setDescription(description);
        Legend legend = weekLineChart.getLegend();
        legend.setTextSize(12);

        if (weekWeightList.size() == 0) {
            weekLineChart.setNoDataText("No data available");
        }
        String[] week = new String[]{"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
        int idx = new Date(curTimeStampForWeek).getDay();
        String[] weekOnXAix = new String[7];
        for (int i = 0; i < 7; i++) {
            weekOnXAix[i] = week[(idx + i) % 7];
        }

        XAxis xAxis = weekLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(weekOnXAix));
        xAxis.setGranularity(1);
        xAxis.setTextSize(13);
        YAxis yAxis = weekLineChart.getAxisLeft();
        yAxis.setTextSize(13);
        YAxis yAxisRight = weekLineChart.getAxisRight();
        yAxisRight.setTextSize(13);
        List<LineChartData> dataObjects = new ArrayList<>();
        for (int i = 0; i < weekWeightList.size(); i++) {
            dataObjects.add(new LineChartData(i, weekWeightList.get(i)));
        }

        List<LineChartData> dataObjects1 = new ArrayList<>();
        for (int i = 0; i < weekUnWeightList.size(); i++) {
            dataObjects1.add(new LineChartData(i, weekUnWeightList.get(i)));
        }

        List<Entry> entries = new ArrayList<Entry>();
        for (LineChartData data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new Entry(data.getIdx(), data.getAmount()));
        }

        List<Entry> entries1 = new ArrayList<Entry>();
        for (LineChartData data : dataObjects1) {
            // turn your data into Entry objects
            entries1.add(new Entry(data.getIdx(), data.getAmount()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "weighed"); // add entries to dataset
        dataSet.setColor(Color.rgb(255, 192, 203));
        dataSet.setLineWidth(2);
        dataSet.setValueTextSize(13);
        dataSet.setCircleColor(Color.RED);

//        LineDataSet dataSet1 = new LineDataSet(entries1, "unweighed"); // add entries to dataset
//        dataSet1.setColor(Color.rgb(173, 216, 230));
//        dataSet1.setLineWidth(2);
//        dataSet1.setValueTextSize(13);
//        dataSet1.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
//        dataSets.add(dataSet1);

        LineData lineData = new LineData(dataSets);
        weekLineChart.setData(lineData);
        weekLineChart.invalidate(); // refresh
        weekLineChart.notifyDataSetChanged();
    }

    public void initMonthLineChartData() {
        Description description = new Description();
        description.setText("month statistic");
        description.setTextSize(13);
        monthLineChart.setDescription(description);

        monthLineChart.setNoDataText("No data available");

        String[] monthDataOnXAxis = new String[listOfMonthDate.size()];
        for(int i = 0;i<listOfMonthDate.size();i++){
            monthDataOnXAxis[i] = listOfMonthDate.get(i);
        }

        XAxis xAxis = monthLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(monthDataOnXAxis));
        xAxis.setGranularity(1);
        xAxis.setTextSize(13);
        YAxis yAxis = monthLineChart.getAxisLeft();
        yAxis.setTextSize(13);
        YAxis yAxisRight = monthLineChart.getAxisRight();
        yAxisRight.setTextSize(13);

        Legend legend = monthLineChart.getLegend();
        legend.setTextSize(13);

        List<LineChartData> dataObjects = new ArrayList<>();

        for(int i = 0;i<monthWeightList.size();i++){
            dataObjects.add(new LineChartData(i,monthWeightList.get(i)));
        }

        List<LineChartData> dataObjects1 = new ArrayList<>();

        for(int i = 0;i<monthUnWeightList.size();i++){
            dataObjects1.add(new LineChartData(i,monthUnWeightList.get(i)));
        }

        List<Entry> entries = new ArrayList<Entry>();
        for (LineChartData data : dataObjects) {
            // turn your data into Entry objects
            entries.add(new Entry(data.getIdx(), data.getAmount()));
        }

        List<Entry> entries1 = new ArrayList<Entry>();
        for (LineChartData data : dataObjects1) {
            // turn your data into Entry objects
            entries1.add(new Entry(data.getIdx(), data.getAmount()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "weighed"); // add entries to dataset
        dataSet.setColor(Color.rgb(255, 192, 203));
        dataSet.setLineWidth(2);
        dataSet.setValueTextSize(13);
        dataSet.setCircleColor(Color.RED);

//        LineDataSet dataSet1 = new LineDataSet(entries1, "unweighed"); // add entries to dataset
//        dataSet1.setColor(Color.rgb(173, 216, 230));
//        dataSet1.setLineWidth(2);
//        dataSet1.setValueTextSize(13);
//        dataSet1.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
//        dataSets.add(dataSet1);

        LineData lineData = new LineData(dataSets);
        monthLineChart.setData(lineData);
        monthLineChart.invalidate(); // refresh
        monthLineChart.notifyDataSetChanged();
    }
}