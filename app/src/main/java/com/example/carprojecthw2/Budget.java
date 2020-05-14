package com.example.carprojecthw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class Budget extends Fragment {

    private TextView salaryTxt;
    private EditText annualSalary;
    private TextView calcSalaryTxt;
    private TextView calcSalaryChangeable;
    private TextView expenseTxt;
    private EditText dailyExpense;
    private TextView calcExpenseTxt;
    private TextView calcExpenseChangeable;
    private TextView savingsTxt;
    private EditText hopefulSaving;
    private TextView calcWantedSavingTxt;
    private TextView calcWantedSavingChangeable;
    private TextView calcExpectedSavingTxt;
    private TextView calcExpectedSavingChangeable;
    private Button calculate;
    private Button saveBudget;

    private HomeViewModel homeViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_budget, container, false);

        salaryTxt = root.findViewById(R.id.salaryTxt);
        annualSalary = root.findViewById(R.id.annualSalary);
        calcSalaryTxt = root.findViewById(R.id.calcSalaryTxt);
        calcSalaryChangeable = root.findViewById(R.id.calcSalaryChangeable);

        expenseTxt = root.findViewById(R.id.expenseTxt);
        dailyExpense = root.findViewById(R.id.dailyExpense);
        calcExpenseTxt = root.findViewById(R.id.calcExpenseTxt);
        calcExpenseChangeable = root.findViewById(R.id.calcExpenseChangeable);

        savingsTxt = root.findViewById(R.id.savingTxt);
        hopefulSaving = root.findViewById(R.id.desiredSavings);
        calcWantedSavingTxt = root.findViewById(R.id.calcWantedSavingTxt);
        calcWantedSavingChangeable = root.findViewById(R.id.calcWantedSavingChangeable);
        calcExpectedSavingTxt = root.findViewById(R.id.calcExpectedSavingTxt);
        calcExpectedSavingChangeable = root.findViewById(R.id.calcExpectedSavingChangeable);

        calculate = root.findViewById(R.id.budgetButton);
        saveBudget = root.findViewById(R.id.saveBudget);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()){
                    int salary = 0;
                    int expense = 0;
                    int savings = 0;

                    try {
                        salary = Integer.parseInt(annualSalary.getText().toString());
                        expense = Integer.parseInt(dailyExpense.getText().toString());
                        savings = Integer.parseInt(hopefulSaving.getText().toString());
                    }catch (NumberFormatException nfe){
                        System.out.println("Number format exception");
                        nfe.printStackTrace();
                    }

                    float dailyBudget = salary/365;
                    float annualSavingCalculated = (dailyBudget - expense)*365;

                    calcSalaryChangeable.setText(String.valueOf(salary));
                    calcExpenseChangeable.setText(String.valueOf(expense));
                    calcWantedSavingChangeable.setText(String.valueOf(savings));
                    calcExpectedSavingChangeable.setText(String.valueOf(annualSavingCalculated));

                    if(annualSavingCalculated < savings){
                        calcExpectedSavingChangeable.setTextColor(Color.RED);
                    }else{
                        calcExpectedSavingChangeable.setTextColor(Color.GREEN);
                    }
                }
            }
        });


        saveBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float dailyExpenseMax = Float.parseFloat(calcExpenseChangeable.getText().toString());
                float desiredSavings = Float.parseFloat(calcExpectedSavingChangeable.getText().toString());

                SharedPreferences mPrefs = getActivity().getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE);

                float todaysSpendings = mPrefs.getFloat("todaysSpendings", 0);
                float currentSavings = mPrefs.getFloat("currentSavings", 0);

                SharedPreferences.Editor prefEditor = mPrefs.edit();

                prefEditor.putFloat("dailyExpenseMax", dailyExpenseMax);
                prefEditor.putFloat("desiredSavings", desiredSavings);
                prefEditor.putBoolean("isBudgetSetup", true);
                prefEditor.commit();

                homeViewModel.setDailyExpenseMax(dailyExpenseMax);
                homeViewModel.setTodaysRemainingFunds(dailyExpenseMax);
                homeViewModel.setTodaysOverage(0);
                homeViewModel.setTodaysSpendings(todaysSpendings);
                homeViewModel.setDesiredSavings(desiredSavings);
                homeViewModel.setCurrentSavings(currentSavings);

                NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment));
                navController.navigate(R.id.nav_home);
            }
        });


        return root;
    }

    public boolean valid(){
        boolean isValid = true;

        if(!annualSalary.getText().toString().matches("\\d*")){
            Toast.makeText(getContext(),"Incorrect Salary Format", Toast.LENGTH_LONG).show();
            salaryTxt.setTextColor(Color.RED);
            isValid = false;
        }
        if(!dailyExpense.getText().toString().matches("\\d*")){
            Toast.makeText(getContext(),"Incorrect Expense Format", Toast.LENGTH_LONG).show();
            expenseTxt.setTextColor(Color.RED);
            isValid = false;
        }
        if(!hopefulSaving.getText().toString().matches("\\d*")){
            Toast.makeText(getContext(),"Incorrect Saving Format", Toast.LENGTH_LONG).show();
            savingsTxt.setTextColor(Color.RED);
            isValid = false;
        }

        return isValid;
    }
}
