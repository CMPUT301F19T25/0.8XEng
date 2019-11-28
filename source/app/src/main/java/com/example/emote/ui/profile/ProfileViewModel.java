package com.example.emote.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * The View Model for the Profile page
 */
public class ProfileViewModel extends ViewModel {


    private MutableLiveData<String> mText;

    /**
     * Constructor for the profile view model
     */
    public ProfileViewModel() {
        mText = new MutableLiveData<>();
    }

    /**
     * Get text for the profile view model
     * @return returns the text for the profile view model
     */
    public LiveData<String> getText() {
        return mText;
    }
}