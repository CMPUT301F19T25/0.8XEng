package com.example.emote.ui.listemote;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
    private ArrayAdapter<EmotionEvent> emoteAdapter;
    private ListView emoteListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listEmoteViewModel =
                ViewModelProviders.of(this).get(ListEmoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_emote, container, false);
        final TextView textView = root.findViewById(R.id.text_list_emote);
        listEmoteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        emoteListView = root.findViewById(R.id.emote_list_view);
        emoteDataList = new ArrayList<>();
        emoteAdapter = new EmoteListAdapter(getContext(), emoteDataList);
        emoteListView.setAdapter(emoteAdapter);

        FireStoreHandler fsh = new FireStoreHandler("john123");
        FirebaseFirestore db = fsh.getFireStoreDBReference();
        db.collection(FireStoreHandler.EMOTE_COLLECTION)
                .whereEqualTo(EmotionEvent.USERNAME_KEY, fsh.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<EmotionEvent> new_emotes;
                        if (task.isSuccessful()) {
                            emoteDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                emoteDataList.add(document.toObject(EmotionEvent.class));
                            }
                            emoteAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return root;
    }
}