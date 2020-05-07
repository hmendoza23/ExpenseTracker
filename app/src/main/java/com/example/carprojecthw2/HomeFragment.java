package com.example.carprojecthw2;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    private CarDetailsViewModel viewModel;

    private static String urlMake = "https://thawing-beach-68207.herokuapp.com/carmakes";
    private static String urlModel = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";
    private static String urlMakeModelList = "https://thawing-beach-68207.herokuapp.com/cars/";
    private static String urlDetails = "https://thawing-beach-68207.herokuapp.com/cars/";

    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();

    private Spinner carMake;
    private Spinner carModel;
    private RecyclerView carList;

    private HashMap<String, Integer> carStuff = new HashMap<>();
    private ArrayList<Integer> carIDs = new ArrayList<>();
    private ArrayList<String> carStringInfo = new ArrayList<>();
    private HashMap<String, Integer> modelName_ID = new HashMap<>();
    private MyAdapter myAdapter;

    private int currentMakeID;
    private int currentModelID;

    private View previousView;

    public HomeFragment(){}



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                ViewModelProviders.of(requireActivity()).get(CarDetailsViewModel.class);
        if(previousView != null)
            inflater.inflate(R.layout.fragment_home, (ViewGroup) previousView, false);
        else{
            View root = inflater.inflate(R.layout.fragment_home, container, false);
        carMake = root.findViewById(R.id.carMake);
        carModel = root.findViewById(R.id.carModel);

        final int screensize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        /* click listener so that we know which recycler view item was selected and which data to get for car details */
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {
                FetchCarInfo task5 = new FetchCarInfo(urlDetails, "details", carIDs.get(position), carMake.getSelectedItem().toString(), carModel.getSelectedItem().toString());
                task5.execute();
                if(screensize != Configuration.SCREENLAYOUT_SIZE_LARGE && screensize != Configuration.SCREENLAYOUT_SIZE_XLARGE){
                    NavController navController = Navigation.findNavController(getActivity().findViewById(R.id.nav_host_fragment));
                    navController.navigate(R.id.nav_car_details);
                }

            }
        };

        /* Ui element connections */
        carList = root.findViewById(R.id.carList);
        carList.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        carList.setLayoutManager(layoutManager);
        myAdapter = new MyAdapter(getContext(), carStringInfo, listener);
        carList.setAdapter(myAdapter);

        FetchCarInfo task = new FetchCarInfo(urlMake, "make", 0);
        task.execute();

        /* item select listeners so that the subsequent spinner can be updated */
        carMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FetchCarInfo task1 = new FetchCarInfo(urlModel, "model", carStuff.get(carMake.getItemAtPosition(position)));
                currentMakeID = carStuff.get(carMake.getItemAtPosition(position));
                System.out.println("current Make Id" + currentMakeID);
                task1.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                FetchCarInfo task2 = new FetchCarInfo(urlModel, "model", carStuff.get(carMake.getItemAtPosition(0)));
                currentMakeID = carStuff.get(carMake.getItemAtPosition(0));
                task2.execute();
            }
        });

        /* Listener so that recycler view can be updated */
        carModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentModelID = modelName_ID.get(carModel.getItemAtPosition(position).toString());

                FetchCarInfo task3 = new FetchCarInfo(urlMakeModelList,
                        "list",
                        currentMakeID,
                        currentModelID,
                        92603);
                task3.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentModelID = modelName_ID.get(carModel.getItemAtPosition(0).toString());

                FetchCarInfo task4 = new FetchCarInfo(urlMakeModelList,
                        "list",
                        currentMakeID,
                        currentModelID,
                        92603);
                task4.execute();
            }
        });
        /* saves previous view so that it doesnt have to reload everything when you click back
        * from car details on a smaller screen */
        previousView = root;
        return root;
        }
        return previousView;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class FetchCarInfo extends AsyncTask<Void, Void, Void> {

        private String url;
        private String urlType;
        private int id;
        private int makeID;
        private int modelID;
        private int zip;
        private int detailsID;
        private String makeName;
        private String modelName;
        ArrayList<String> makes = new ArrayList<>();
        ArrayList<String> models = new ArrayList<>();

        /* Differenct constructors for different needs */
        public FetchCarInfo(String url, String urlType, int id){
            this.url = url;
            this.urlType = urlType;
            this.id = id;
        }

        public FetchCarInfo(String url, String urlType, int make, int model, int zip){
            this.url = url;
            this.urlType = urlType;
            this.makeID = make;
            this.modelID = model;
            this.zip = zip;
        }

        public FetchCarInfo(String url, String urlType, int detailsID, String makeName, String modelName){
            this.url = url;
            this.urlType = urlType;
            this.detailsID = detailsID;
            this.makeName = makeName;
            this.modelName = modelName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = new String();
            String buildUrl;

            // Making a request to url and getting response
            /* Switches are used so that we can decide what exactly needs to be done */
            switch (urlType){
                case "make":
                    jsonStr = sh.makeServiceCall(url);
                    break;

                case "model":
                    buildUrl = url + id;
                    jsonStr = sh.makeServiceCall(buildUrl);
                    break;

                case "list":
                    buildUrl = url + makeID + "/" + modelID + "/" + zip;
                    jsonStr = sh.makeServiceCall(buildUrl);
                    break;

                case "details":
                    buildUrl = url + detailsID;
                    jsonStr = sh.makeServiceCall(buildUrl);
                    break;

                default:
            }


            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                /* Switches are used so that we can decide what exactly needs to be done */
                switch (urlType){
                    case "make":
                        try {
                            JSONArray carInfo = new JSONArray(jsonStr);

                            for (int i = 0; i < carInfo.length(); i++) {
                                JSONObject c = carInfo.getJSONObject(i);

                                String tempMake = c.getString("vehicle_make");
                                Integer tempID = c.getInt("id");

                                makes.add(tempMake);
                                carStuff.put(tempMake,tempID);
                            }
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
                        }
                        break;

                    case "model":
                        try{
                            JSONArray carInfo = new JSONArray(jsonStr);
                            System.out.println(jsonStr);
                            for(int i = 0; i < carInfo.length(); i++){
                                JSONObject c = carInfo.getJSONObject(i);

                                String temp1 = c.getString("model");
                                int temp2 = c.getInt("id");

                                models.add(temp1);
                                System.out.println("temp1 = " + temp1);
                                System.out.println("temp2 = " + temp2);
                                modelName_ID.put(temp1, temp2);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "list":
                        carStringInfo.clear();
                        carIDs.clear();
                        try{
                            JSONObject thing = new JSONObject(jsonStr);
                            JSONArray carInfo = thing.getJSONArray("lists");

                            for(int i = 0; i < carInfo.length(); i++){
                                JSONObject c = carInfo.getJSONObject(i);
                                String tempInfo = (i+1) + ") " + c.getString("vehicle_make") + " " + c.getString("model");
                                System.out.println(tempInfo);
                                carStringInfo.add(tempInfo);
                                carIDs.add(c.getInt("id"));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;

                    case "details":
                        try {
                            final JSONObject deets;// = new JSONObject(jsonStr);
                            JSONArray details = new JSONArray(jsonStr);
                            deets = details.getJSONObject(0);
                            String makeModel = makeName + "-" + modelName;

                            viewModel.postMakeModel(makeModel);
                            viewModel.postPrice(deets.getString("price"));

                            try {
                                URL pictureURL = new URL(deets.getString("image_url"));
                                HttpURLConnection connection = (HttpURLConnection) pictureURL.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                Bitmap picture = BitmapFactory.decodeStream(input);
                                viewModel.postPicture(picture);
                            }catch (Exception e){
                                System.out.println("damn it");
                            }

                            viewModel.postDescription(deets.getString("veh_description"));
                            viewModel.postLastUpdate(deets.getString("updated_at"));

                            }catch (JSONException e){
                            e.printStackTrace();
                        }
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            /* Switches are used so that we can decide what exactly needs to be done */
            switch (urlType){
                case "model":
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                            getContext(), android.R.layout.simple_spinner_item, models);
                    carModel.setAdapter(adapter1);
                break;
                case "make":
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                            getContext(), android.R.layout.simple_spinner_item, makes);
                    carMake.setAdapter(adapter2);
                    break;

                case "list":

                    myAdapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    }

    /* call back interface */
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

}
