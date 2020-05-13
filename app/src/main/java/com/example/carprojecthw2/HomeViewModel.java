package com.example.carprojecthw2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Float> dailyExpenseMax = new MutableLiveData<>();
    private MutableLiveData<Float> todaysSpendings = new MutableLiveData<>();
    private MutableLiveData<Float> desiredSavings = new MutableLiveData<>();
    private MutableLiveData<Float> currentSavings = new MutableLiveData<>();
    // currently unsure how i want to use this


    public LiveData<Float> getExpenseMax(){ return dailyExpenseMax; }
    public LiveData<Float> getTodaysSpending(){ return  todaysSpendings; }
    public LiveData<Float> getDesiredSavings() { return desiredSavings; }
    public LiveData<Float> getCurrentSavings() { return currentSavings; }

    public void increaseTodaysSpending(float x){ todaysSpendings.setValue(todaysSpendings.getValue() + x);}
    public void decreaseTodaysSpending(float x){ todaysSpendings.setValue(todaysSpendings.getValue() - x);}

    public void increaseCurrentSavings(float x){ currentSavings.setValue(currentSavings.getValue() + x);}
    public void decreaseCurrentSavings(float x){ currentSavings.setValue(currentSavings.getValue() - x);}

    public void setDailyExpenseMax(float x){ dailyExpenseMax.setValue(x);}
    public void setTodaysSpendings(float x){ todaysSpendings.setValue(x);}
    public void setDesiredSavings(float x){ desiredSavings.setValue(x);}
    public void setCurrentSavings(float x){ currentSavings.setValue(x);}
}
