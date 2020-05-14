package com.example.carprojecthw2;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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


    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

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


        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPropertyAnimator animator = spendingCardView.animate();
                animator.translationX(spendingCardView.getWidth());
                animator.setDuration(500);
                animator.start();

            }
        });


        final float x[] = new float[2];
        spendingCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x[0] = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        x[1] = event.getX();
                        if ((x[0] - x[1]) > 300) {
                            ViewPropertyAnimator animate = spendingCardView.animate();
                            animate.translationX(-spendingCardView.getWidth());
                            animate.setDuration(500);
                            animate.start();
                        }
                        break;

                    default:
                }
                return true;
            }
        });


        spend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(spendingAmount.getText().toString().matches("(\\d*\\.?\\d{0,2})")){
                    homeViewModel.decreaseTodaysSpending(Float.parseFloat(spendingAmount.getText().toString()));
                }else{
                    Toast.makeText(getActivity(),"Incorrect Format: Example: XX.XX", Toast.LENGTH_LONG).show();
                }
            }
        });







        return root;
    }

}
