package com.example.emote.ui.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.emote.EmoteApplication;

/**
 * ViewModel for the Friends' view, allowing you to see your friends
 */
public class FriendsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    CountingIdlingResource idlingResource;

    public FriendsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Friends");
        idlingResource = EmoteApplication.getIdlingResource();
    }

    public LiveData<String> getText() { return mText; }
}
