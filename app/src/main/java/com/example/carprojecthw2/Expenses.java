package com.example.carprojecthw2;

import android.widget.Button;

/* simple class used for the expenses */
public class Expenses {

    private String reason;
    private float amount;
    private int position;

    public Expenses(String reason, float amount){
        this.reason = reason;
        this.amount = amount;
    }

    /* SETTERS */
    public void setReason(String reason){ this.reason = reason; }
    public void setAmount(float amount){ this.amount = amount; }
    public void setPosition(int position) { this.position = position; }

    /* GETTERS */
    public float getAmount() { return amount; }
    public String getReason() { return reason; }
    public int getPosition() { return position; }
}
