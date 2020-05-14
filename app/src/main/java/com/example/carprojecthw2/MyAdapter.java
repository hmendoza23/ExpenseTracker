package com.example.carprojecthw2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/** This class is needed for the implementation of the list of classes in the current schedule fragment
 * TEXT VIEW BELOW WILL BE REPLACED WITH A CUSTOM VIEW **/
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Expenses> expensesList = new ArrayList<>();
    private Context context;
    private HomeFragment.RecyclerViewClickListener clickListener;

    /* Provide a reference to the views for each data item
       Complex data items may need more than one view per item, and
       you provide access to all the views for a data item in a view holder  */
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView reason;
        private TextView amount;
        private ImageButton remove;
        private int position;
        private HomeFragment.RecyclerViewClickListener mListener;


        public MyViewHolder(View v, HomeFragment.RecyclerViewClickListener listener) {
            super(v);

            reason = v.findViewById(R.id.expenseReasonTxt);
            amount = v.findViewById(R.id.amountTxt_Rv);
            remove = v.findViewById(R.id.removeExpense);
            mListener = listener;

            remove.setOnClickListener(this);

        }

        /* Click listener callback interface for recognizing clicks each element of the
        * recycler view for schedule */
        @Override
        public void onClick(View view){
            mListener.onClick(view, getAdapterPosition());
        }

        /* setters for textviews in each row of the schedule recylcer view*/
        public void setAmount(float amount) { this.amount.setText(String.valueOf(amount)); }
        public void setReason(String reason) { this.reason.setText(reason); }


        public void setPosition(int position){this.position = position;}

    }

    /* Provide a suitable constructor (depends on the kind of dataset)*/
    public MyAdapter(Context context, ArrayList<Expenses> expensesList, HomeFragment.RecyclerViewClickListener listener) {
        this.context = context;
        this.expensesList = expensesList;
        clickListener = listener;
    }

    /* Provide a suitable constructor (depends on the kind of dataset)*/
    public MyAdapter(Context context, ArrayList<Expenses> expensesList) {
        this.context = context;
        this.expensesList = expensesList;
    }

    /* Create new views (invoked by the layout manager)*/
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        /* create a new view*/
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext()) ;
        View view = layoutInflater.inflate(R.layout.expense_item, parent, false);

        MyViewHolder vh = new MyViewHolder(view, clickListener);
        return vh;
    }

    /* Replace the contents of a view (invoked by the layout manager) */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Expenses expenses = expensesList.get(position);

        holder.setReason(expenses.getReason());
        holder.setAmount(expenses.getAmount());
        holder.setPosition(position);

    }

    /* Return the size of your dataset (invoked by the layout manager) */
    @Override
    public int getItemCount() {
        return expensesList.size();
    }

    public ArrayList<Expenses> getExpenses(){ return expensesList;}
}
