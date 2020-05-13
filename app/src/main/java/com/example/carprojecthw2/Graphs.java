package com.example.carprojecthw2;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

public class Graphs extends AppCompatActivity {
    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        barChart = (BarChart) findViewById(R.id.bargraph);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f,0));
        barEntries.add(new BarEntry(88f, 1));
        barEntries.add(new BarEntry(66f, 2));
        barEntries.add(new BarEntry(12f, 3));
        barEntries.add(new BarEntry(19f, 4));
        barEntries.add(new BarEntry(16f, 5));
        barEntries.add(new BarEntry(26f, 6));
        barEntries.add(new BarEntry(36f, 7));
        barEntries.add(new BarEntry(46f, 8));
        barEntries.add(new BarEntry(56f, 9));
        barEntries.add(new BarEntry(76f, 10));
        barEntries.add(new BarEntry(86f, 11));


        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");

        ArrayList<String> dates = new ArrayList<>();

        dates.add("January");
        dates.add("February");
        dates.add("March");
        dates.add("April");
        dates.add("May");
        dates.add("June");
        dates.add("July");
        dates.add("August");
        dates.add("September");
        dates.add("October");
        dates.add("November");
        dates.add("December");

        BarData theData = new BarData((IBarDataSet) dates, barDataSet);
        barChart.setData(theData);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }
}
