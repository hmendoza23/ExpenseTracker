package com.example.carprojecthw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import java.util.regex.Pattern;

/**
 * this class handles the functionality of the signup page
 */
public class SignupFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        final View root = inflater.inflate(R.layout.fragment_signup, container, false);

        /* Connect the XML elements to the java code **/
        final EditText email = root.findViewById(R.id.email);
        final TextView emailTxt = root.findViewById(R.id.emailTxt);
        final EditText password = root.findViewById(R.id.PasswordSignUp);
        final TextView passwordTxt = root.findViewById(R.id.passwordTxt);
        final EditText rePassword = root.findViewById(R.id.rePassword);
        final TextView rePasswordTxt = root.findViewById(R.id.rePasswordTxt);
        Button signUp = root.findViewById(R.id.loginBtn);


        /* set functionality of the sign up button **/
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean goodToSubmit = true;
                String error = new String();
                int errorCount = 0;

                /* Gets the text typed in the edit text fields **/
                String user = email.getText().toString();
                String pass = password.getText().toString();
                String rePass = rePassword.getText().toString();

                emailTxt.setTextColor(Color.BLACK);
                passwordTxt.setTextColor(Color.BLACK);
                rePasswordTxt.setTextColor(Color.BLACK);

                /* Simple regex check of the email entered by user and the passwords entered **/
                if(!Pattern.matches("[\\w | \\. ]+\\@[\\w | \\. ]+", user)) {
                    emailTxt.setTextColor(Color.RED);
                    goodToSubmit = false;
                    errorCount++;
                    error = "Email format incorrect";
                }
                if(!(user.contains("@csulb.edu") || user.contains("@student.csulb.edu"))) {
                    emailTxt.setTextColor(Color.RED);
                    goodToSubmit = false;
                    errorCount++;
                    error = "Email must be a CSULB email";
                }
                if(!pass.equals(rePass)){
                    passwordTxt.setTextColor(Color.RED);
                    goodToSubmit = false;
                    errorCount++;
                    error = "Passwords do not match";
                }

                if(errorCount == 1) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();

                }else if(errorCount > 1) {
                    Toast.makeText(getContext(), "Correct red fields", Toast.LENGTH_LONG).show();
                }

                if(goodToSubmit){
                    /* closes the keyboard **/
                    password.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    /* with shared preferences new data is then added**/
                    SharedPreferences db = getActivity().getSharedPreferences("database", Context.MODE_PRIVATE);
                    SharedPreferences.Editor dbEditor = db.edit();
                    dbEditor.putString(user, pass);
                    dbEditor.commit();

                    /* takes us to the login page**/
                    Intent reStart = new Intent(getActivity(), MainActivity.class);
                    startActivity(reStart);
                    getActivity().finish();

                }
            }
        });

        return root;
    }
}
