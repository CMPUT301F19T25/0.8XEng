package com.example.emote.ui.listemote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ListEmoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ListEmoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is List Emote fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}