package com.example.carprojecthw2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private PieChart dailyBudgetChart;
    private Button addExpenseButton;

    private RecyclerView dailySpendingRecyclerView;
    private TextView emptyRecyclerView;

    private ProgressBar savingsProgress;
    private LineChart spendingHistory;

    private CardView spendingCardView;
    private EditText spendingAmount;
    private Button spend;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

            View root = inflater.inflate(R.layout.fragment_home, container, false);

            homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);


            dailyBudgetChart = root.findViewById(R.id.dailyBudgetChart);
            dailyBudgetChart.setDrawHoleEnabled(true);
            dailyBudgetChart.setHoleRadius(80);
            dailyBudgetChart.setHoleColor(Color.WHITE);

            addExpenseButton = root.findViewById(R.id.add);

            dailySpendingRecyclerView = root.findViewById(R.id.dailySpendingRecyclerView);
            emptyRecyclerView = root.findViewById(R.id.emptyRecyclerView);

            savingsProgress = root.findViewById(R.id.savingsProgress);
            spendingHistory = root.findViewById(R.id.budgetHistory);

            spendingCardView = root.findViewById(R.id.spendCardView);
            spendingAmount = root.findViewById(R.id.addingExpenseChangeable);
            spend = root.findViewById(R.id.spendButton);






        return root;

    }



}
