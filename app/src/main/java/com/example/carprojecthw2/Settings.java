package com.example.carprojecthw2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Settings extends Fragment {
    private Button changeEmail;
    private Button changePassword;
    private Button resetData;
    private Button logoutButton;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);

        changeEmail = root.findViewById(R.id.update_email_button);
        changePassword = root.findViewById(R.id.change_password_button);
        resetData = root.findViewById(R.id.reset_data);
        logoutButton = root.findViewById(R.id.logout);

        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reStart = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(reStart);
                //Settings.this.finish();
            }
        });





    // Inflate the layout for this fragment
        return root;
    }
}
