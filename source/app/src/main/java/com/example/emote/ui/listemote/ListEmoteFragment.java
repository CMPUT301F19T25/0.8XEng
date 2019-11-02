package com.example.emote.ui.listemote;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListEmoteFragment extends Fragment {

    private static final String TAG = "ListEmoteFragment";
    private ListEmoteViewModel listEmoteViewModel;

    private ArrayList<EmotionEvent> emoteDataList;
    private EmoteListAdapter emoteAdapter;
    private ListView emoteListView;
    private Spinner spinner;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listEmoteViewModel =
                ViewModelProviders.of(this).get(ListEmoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_emote, container, false);

        emoteListView = root.findViewById(R.id.emote_list_view);
        spinner = root.findViewById(R.id.spinner);

        String emotions[] = new String[Emotion.values().length];
        for(int i = 0; i<Emotion.values().length;i++){
            int identifier = getResources().getIdentifier(Emotion.values()[i].toString(),"string", getContext().getPackageName());
            emotions[i] = getContext().getResources().getString(identifier);

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, emotions);
        spinner.setAdapter(adapter);


        emoteDataList = new ArrayList<>();
        emoteAdapter = new EmoteListAdapter(getContext(), emoteDataList);
        emoteListView.setAdapter(emoteAdapter);
        listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList);



        return root;
    }
}