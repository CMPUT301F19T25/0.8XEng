package com.example.emote;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.emote.FireStoreHandler.EMOTE_COLLECTION;
import static com.example.emote.FireStoreHandler.FRIEND_COLLECTION;
import static java.lang.Long.MIN_VALUE;

/**
 * Activity to show the following list for the users
 */
public class FollowingListActivity extends AppCompatActivity {

    private static final String TAG = "FollowingListActivity";
    private ListView followingListView;
    private ArrayAdapter<String> followingAdapter;
    private ArrayList<String> followingDataList;
    private List<String> current_friends;

    private FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
    private FirebaseFirestore db = fsh.getFireStoreDBReference();


    /**
     * onCreate event handler to fetch user's following list
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_following);
        this.getSupportActionBar().hide();
        String username = EmoteApplication.getUsername();
        followingListView = findViewById(R.id.following_list_view);

        db.collection(FRIEND_COLLECTION).document(EmoteApplication.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                current_friends = (List<String>) document.get("CURRENT_FRIENDS");
                                followingDataList = (ArrayList<String>) current_friends;
                                followingAdapter = new FollowingListAdapter(getBaseContext(), followingDataList);
                                followingListView.setAdapter(followingAdapter);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     * Method to handle what to do when back button is pressed, used to exit.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
