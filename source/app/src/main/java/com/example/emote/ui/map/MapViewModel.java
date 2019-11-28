package com.example.emote.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.emote.EmoteApplication;

/**
 * The View Model for the Map Fragment
 */
public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     * Constructor for the MapViewModel
     */
    public MapViewModel(){
        // Create idling resource for map ui tests
        EmoteApplication.setIdlingResource( new CountingIdlingResource("map"));
    }

    /**
     * Get the text for the MapViewModel
     * @return mText, the text for the MapViewModel
     */
    public LiveData<String> getText() { return mText; }
}
