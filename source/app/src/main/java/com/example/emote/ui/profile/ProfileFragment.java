package com.example.emote.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.idling.CountingIdlingResource;

import com.example.emote.EmoteApplication;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.example.emote.FollowingListActivity;
import com.example.emote.LoginActivity;
import com.example.emote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import static com.example.emote.FireStoreHandler.EMOTE_COLLECTION;
import static com.example.emote.FireStoreHandler.FRIEND_COLLECTION;
import static java.lang.Long.MIN_VALUE;

/**
 * Fragment to display the user's profile.
 * Display's the user's username and the mood
 * of their latest mood event.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private ProfileViewModel profileViewModel;

    private TextView usernameText;
    private TextView currentmoodText;
    private TextView friendsText;
    private Button signoutButton;
    private Button showFriends;

    private FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
    private FirebaseFirestore db = fsh.getFireStoreDBReference();

    private CountingIdlingResource idlingResource = new CountingIdlingResource("profile");

    /**
     *
     * @param inflater LayoutInflater to inflate the fragment view
     * @param container Viewgroup container of fragment
     * @param savedInstanceState Bundle of the saved instance state
     * @return return the view for the profile fragment
     */
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
        friendsText = root.findViewById(R.id.profile_number_friends);
        showFriends = root.findViewById(R.id.show_friends_button);
        signoutButton = root.findViewById(R.id.signoutButton);

        usernameText.setText(fsh.getUsername());
        EmoteApplication.setIdlingResource(idlingResource);
        idlingResource.increment();
        db.collection(FRIEND_COLLECTION).document(EmoteApplication.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                friendsText.setText(Integer.toString(((List<String>) document.get("CURRENT_FRIENDS")).size()));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                        idlingResource.decrement();
                    }
                });
        idlingResource.increment();
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
                        idlingResource.decrement();

                    }
                });

        showFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), FollowingListActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    EmoteApplication.setUsername(null);

                    startActivity(intent);
                    //getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }
}