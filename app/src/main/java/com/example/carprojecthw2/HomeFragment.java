package com.example.carprojecthw2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private PieChart dailyBudgetChart;
    private Button addExpenseButton;

    private RecyclerView dailySpendingRecyclerView;
    private MyAdapter myAdapter;
    private ArrayList<Expenses> expensesList = new ArrayList<>();
    private TextView emptyRecyclerView;
    private Spinner expenseReasonSpinner;

    private ProgressBar savingsProgress;
    private TextView currentSavingsTotal;
    private TextView savingGoal;


    private LineChart spendingHistory;

    private CardView spendingCardView;
    private EditText spendingAmount;
    private Button spend;


    private PieDataSet dataSet;
    final List<Integer> colors = new ArrayList<>();
    private PieData data;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        /* All below used for the circle pie chart */
        final List<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(homeViewModel.getTodaysRemainingFunds().getValue(), "Left"));
        pieEntries.add(new PieEntry(homeViewModel.getTodaysSpending().getValue(), "Spent"));
        pieEntries.add(new PieEntry(homeViewModel.getTodaysOverage().getValue(), "Overage"));

        dataSet = new PieDataSet(pieEntries,".");
        colors.add(Color.GREEN);
        colors.add(Color.LTGRAY);
        colors.add(Color.RED);
        dataSet.setColors(colors);
        data = new PieData(dataSet);


        dailyBudgetChart = root.findViewById(R.id.dailyBudgetChart);
        dailyBudgetChart.setDrawHoleEnabled(true);
        dailyBudgetChart.setHoleRadius(80);
        dailyBudgetChart.setHoleColor(Color.WHITE);
        dailyBudgetChart.setCenterText("TODAY");
        dailyBudgetChart.setCenterTextSize(30);
        dailyBudgetChart.setDrawEntryLabels(false);
        dailyBudgetChart.getDescription().setEnabled(false);
        dailyBudgetChart.animateY(1000);
        dailyBudgetChart.setData(data);

        /* Useded for adding expenses to the recycler view */
        addExpenseButton = root.findViewById(R.id.add);
        expenseReasonSpinner = root.findViewById(R.id.expenseReasonSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.reason, android.R.layout.simple_spinner_dropdown_item);
        expenseReasonSpinner.setAdapter(adapter);

        dailySpendingRecyclerView = root.findViewById(R.id.dailySpendingRecyclerView);
        emptyRecyclerView = root.findViewById(R.id.emptyRecyclerView);

        savingsProgress = root.findViewById(R.id.savingsProgress);
        currentSavingsTotal = root.findViewById(R.id.currentSavingsTotal);
        savingGoal = root.findViewById(R.id.savingGoal);

        /* Used for line chart data of history */
        spendingHistory = root.findViewById(R.id.budgetHistory);
        ArrayList<Entry> values = new ArrayList<>();
        HashMap<String, Float> moneyData = new HashMap<>();
        SharedPreferences history = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetHistory", Context.MODE_PRIVATE);
        Gson gson1 = new Gson();
        Map<String, ?> keys = history.getAll();

        for(String name : keys.keySet()) {
            String json = keys.get(name).toString();

            String todayValue = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()).substring(0,6);

            if(name.contains(todayValue)){
                HashMap<String, String> data = gson1.fromJson(json, new TypeToken<HashMap<String, String>>(){}.getType());

                float remaining = Float.parseFloat(Objects.requireNonNull(data.get("remainingFunds")));
                float overage = Float.parseFloat(Objects.requireNonNull(data.get("overage")));

                moneyData.put(name, remaining-overage);
            }

        }

        // TreeMap to store values of HashMap
        TreeMap<String, Float> sorted = new TreeMap<>();

        // Copy all data from hashMap into TreeMap
        sorted.putAll(moneyData);

        HashMap<String,Float> sortedData = new HashMap<>();
        // Display the TreeMap which is naturally sorted
        int a = 1;
        for (Map.Entry<String, Float> entry : sorted.entrySet()) {
            sortedData.put(entry.getKey(), entry.getValue());
            values.add(new Entry(a, entry.getValue()));
                    a++;
        }

        for(int j = 0; j < 10; j ++){
            values.add(new Entry(j,(j*10)%15));
        }

        LineDataSet lineDataSet = new LineDataSet(values, "Month");
        LineData lineData = new LineData(lineDataSet);
        spendingHistory.setData(lineData);


        spendingCardView = root.findViewById(R.id.spendCardView);
        spendingAmount = root.findViewById(R.id.addingExpenseChangeable);
        spend = root.findViewById(R.id.spendButton);

        boolean setByViewModel = false;
        /* updating expense list */
        homeViewModel.getExpenseMax().observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                expensesList.clear();
                emptyRecyclerView.setVisibility(View.VISIBLE);
                homeViewModel.setExpenseList(expensesList);

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE).edit();
                editor.remove("expensesList");
                editor.commit();
            }
        });

        /* updating expense list and textview */
        homeViewModel.getTodaysExpenseList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Expenses>>() {
            @Override
            public void onChanged(ArrayList<Expenses> expenses) {
                if(expenses.isEmpty()){
                    emptyRecyclerView.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE).edit();
                    editor.remove("expensesList");
                    editor.commit();
                }
            }
        });

        /* setting visibility and list of expenses list */
        if(homeViewModel.getTodaysExpenseList().getValue() != null){
            if(!homeViewModel.getTodaysExpenseList().getValue().isEmpty()){
                expensesList = homeViewModel.getTodaysExpenseList().getValue();
                emptyRecyclerView.setVisibility(View.INVISIBLE);
                setByViewModel = true;
            }
        }

        /* animation of card view */
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPropertyAnimator animator = spendingCardView.animate();
                animator.translationX(spendingCardView.getWidth());
                animator.setDuration(500);
                animator.start();
            }
        });

        /* touch to swipe functionality */
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


        /* updates all the needed values and info into homeView and updates chart */
        spend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!spendingAmount.getText().toString().isEmpty()) {
                    if (spendingAmount.getText().toString().matches("(\\d*\\.?\\d{0,2})")) {
                        homeViewModel.increaseTodaysSpending(Float.parseFloat(spendingAmount.getText().toString()));
                        homeViewModel.decreaseTodaysRemainingFunds(Float.parseFloat(spendingAmount.getText().toString()));

                        expensesList.add(new Expenses(expenseReasonSpinner.getSelectedItem().toString(), Float.parseFloat(spendingAmount.getText().toString())));
                        myAdapter.notifyDataSetChanged();
                        emptyRecyclerView.setVisibility(View.INVISIBLE);

                        homeViewModel.setExpenseList(expensesList);

                        if(homeViewModel.getTodaysRemainingFunds().getValue() < 0){
                            homeViewModel.increaseTodaysOverage(Math.abs(homeViewModel.getTodaysRemainingFunds().getValue()));
                            homeViewModel.setTodaysRemainingFunds(0);
                            homeViewModel.setTodaysSpendings(homeViewModel.getExpenseMax().getValue() - homeViewModel.getTodaysOverage().getValue());
                        }


                        final List<PieEntry> pieEntries2 = new ArrayList<>();
                        pieEntries2.add(new PieEntry(homeViewModel.getTodaysRemainingFunds().getValue(), "Left"));
                        pieEntries2.add(new PieEntry(homeViewModel.getTodaysSpending().getValue(), "Spent"));
                        pieEntries2.add(new PieEntry(homeViewModel.getTodaysOverage().getValue(), "Overage"));


                        dailyBudgetChart.clearAnimation();

                        dataSet = new PieDataSet(pieEntries2, ".");
                        dataSet.setColors(colors);
                        data = new PieData(dataSet);
                        dailyBudgetChart.setData(data);
                        dailyBudgetChart.notifyDataSetChanged();
                        dailyBudgetChart.invalidate();

                    } else {
                        Toast.makeText(getActivity(), "Incorrect Format: Example: XX.XX", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getActivity(), "Incorrect Format: Example: XX.XX", Toast.LENGTH_LONG).show();
                }
            }
        });

        /* alert dialog to confirm removal of selected item */
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

        /* set up for recycler view */
        dailySpendingRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        dailySpendingRecyclerView.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(getContext(), expensesList, listener);
        dailySpendingRecyclerView.setAdapter(myAdapter);

        /* pulls expenses list from the shared preferences if not already set */
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE);
        if(!setByViewModel) {
            Gson gson = new Gson();

            if (!sharedPreferences.getString("expensesList", "0").equals("0")) {
                String storedHashMapString = sharedPreferences.getString("expensesList", "0");
                java.lang.reflect.Type type = new TypeToken<HashMap<String, Expenses>>() {
                }.getType();
                HashMap<String, Expenses> expensesHashMap = gson.fromJson(storedHashMapString, type);

                for (int i = 0; i < expensesHashMap.size(); i++) {
                    expensesList.add(expensesHashMap.get(String.valueOf(i)));
                }
                myAdapter.notifyDataSetChanged();
                emptyRecyclerView.setVisibility(View.INVISIBLE);
            }
        }


        savingsProgress.setMax(homeViewModel.getDesiredSavings().getValue().intValue());
        savingsProgress.setProgress((int) sharedPreferences.getFloat("currentSavings", 0f), true);
        savingsProgress.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        savingsProgress.setScaleY(4f);


        savingGoal.setText(String.valueOf(homeViewModel.getDesiredSavings().getValue().intValue()));
        currentSavingsTotal.setText(String.valueOf(homeViewModel.getCurrentSavings().getValue().intValue()));





        return root;
    }

    /* used to save all the data needed between application launches */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        SharedPreferences mPrefs = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPrefs.edit();

        HashMap<String,Expenses> expenses = new HashMap<>();
        for(int i = 0; i < expensesList.size(); i++){
            expenses.put(String.valueOf(i), expensesList.get(i));
        }
        Gson gson = new Gson();
        String hashMapString = gson.toJson(expenses);

        editor.remove("expensesList");
        editor.putString("expensesList", hashMapString);

        editor.putString("todaysDate", new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()));
        editor.putFloat("todaysRemainingFunds", homeViewModel.getTodaysRemainingFunds().getValue());
        editor.putFloat("todaysSpending", homeViewModel.getTodaysSpending().getValue());
        editor.putFloat("todaysOverage", homeViewModel.getTodaysOverage().getValue());
        editor.commit();

    }

    /* callback for clicking on recycler view items */
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    /* removal of item in recycler view by position */
    public void remove(int position){

        if(homeViewModel.getTodaysOverage().getValue() > 0){
            float temp = homeViewModel.getTodaysOverage().getValue();
            temp -= expensesList.get(position).getAmount();

            if(temp < 0){
                homeViewModel.setTodaysOverage(0);
                homeViewModel.increaseTodaysRemainingFunds(Math.abs(temp));
                homeViewModel.setTodaysSpendings(homeViewModel.getExpenseMax().getValue() - homeViewModel.getTodaysRemainingFunds().getValue());
            }
            else{
                homeViewModel.setTodaysOverage(temp);
                homeViewModel.setTodaysSpendings(homeViewModel.getExpenseMax().getValue() - homeViewModel.getTodaysOverage().getValue());
            }
        }else {
            homeViewModel.increaseTodaysRemainingFunds(expensesList.get(position).getAmount());
            homeViewModel.decreaseTodaysSpending(expensesList.get(position).getAmount());

        }
        expensesList.remove(position);
        myAdapter.notifyDataSetChanged();
        homeViewModel.setExpenseList(expensesList);

        final List<PieEntry> pieEntries2 = new ArrayList<>();
        pieEntries2.add(new PieEntry(homeViewModel.getTodaysRemainingFunds().getValue(), "Left"));
        pieEntries2.add(new PieEntry(homeViewModel.getTodaysSpending().getValue(), "Spent"));
        pieEntries2.add(new PieEntry(homeViewModel.getTodaysOverage().getValue(), "Overage"));

        dailyBudgetChart.clearAnimation();

        dataSet = new PieDataSet(pieEntries2, ".");
        dataSet.setColors(colors);
        data = new PieData(dataSet);
        dailyBudgetChart.setData(data);
        dailyBudgetChart.notifyDataSetChanged();
        dailyBudgetChart.invalidate();
    }

}
