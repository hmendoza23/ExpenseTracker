package com.example.carprojecthw2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        MenuItem logOut = menu.findItem(R.id.logout);
        MenuItem Budget = menu.findItem(R.id.budget_frag);
        MenuItem graph = menu.findItem(R.id.nav_graphs);


        /* All stuff to check if the user is already logged in and what/what not to show the user depending on that */
        Boolean isLoggedIn;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = mPrefs.edit();

        if(mPrefs.getBoolean("loggedIn", false)){
            home.setVisible(true);
            login.setVisible(false);
            signUp.setVisible(false);
            settings.setVisible(true);
            graph.setVisible(true);
            String user = mPrefs.getString("User", null);
            userTxt.setText(user);
            isLoggedIn = true;
        }
        else{
            home.setVisible(false);
            login.setVisible(true);
            signUp.setVisible(true);
            settings.setVisible(false);
            graph.setVisible(false);
            logOut.setVisible(false);
            isLoggedIn = false;
        }



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.mobile_navigation);
        navController.setGraph(navGraph);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login,R.id.nav_signup, R.id.nav_settings,R.id.nav_graphs, R.id.budget_frag, R.id.logout)
                .setDrawerLayout(drawer)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* Navigates to the proper place depending on screen size and login status */
        if(isLoggedIn)
            navController.navigate(R.id.nav_home);
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
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
