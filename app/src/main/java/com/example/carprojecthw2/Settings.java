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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * this class handles the functionalities in the settings page
 */

public class Settings extends Fragment {
    private Button changeEmail;
    private Button changePassword;
    private Button resetData;
    private Button logoutButton;
    private Button chgnPass;
    //private Button closeEmailChangeWindow;
    private EditText newPassTxt;
    private EditText confirmnewPass;
    private EditText passEmail;

    private CardView changeEmailCardView;
    private CardView changePasswordCardView;
    private TextView txtEmail;
    private EditText retrievedEmailTxt;
    private TextView txtNewpassTxt;
    private EditText passForEmailChange;
    private Button emailChangeBtn;


    /**
     * onCreateView sets up the graphical initialisations
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_settings, container, false);


        changeEmail = root.findViewById(R.id.update_email_button);
        changePassword = root.findViewById(R.id.change_password_button);
        resetData = root.findViewById(R.id.reset_data);
        logoutButton = root.findViewById(R.id.logout);
        changeEmailCardView = root.findViewById(R.id.emailCardView);
        passForEmailChange = root.findViewById(R.id.enteredPasswordforEmailChange);
        emailChangeBtn = root.findViewById(R.id.confirm_update_email_button);
        txtEmail = root.findViewById(R.id.emailTxt);
        retrievedEmailTxt = root.findViewById(R.id.retrievedEmail);
        chgnPass = root.findViewById(R.id.confirm_change_pass_button);
        newPassTxt = root.findViewById(R.id.newPass);
        changePasswordCardView = root.findViewById(R.id.passwordCardView);
        confirmnewPass = root.findViewById(R.id.newPassConfirm);
        txtNewpassTxt = root.findViewById(R.id.newPassConfirmTxt);
        passEmail = root.findViewById(R.id.passwordEmail);
        //closeEmailChangeWindow = root.findViewById(R.id.exitButtonForChangeEmail);

        /**
         * this method is for the change email button. once clicked with awaken the cardview for this button
         */
        changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPropertyAnimator animator = changeEmailCardView.animate();
                animator.translationX(changeEmailCardView.getWidth());
                animator.setDuration(500);
                animator.start();
            }

        });


        /**
         * this cardview is opened after change email button is pressed, it is animated by sliding sideways
         */
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

        });



        /* set functionality of the update email button inside the card view **/
        emailChangeBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                SharedPreferences db = getActivity().getSharedPreferences("database", Context.MODE_PRIVATE);
                SharedPreferences.Editor dbEditor = db.edit();

                boolean goodToSubmit = true;
                int errorCount = 0;
                String error = new String();
                String newEmailString = retrievedEmailTxt.getText().toString();
                String passemailchange = passForEmailChange.getText().toString();

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
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    final SharedPreferences.Editor editor = mPrefs.edit();

                    dbEditor.putString(newEmailString, passemailchange);
                    dbEditor.commit();

                    editor.remove("loggedIn");
                    editor.putBoolean("loggedIn", false);
                    editor.apply();
                    Toast.makeText(getContext(), "LOGIN WITH NEW EMAIL!", Toast.LENGTH_LONG).show();
                    Intent reStart = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(reStart);
                    getActivity().finish();

                    /* look in terminal to see the db printed out */
                    Iterator iter = db.getAll().entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry pair = (Map.Entry) iter.next();
                        //System.out.println("Should print emails only:" + pair.getKey());
                        System.out.println("Stuff in db: " + pair);
                    }

                }
            }
        });


        /**
         * this method is for the change password button. once clicked will awaken the cardview for this button
         */
        changePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ViewPropertyAnimator animator = changePasswordCardView.animate();
                animator.translationX(changePasswordCardView.getWidth());
                animator.setDuration(500);
                animator.start();
            }
        });


        /**
         * this card view is awaken when the button is clicked, slides in
         */
        final float x1[] = new float[2];
        changePasswordCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1[0] = event.getX();
                        break;

                    case MotionEvent.ACTION_UP:
                        x1[1] = event.getX();
                        if ((x1[0] - x1[1]) > 300) {
                            ViewPropertyAnimator animate = changePasswordCardView.animate();
                            animate.translationX(-changePasswordCardView.getWidth());
                            animate.setDuration(500);
                            animate.start();
                        }
                        break;

                    default:
                }
                return true;
            }


        });

        /**
         * this button is once the user clicks to change password inside the cardview
         */
        chgnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final HashMap dictionary = new HashMap<>();
                SharedPreferences db = getActivity().getSharedPreferences("database", Context.MODE_PRIVATE);
                SharedPreferences.Editor dbEditor = db.edit();
                dictionary.putAll(db.getAll());

                boolean goodToSubmit = true;
                int errorCount = 0;
                String error = new String();
                String currEmail = passEmail.getText().toString();
                String pass = newPassTxt.getText().toString();
                String rePass = confirmnewPass.getText().toString();

                if (!pass.equals(rePass)) {
                    txtNewpassTxt.setTextColor(Color.RED);
                    goodToSubmit = false;
                    errorCount++;
                    error = "Passwords do not match";
                }

                if (errorCount == 1) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                }

                if (goodToSubmit) {
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    final SharedPreferences.Editor editor = mPrefs.edit();

                    /* closes the keyboard **/
                    confirmnewPass.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    dbEditor.putString(currEmail, rePass);
                    dbEditor.commit();

                    editor.remove("loggedIn");
                    editor.putBoolean("loggedIn", false);
                    editor.apply();
                    Toast.makeText(getContext(), "LOGIN WITH NEW PASSWORD!", Toast.LENGTH_LONG).show();
                    Intent reStart = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(reStart);
                    getActivity().finish();

                    /* this prints in the terminal and shows that the password has been updated **/
                    Iterator iter = db.getAll().entrySet().iterator();
                    System.out.println("Printing database shared preferencecs");
                    while (iter.hasNext()) {
                        Map.Entry pair = (Map.Entry) iter.next();
                        //System.out.println("Should print emails only:" + pair.getKey());
                        System.out.println("Stuff in db: " + pair);
                    }

                }
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


        /**
         * this is to log the user out of the app, just passes a boolean from the shared preferences and finishes the activity
         */
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
