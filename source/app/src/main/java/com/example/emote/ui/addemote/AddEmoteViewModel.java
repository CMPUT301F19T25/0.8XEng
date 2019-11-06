package com.example.emote.ui.addemote;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emote.Emotion;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.example.emote.ui.listemote.EmoteListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AddEmoteViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private ArrayList<EmotionEvent> emoteDataList;

    public AddEmoteViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Add Emote fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}