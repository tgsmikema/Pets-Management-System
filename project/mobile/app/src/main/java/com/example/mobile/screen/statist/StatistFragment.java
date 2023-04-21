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

import com.example.mobile.databinding.FragmentStatistBinding;
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

public class StatistFragment extends Fragment{

    static class LineChartData {
        private int idx;
        private int amount;

        public LineChartData(int idx,int amount){
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
        public int getAmount(){
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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStatistBinding.inflate(inflater, container, false);
        initialView();
        initSpinner();
        initWeekLineData();
        initMonthLineChart();
        return binding.getRoot();
    }

    @Override
    public void onResume(){
        super.onResume();
        initialView();
        initSpinner();
        initWeekLineData();
        initMonthLineChart();
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
        weekLineChart = binding.weekChart;
        monthLineChart = binding.monthChart;
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

    public void initWeekLineData(){

        Description description = new Description();
        description.setText("week statistic");
        description.setTextSize(13);
        weekLineChart.setDescription(description);
        Legend legend = weekLineChart.getLegend();
        legend.setTextSize(12);

        weekLineChart.setNoDataText("No data available");

        XAxis xAxis = weekLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[] {"Mon","Tue","Wed","Thur","Fri","Sat","Sun"}));
        xAxis.setGranularity(1);
        xAxis.setTextSize(13);
        YAxis yAxis = weekLineChart.getAxisLeft();
        yAxis.setTextSize(13);
        YAxis yAxisRight = weekLineChart.getAxisRight();
        yAxisRight.setTextSize(13);
        List<LineChartData> dataObjects = new ArrayList<>(Arrays.asList(new LineChartData(0,123),
                new LineChartData(1,56),
                new LineChartData(2,89),
                new LineChartData(3,29),
                new LineChartData(4,189),
                new LineChartData(5,79),
                new LineChartData(6,101)
                ));

        List<LineChartData> dataObjects1 = new ArrayList<>(Arrays.asList(new LineChartData(0,223)
                ,new LineChartData(1,156),
                new LineChartData(2,89),
                new LineChartData(3,129),
                new LineChartData(4,19),
                new LineChartData(5,109),
                new LineChartData(6,11)
                ));

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

        LineDataSet dataSet = new LineDataSet(entries, "weighted"); // add entries to dataset
        dataSet.setColor(Color.rgb(255,192,203));
        dataSet.setLineWidth(2);
        dataSet.setValueTextSize(13);
        dataSet.setCircleColor(Color.RED);

        LineDataSet dataSet1 = new LineDataSet(entries1, "unweighted"); // add entries to dataset
        dataSet1.setColor(Color.rgb(173,216,230));
        dataSet1.setLineWidth(2);
        dataSet1.setValueTextSize(13);
        dataSet1.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        dataSets.add(dataSet1);

        LineData lineData = new LineData(dataSets);
        weekLineChart.setData(lineData);
        weekLineChart.invalidate(); // refresh
        weekLineChart.notifyDataSetChanged();
    }

    public void initMonthLineChart(){
        Description description = new Description();
        description.setText("month statistic");
        description.setTextSize(13);
        monthLineChart.setDescription(description);

        monthLineChart.setNoDataText("No data available");

        XAxis xAxis = monthLineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[] {"4.1","4.5","4.9","4.13","4.17","4.22","4.27"}));
        xAxis.setGranularity(1);
        xAxis.setTextSize(13);
        YAxis yAxis = monthLineChart.getAxisLeft();
        yAxis.setTextSize(13);
        YAxis yAxisRight = monthLineChart.getAxisRight();
        yAxisRight.setTextSize(13);

        Legend legend = monthLineChart.getLegend();
        legend.setTextSize(13);

        List<LineChartData> dataObjects = new ArrayList<>(Arrays.asList(new LineChartData(0,123),
                new LineChartData(1,56),
                new LineChartData(2,89),
                new LineChartData(3,29),
                new LineChartData(4,189),
                new LineChartData(5,79),
                new LineChartData(6,101)
        ));

        List<LineChartData> dataObjects1 = new ArrayList<>(Arrays.asList(new LineChartData(0,223)
                ,new LineChartData(1,156),
                new LineChartData(2,89),
                new LineChartData(3,129),
                new LineChartData(4,19),
                new LineChartData(5,109),
                new LineChartData(6,11)
        ));

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

        LineDataSet dataSet = new LineDataSet(entries, "weighted"); // add entries to dataset
        dataSet.setColor(Color.rgb(255,192,203));
        dataSet.setLineWidth(2);
        dataSet.setValueTextSize(13);
        dataSet.setCircleColor(Color.RED);

        LineDataSet dataSet1 = new LineDataSet(entries1, "unweighted"); // add entries to dataset
        dataSet1.setColor(Color.rgb(173,216,230));
        dataSet1.setLineWidth(2);
        dataSet1.setValueTextSize(13);
        dataSet1.setCircleColor(Color.RED);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        dataSets.add(dataSet1);

        LineData lineData = new LineData(dataSets);
        monthLineChart.setData(lineData);
        monthLineChart.invalidate(); // refresh
        monthLineChart.notifyDataSetChanged();
    }
}