package com.example.carprojecthw2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private PieChart dailyBudgetChart;
    private Button addExpenseButton;

    private RecyclerView dailySpendingRecyclerView;
    private MyAdapter myAdapter;
    private ArrayList<Expenses> expensesList = new ArrayList<>();
    private TextView emptyRecyclerView;

    private ProgressBar savingsProgress;
    private LineChart spendingHistory;

    private CardView spendingCardView;
    private EditText spendingAmount;
    private Button spend;

    private float todaysRemainingFunds;
    private float todaysSpending;
    private float todaysOverage;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        todaysRemainingFunds = homeViewModel.getTodaysRemainingFunds().getValue();
        todaysSpending = homeViewModel.getTodaysSpending().getValue();
        todaysOverage = homeViewModel.getTodaysOverage().getValue();

        List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(todaysRemainingFunds, "Left for Today"));
        pieEntries.add(new PieEntry(todaysSpending, "Spent Today"));
        pieEntries.add(new PieEntry(todaysOverage, "Overage Today"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "Today's Spending");
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.LTGRAY);
        colors.add(Color.RED);
        dataSet.setColors(colors);
        final PieData data = new PieData(dataSet);

        dailyBudgetChart = root.findViewById(R.id.dailyBudgetChart);
        dailyBudgetChart.setDrawHoleEnabled(true);
        dailyBudgetChart.setHoleRadius(80);
        dailyBudgetChart.setHoleColor(Color.WHITE);

        dailyBudgetChart.setData(data);

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
                    homeViewModel.increaseTodaysSpending(Float.parseFloat(spendingAmount.getText().toString()));
                    homeViewModel.decreaseTodaysRemainingFunds(Float.parseFloat(spendingAmount.getText().toString()));

                    expensesList.add(new Expenses("vibrator", Float.parseFloat(spendingAmount.getText().toString())));
                    myAdapter.notifyDataSetChanged();
                    emptyRecyclerView.setVisibility(View.INVISIBLE);

                }else{
                    Toast.makeText(getActivity(),"Incorrect Format: Example: XX.XX", Toast.LENGTH_LONG).show();
                }
            }
        });

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.app_name);
                builder.setMessage("Are you sure you want to remove this expense?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remove(position);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        };


        dailySpendingRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        dailySpendingRecyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(getContext(), expensesList, listener);
        dailySpendingRecyclerView.setAdapter(myAdapter);

        homeViewModel.getTodaysSpending().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {

            }
        });





        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences mPrefs = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();

        editor.putFloat("todaysRemainingFunds", todaysRemainingFunds);
        editor.putFloat("todaysSpending", todaysSpending);
        editor.putFloat("todaysOverage", todaysOverage);
        editor.commit();

    }

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public void remove(int position){
        expensesList.remove(position);
        myAdapter.notifyDataSetChanged();
    }

}
