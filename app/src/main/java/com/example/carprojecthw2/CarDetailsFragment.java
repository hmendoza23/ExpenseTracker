package com.example.carprojecthw2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CarDetailsFragment extends Fragment {

    private CarDetailsViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                              ViewGroup container, Bundle savedInstanceState) {

        /* gets the correct view model */
        viewModel = new ViewModelProvider(requireActivity()).get(CarDetailsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_car_details, container, false);

        /* Ui element set up */
        final TextView makeModel = root.findViewById(R.id.make_model);
        final TextView price = root.findViewById(R.id.price);
        final ImageView picture = root.findViewById(R.id.carPic);
        final TextView description = root.findViewById(R.id.description);
        final TextView lastUpdate = root.findViewById(R.id.last_update);

        /* On change listener so ui elements are updated when underlying data changes */
        viewModel.getMakeModel().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                makeModel.setText( s);
            }
        });
        viewModel.getPrice().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                price.setText(s);
            }
        });
        viewModel.getPicture().observe(getViewLifecycleOwner(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap s) {
                picture.setImageBitmap(s);
            }
        });
        viewModel.getDescription().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                description.setText(s);
            }
        });
        viewModel.getLastUpdate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                String builder = "Last Update: " + s;
                lastUpdate.setText( builder);
            }
        });

        return root;
    }



}
