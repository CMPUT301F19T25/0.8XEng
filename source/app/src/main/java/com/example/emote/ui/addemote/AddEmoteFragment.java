package com.example.emote.ui.addemote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.Emotion;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import com.example.emote.EmotionEvent;
import com.example.emote.Situation;

import java.util.Date;

public class AddEmoteFragment extends Fragment {

    private static final String TAG = "AddEmoteFragment";
    private AddEmoteViewModel addEmoteViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addEmoteViewModel =
                ViewModelProviders.of(this).get(AddEmoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_emote, container, false);
        final TextView textView = root.findViewById(R.id.text_add_emote);
        addEmoteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        FireStoreHandler fsh = new FireStoreHandler("john123");
        EmotionEvent emoteEvent1 = new EmotionEvent(Emotion.HAPPY, Situation.FEW_PEOPLE, "Good food", new Date(System.currentTimeMillis()));
        EmotionEvent emoteEvent2 = new EmotionEvent(Emotion.ANNOYED, Situation.FEW_PEOPLE, "Dog died", new Date(System.currentTimeMillis()));
        EmotionEvent emoteEvent3 = new EmotionEvent(Emotion.EXCITED, Situation.FEW_PEOPLE, "Sleepy", new Date(System.currentTimeMillis()));
        fsh.addEmote(emoteEvent1);
        fsh.addEmote(emoteEvent2);
        fsh.addEmote(emoteEvent3);


        return root;
    }
}