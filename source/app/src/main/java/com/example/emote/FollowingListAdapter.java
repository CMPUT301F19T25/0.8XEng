package com.example.emote;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.emote.FireStoreHandler.EMOTE_COLLECTION;
import static java.lang.Long.MIN_VALUE;

public class FollowingListAdapter extends ArrayAdapter<String> {

    private static final String TAG = "FollowingListAdapter";
    private ArrayList<String> users;
    private Context context;

    private FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
    private FirebaseFirestore db = fsh.getFireStoreDBReference();

    public FollowingListAdapter(@NonNull Context context, ArrayList<String> users) {
        super(context, 0, users);
        this.users = users;
        this.context = context;
    }

    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_individual_following, parent, false);
        }

        TextView username = view.findViewById(R.id.following_username);
        TextView current_mood = view.findViewById(R.id.following_current_mood);
        TextView date = view.findViewById(R.id.following_current_date);

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
                                if (current_emote.getUsername().equals(users.get(position)) && (max_date.compareTo(current_emote.getDate()) <= 0)) {
                                    recent_emote = current_emote;
                                    max_date = document.toObject(EmotionEvent.class).getDate();
                                }
                            }
                            if (recent_emote == null) {
                                username.setText(users.get(position));
                                current_mood.setText("No emotions found");
                                date.setText("Not available");
                            } else {
                                username.setText(users.get(position));
                                current_mood.setText(recent_emote.getEmote().toString());
                                DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
                                date.setText(df.format(recent_emote.getDate()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return view;
    }
}
