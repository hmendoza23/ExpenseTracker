package com.example.carprojecthw2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* set up for the tool bar at the top of the app */
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        /* Set up for the drawer style navigation */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        /* More Ui element set up */
        TextView userTxt = navigationView.getHeaderView(0).findViewById(R.id.userName);
        Menu menu = navigationView.getMenu();
        MenuItem home = menu.findItem(R.id.nav_home);
        MenuItem login = menu.findItem(R.id.nav_login);
        MenuItem signUp = menu.findItem(R.id.nav_signup);
        MenuItem settings = menu.findItem(R.id.nav_settings);
        //MenuItem logOut = menu.findItem(R.id.logout);
        MenuItem Budget = menu.findItem(R.id.budget_frag);



        /* All stuff to check if the user is already logged in and what/what not to show the user depending on that */
        Boolean isLoggedIn;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = mPrefs.edit();

        if(mPrefs.getBoolean("loggedIn", false)){
            home.setVisible(true);
            login.setVisible(false);
            signUp.setVisible(false);
            settings.setVisible(true);

            String user = mPrefs.getString("User", null);
            userTxt.setText(user);
            isLoggedIn = true;
        }
        else{
            home.setVisible(false);
            login.setVisible(true);
            signUp.setVisible(true);
            settings.setVisible(false);

            Budget.setVisible(false);
            isLoggedIn = false;
        }


        SharedPreferences mPrefs2 = getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = mPrefs2.edit();
        SharedPreferences.Editor editor3 = getSharedPreferences("com.example.ExpenseTracker.budgetHistory", Context.MODE_PRIVATE).edit();

        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());

        if(mPrefs2.getString("todaysDate", "00000000").equals(today)){
            homeViewModel.setTodaysRemainingFunds(mPrefs2.getFloat("todaysRemainingFunds", 0f));
            homeViewModel.setTodaysSpendings(mPrefs2.getFloat("todaysSpending", 0f));
            homeViewModel.setTodaysOverage(mPrefs2.getFloat("todaysOverage", 0f));
            homeViewModel.setDesiredSavings(mPrefs2.getFloat("desiredSavings", 0f));
            homeViewModel.setCurrentSavings(mPrefs2.getFloat("currentSavings", 0));
        }
        else{
            HashMap<String, String> history = new HashMap<>();
            history.put("date", mPrefs2.getString("todaysDate", "000000"));
            history.put("remainingFunds", String.valueOf(mPrefs2.getFloat("todaysRemainingFunds", 0f)));
            history.put("spending", String.valueOf(mPrefs2.getFloat("todaysSpending", 0f)));
            history.put("overage", String.valueOf(mPrefs2.getFloat("todaysOverage", 0f)));

            Gson gson = new Gson();
            String hashMapString = gson.toJson(history);

            editor3.putString(mPrefs2.getString("todaysDate", "000000"), hashMapString);
            editor3.commit();


            float currentSavings = mPrefs2.getFloat("currentSavings", 0f);
            if(mPrefs2.getFloat("todaysOverage", 0) > 0){
                currentSavings -= mPrefs2.getFloat("todaysOverage", 0);
            }else{
                currentSavings += mPrefs2.getFloat("todaysRemainingFunds", 0f);
            }

            if(currentSavings < 0){
                homeViewModel.setDailyExpenseMax((mPrefs2.getFloat("annualSalary",0) + currentSavings)/365);
                homeViewModel.setDesiredSavings(0);
            }

            editor2.remove("todaysRemainingFunds");
            editor2.remove("todaysSpending");
            editor2.remove("todaysOverage");
            editor2.putString("todaysDate", today);
            editor2.remove("currentSavings");
            editor2.putFloat("currentSavings", currentSavings);
            editor2.commit();

            homeViewModel.setTodaysRemainingFunds(mPrefs2.getFloat("dailyExpenseMax", 0f));
            homeViewModel.setTodaysSpendings(0f);
            homeViewModel.setTodaysOverage(0f);
            homeViewModel.setCurrentSavings(currentSavings);
            homeViewModel.setDesiredSavings(mPrefs2.getFloat("desiredSavings", 0f));
        }



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
        navController.setGraph(navGraph);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login,R.id.nav_signup, R.id.nav_settings, R.id.budget_frag, R.id.logout)
                .setDrawerLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* Navigates to the proper place depending on screen size and login status */
        if(isLoggedIn) {
            SharedPreferences mPrefs3 = getSharedPreferences("com.example.ExpenseTracker.budgetData", Context.MODE_PRIVATE);
            if(mPrefs2.getBoolean("isBudgetSetup", false))
                navController.navigate(R.id.nav_home);
            else
                navController.navigate(R.id.budget_frag);
        }
        else
            navController.navigate(R.id.nav_login);


        /**
         *  logOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
         *             @Override
         *             public boolean onMenuItemClick(MenuItem item) {
         *                 editor.remove("loggedIn");
         *                 editor.putBoolean("loggedIn", false);
         *                 editor.apply();
         *                 Intent reStart = new Intent(getApplicationContext(), MainActivity.class);
         *                 startActivity(reStart);
         *                 MainActivity.this.finish();
         *                 return false;
         *             }
         *         });
         */
        /* log out button functionality*/



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_help:
                Toast.makeText(this, "Enter your salary, daily expenses and desired savings to start budgeting", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_about:
                Toast.makeText(this, "An app to help you budget!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
