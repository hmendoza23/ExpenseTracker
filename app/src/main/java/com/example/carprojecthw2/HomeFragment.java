package com.example.carprojecthw2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.PieChart;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private PieChart dailyBudgetChart;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_home, container, false);

            homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


            dailyBudgetChart = root.findViewById(R.id.dailyBudgetChart);


            dailyBudgetChart.setDrawHoleEnabled(true);
            dailyBudgetChart.setHoleRadius(80);
            dailyBudgetChart.setHoleColor(Color.WHITE);







        return root;

    }



}
