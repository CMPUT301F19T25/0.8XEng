package com.example.emote.ui.friends;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for the Friends' view, allowing you to see your friends
 */
public class FriendsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    /**
     * Constructor for the FriendsViewModel
     */
    public FriendsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Friends");
    }

    /**
     * get the text for the view model
     * @return the mtext
     */
    public LiveData<String> getText() { return mText; }
}
