package com.example.carprojecthw2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Float> dailyExpenseMax = new MutableLiveData<>();
    private MutableLiveData<Float> todaysRemainingFunds = new MutableLiveData<>();
    private MutableLiveData<Float> todaysSpendings = new MutableLiveData<>();
    private MutableLiveData<Float> desiredSavings = new MutableLiveData<>();
    private MutableLiveData<Float> currentSavings = new MutableLiveData<>();
    private MutableLiveData<Float> todaysOverage = new MutableLiveData<>();
    // currently unsure how i want to use this


    public LiveData<Float> getExpenseMax(){ return dailyExpenseMax; }
    public LiveData<Float> getTodaysSpending(){ return  todaysSpendings; }
    public LiveData<Float> getTodaysOverage(){ return todaysOverage; }
    public LiveData<Float> getDesiredSavings() { return desiredSavings; }
    public LiveData<Float> getCurrentSavings() { return currentSavings; }
    public LiveData<Float> getTodaysRemainingFunds(){ return  todaysRemainingFunds; }

    public void increaseTodaysSpending(float x){ todaysSpendings.setValue(todaysSpendings.getValue() + x);}
    public void decreaseTodaysSpending(float x){ todaysSpendings.setValue(todaysSpendings.getValue() - x);}

    public void increaseCurrentSavings(float x){ currentSavings.setValue(currentSavings.getValue() + x);}
    public void decreaseCurrentSavings(float x){ currentSavings.setValue(currentSavings.getValue() - x);}

    public void increaseTodaysOverage(float x){todaysOverage.setValue(todaysOverage.getValue() + x);}
    public void decreaseTodaysOverage(float x){todaysOverage.setValue(todaysOverage.getValue() - x);}

    public void increaseTodaysRemainingFunds(float x){todaysRemainingFunds.setValue(todaysRemainingFunds.getValue() + x);}
    public void decreaseTodaysRemainingFunds(float x){todaysRemainingFunds.setValue(todaysRemainingFunds.getValue() - x);}

    public void setDailyExpenseMax(float x){ dailyExpenseMax.setValue(x); }
    public void setTodaysSpendings(float x){ todaysSpendings.setValue(x); }
    public void setDesiredSavings(float x){ desiredSavings.setValue(x); }
    public void setCurrentSavings(float x){ currentSavings.setValue(x); }
    public void setTodaysOverage(float x) { todaysOverage.setValue(x); }
    public void setTodaysRemainingFunds(float x) { todaysRemainingFunds.setValue(x);}
}
