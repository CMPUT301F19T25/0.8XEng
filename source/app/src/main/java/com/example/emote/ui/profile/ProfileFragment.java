package com.example.emote.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.EmoteApplication;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;

import static com.example.emote.FireStoreHandler.EMOTE_COLLECTION;
import static java.lang.Long.MIN_VALUE;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;

    private TextView usernameText;
    private TextView currentmoodText;

    private FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
    private FirebaseFirestore db = fsh.getFireStoreDBReference();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_profile);
        profileViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        usernameText = root.findViewById(R.id.profile_username);
        currentmoodText = root.findViewById(R.id.profile_current_mood);

        usernameText.setText(fsh.getUsername());
        db.collection(EMOTE_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Date max_date = new Date(MIN_VALUE);
                            EmotionEvent recent_emote = null;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                EmotionEvent current_emote = document.toObject(EmotionEvent.class);
                                if (current_emote.getUsername().equals(EmoteApplication.getUsername()) && (max_date.compareTo(current_emote.getDate()) <= 0)) {
                                    recent_emote = current_emote;
                                    max_date = document.toObject(EmotionEvent.class).getDate();
                                }
                            }
                            if (recent_emote == null) {
                                currentmoodText.setText("No emotions found");
                            } else {
                                currentmoodText.setText(recent_emote.getEmote().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return root;
    }
}