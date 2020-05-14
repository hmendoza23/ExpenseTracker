package com.example.carprojecthw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class Settings extends Fragment {
    private Button changeEmail;
    private Button changePassword;
    private Button resetData;
    private Button logoutButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);

        changeEmail = root.findViewById(R.id.update_email_button);
        changePassword = root.findViewById(R.id.change_password_button);
        resetData = root.findViewById(R.id.reset_data);
        logoutButton = root.findViewById(R.id.logout);

        changeEmail.setOnClickListener(new View.OnClickListener() {
            SharedPreferences db = getActivity().getSharedPreferences("database", Context.MODE_PRIVATE);
            SharedPreferences.Editor dbEditor = db.edit();
            @Override
            public void onClick(View v) {
                final CardView cardView = findViewById(R.id.card_view);

            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        resetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText temp;
                final int[] txtId = new int[] {
                        R.id.annualSalary,
                        R.id.expenseTxt,
                        R.id.dailyExpense,
                        R.id.savingTxt,
                        R.id.desiredSavings,
                        R.id.calcSalaryChangeable,
                        R.id.calcExpenseChangeable,
                        R.id.calcWantedSavingChangeable,
                        R.id.calcExpectedSavingChangeable
                };
                for (int i = 0; i < txtId.length; i++){
                    temp = (EditText) getView().findViewById(txtId[i]);
                    temp.setText(null);
                }

            }
        });


        logoutButton.setOnClickListener(new View.OnClickListener() {
            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            final SharedPreferences.Editor editor = mPrefs.edit();

            @Override
            public void onClick(View v) {
                editor.remove("loggedIn");
                editor.putBoolean("loggedIn", false);
                editor.apply();
                Intent reStart = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(reStart);
                getActivity().finish();
            }

        });


    // Inflate the layout for this fragment
        return root;
    }
}
