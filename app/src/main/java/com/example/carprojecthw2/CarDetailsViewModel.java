package com.example.carprojecthw2;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/* View model for car details so data can be updated live and from other fragments */
public class CarDetailsViewModel extends ViewModel {

    private MutableLiveData<String> makeModel;
    private MutableLiveData<String> price;
    private MutableLiveData<String> description;
    private MutableLiveData<String> lastUpdate;
    private MutableLiveData<Bitmap> picture;

    public CarDetailsViewModel() {
        super();
        makeModel = new MutableLiveData<>();
        price = new MutableLiveData<>();
        description = new MutableLiveData<>();
        lastUpdate = new MutableLiveData<>();
        picture = new MutableLiveData<>();
    }

    /* SETTERS
    * using "postValue" so that values can be set from background threads */
    public void postMakeModel(String makeModel) { this.makeModel.postValue(makeModel); }
    public void postPrice(String price) { this.price.postValue(price); }
    public void postPicture(Bitmap picture) { this.picture.postValue(picture); }
    public void postDescription(String description) { this.description.postValue(description); }
    public void postLastUpdate(String lastUpdate) { this.lastUpdate.postValue(lastUpdate); }


    /* GETTERS */
    public LiveData<String> getMakeModel() { return makeModel; }
    public LiveData<String> getPrice() { return price; }
    public LiveData<Bitmap> getPicture() { return picture; }
    public LiveData<String> getDescription() { return description; }
    public LiveData<String> getLastUpdate() { return lastUpdate; }



}
