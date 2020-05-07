package com.example.carprojecthw2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class BigScreen extends Fragment {

    //Layoutinflater is used to instantiate layout XML file into its corresponding view objects
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_big_screen, container, false);

        HomeFragment home = new HomeFragment();
        CarDetailsFragment deets = new CarDetailsFragment();

        /* Places both fragments into one view */
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.home_container, home);
        transaction.replace(R.id.detail_container, deets);
        transaction.commit();

        return root;
    }
}
