package com.example.carprojecthw2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


/** This class is needed for the implementation of the list of classes in the current schedule fragment
 * TEXT VIEW BELOW WILL BE REPLACED WITH A CUSTOM VIEW **/
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<String> carList = new ArrayList<>();
    private Context context;
    private HomeFragment.RecyclerViewClickListener clickListener;
    /* Provide a reference to the views for each data item
       Complex data items may need more than one view per item, and
       you provide access to all the views for a data item in a view holder  */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView carName;
        private int position;
        private HomeFragment.RecyclerViewClickListener mListener;


        public MyViewHolder(View v, HomeFragment.RecyclerViewClickListener listener) {
            super(v);

            carName = v.findViewById(R.id.name);
            mListener = listener;
            carName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            mListener.onClick(view, getAdapterPosition());
        }

        public void setCarName(String carName) {
            this.carName.setText(carName);
        }
        public void setPosition(int position){this.position = position;}

    }

    /* Provide a suitable constructor (depends on the kind of dataset)*/
    public MyAdapter(Context context, ArrayList<String> carList, HomeFragment.RecyclerViewClickListener listener) {
        this.context = context;
        this.carList = carList;
        clickListener = listener;
    }

    /* Create new views (invoked by the layout manager)*/
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        /* create a new view*/
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()) ;
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(view, clickListener);
        return vh;
    }

    /* Replace the contents of a view (invoked by the layout manager) */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String car = carList.get(position);

        holder.setCarName(car);
        holder.setPosition(position);

    }

    /* Return the size of your dataset (invoked by the layout manager) */
    @Override
    public int getItemCount() {
        return carList.size();
    }
}

