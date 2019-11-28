package com.example.emote.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The View Model for the Map Fragment
 */
public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     * Constructor for the MapViewModel
     */
    public MapViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("This is the Map fragment");
    }

    /**
     * Get the text for the MapViewModel
     * @return mText, the text for the MapViewModel
     */
    public LiveData<String> getText() { return mText; }
}
