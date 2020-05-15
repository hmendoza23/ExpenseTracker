package com.example.carprojecthw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;


/**
 * This login fragment class handles the functionality of a user login
 * it takes in an email and password and checks if their credentials exist in shared preferences
 */
public class LoginFragment extends Fragment {

    /**
     * this oncreateview is called and we can assign any view variables and do any graphical initialisations
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return expected to return a view from this method
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        final View root = inflater.inflate(R.layout.fragment_login, container, false);


        final EditText username = root.findViewById(R.id.username);
        final TextView userTxt = root.findViewById(R.id.userTxt);
        final EditText password = root.findViewById(R.id.password);
        final TextView passTxt = root.findViewById(R.id.passTxt);

        final HashMap dictionary = new HashMap<>();
        SharedPreferences db = getActivity().getSharedPreferences("database", Context.MODE_PRIVATE);
        dictionary.putAll(db.getAll());

        SharedPreferences loggedIn = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editorLog = loggedIn.edit();

        Button login = root.findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            /**
             * this onClick method is for when the login button is CLICKED.
             * Goes through user validation and checks if data exists
             * @param v
             */
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                System.out.println("Email: " + user);


                if(dictionary.containsKey(user)){
                    if(dictionary.get(user).toString().equals(pass)){
                        //System.out.println("prints password: " + dictionary.get(user).toString());

                        editorLog.putBoolean("loggedIn", true);
                        editorLog.commit();

                        Intent reStart = new Intent(getActivity(), MainActivity.class);
                        startActivity(reStart);
                        getActivity().finish();
                    }
                    else{
                        userTxt.setTextColor(Color.parseColor("#000000"));
                        passTxt.setTextColor(Color.parseColor("#f20505"));
                        password.clearComposingText();
                        Toast.makeText(getContext(), "Password Incorrect", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getContext(), "Sorry No Matching Emails", Toast.LENGTH_LONG).show();
                    userTxt.setTextColor(Color.parseColor("#f20505"));
                    passTxt.setTextColor(Color.parseColor("#f20505"));
                    username.clearComposingText();
                    password.clearComposingText();
                }
            }
        });

        return root;
    }
}
