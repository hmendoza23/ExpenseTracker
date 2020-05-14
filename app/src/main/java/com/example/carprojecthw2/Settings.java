package com.example.carprojecthw2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Pattern;


public class Settings extends Fragment {
    private Button changeEmail;
    private Button changePassword;
    private Button resetData;
    private Button logoutButton;

    private CardView changeEmailCardView;
    private TextView txtEmail;
    private TextView retrievedEmailTxt;
    private EditText newEmailEntered;
    private Button emailChangeBtn;



    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);

        changeEmail = root.findViewById(R.id.update_email_button);
        changePassword = root.findViewById(R.id.change_password_button);
        resetData = root.findViewById(R.id.reset_data);
        logoutButton = root.findViewById(R.id.logout);
        changeEmailCardView = root.findViewById(R.id.emailCardView);
        newEmailEntered = root.findViewById(R.id.newEmail);
        emailChangeBtn = root.findViewById(R.id.confirm_update_email_button);
        txtEmail = root.findViewById(R.id.emailTxt);
        retrievedEmailTxt = root.findViewById(R.id.retrievedEmail);


        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPropertyAnimator animator = changeEmailCardView.animate();
                animator.translationX(changeEmailCardView.getWidth());
                animator.setDuration(500);
                animator.start();
            }
        });

        final float x[] = new float[2];
        changeEmailCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x[0] = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        x[1] = event.getX();
                        if ((x[0] - x[1]) > 300) {
                            ViewPropertyAnimator animate = changeEmailCardView.animate();
                            animate.translationX(-changeEmailCardView.getWidth());
                            animate.setDuration(500);
                            animate.start();
                        }
                        break;

                    default:
                }
                return true;
            }


            public void onCLick(View v){
                String retEmail = retrievedEmailTxt.getText().toString();
            }


        });


        /* set functionality of the update email button inside the card view **/
        emailChangeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean goodToSubmit = true;
                int errorCount = 0;
                String error = new String();
                String newEmailString = newEmailEntered.getText().toString();

                if(!Pattern.matches("[\\w | \\. ]+\\@[\\w | \\. ]+", newEmailString)) {
                    txtEmail.setTextColor(Color.RED);
                    goodToSubmit = false;
                    errorCount++;
                    error = "Email format incorrect";
                }
                if(!(newEmailString.contains("@csulb.edu") || newEmailString.contains("@student.csulb.edu"))) {
                    txtEmail.setTextColor(Color.RED);
                    goodToSubmit = false;
                    errorCount++;
                    error = "Email must be a CSULB email";
                }

                if(errorCount == 1) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                }

                if(goodToSubmit){
                    String retEmail = retrievedEmailTxt.getText().toString();

                    /* closes the keyboard **/
                    newEmailEntered.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    final HashMap dictionary = new HashMap<>();
                    SharedPreferences db = getActivity().getSharedPreferences("database", Context.MODE_PRIVATE);
                    SharedPreferences.Editor dbEditor = db.edit();
                    dictionary.putAll(db.getAll());

                    if(dictionary.containsKey(retrievedEmailTxt.getText().toString())){
                        dbEditor.putString(newEmailString);
                        dbEditor.commit();
                        dbEditor.putString(user, pass);

                    }


                    Intent reStart = new Intent(getActivity(), MainActivity.class);
                    startActivity(reStart);
                    getActivity().finish();

                }
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
