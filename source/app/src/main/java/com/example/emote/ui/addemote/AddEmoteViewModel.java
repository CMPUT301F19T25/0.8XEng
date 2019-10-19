package com.example.emote.ui.addemote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddEmoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddEmoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Add Emote fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}