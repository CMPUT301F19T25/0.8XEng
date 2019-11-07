package com.example.emote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Long.MIN_VALUE;

public class FireStoreHandler {

    private static final String TAG = "FireStoreHandler";

    public static final String FRIEND_COLLECTION = "FRIEND_COLLECTION";
    public static final String EMOTE_COLLECTION = "EMOTE_COLLECTION";
    public static final String INCOMING_FRIENDS = "INCOMING_FRIEND_REQUESTS";
    public static final String CURRENT_FRIENDS = "CURRENT_FRIENDS";

    private String username;
    private EmotionEvent recent_emote;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * @param username Username of the logged-in user.
     */
    public FireStoreHandler(final String username){

        // Create empty friend and friend request lists if they don't exist
        db.collection(FRIEND_COLLECTION).document(username).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (!document.exists()) {
                            Map<String, Object> friends = new HashMap<>();
                            friends.put(CURRENT_FRIENDS, new ArrayList<String>());
                            friends.put(INCOMING_FRIENDS, new ArrayList<String>());
                            db.collection(FRIEND_COLLECTION).document(username).set(friends)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        this.username = username;
    }

    /**
     * Accept a friend request.
     * @param friendUsername Id for the friend to add.
     */
    public void acceptFriendRequest(String friendUsername){

        // Remove from Incoming Friend Request
        db.collection(FRIEND_COLLECTION).document(username)
            .update(INCOMING_FRIENDS, FieldValue.arrayRemove(friendUsername))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully accepted friend request");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        // Add to friend's friend list
        db.collection(FRIEND_COLLECTION).document(friendUsername)
                .update(CURRENT_FRIENDS, FieldValue.arrayUnion(username))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added to current user to friends friend list");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        // Add to current user's friend list
        db.collection(FRIEND_COLLECTION).document(username)
                .update(CURRENT_FRIENDS, FieldValue.arrayUnion(friendUsername))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added friend to current user's friend list");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }


    /**
     * Send a friend request.
     * @param friendUsername Id for the friend to send the request to.
     */
    public void sendFriendRequest(String friendUsername){
        db.collection(FRIEND_COLLECTION).document(friendUsername)
                .update(INCOMING_FRIENDS, FieldValue.arrayUnion(username))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added friend request");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    /**
     * Add an emotional event. On success the fireStore document id will be added to the emoteEvent
     * Object.
     * @param emoteEvent the emotion event to add.
     */
    public void addEmote(final EmotionEvent emoteEvent){

        emoteEvent.setUsername(username);
        db.collection(EMOTE_COLLECTION).document(emoteEvent.getFireStoreDocumentID()).set(emoteEvent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    /**
     * Delete an emote.
     * @param fireStoreDocumentID The id of the emotion event to be deleted.
     */
    public void removeEmote(String fireStoreDocumentID){
        db.collection(EMOTE_COLLECTION).document(fireStoreDocumentID).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public EmotionEvent getRecentEmote() {
        db.collection(EMOTE_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Date max_date = new Date(MIN_VALUE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (max_date.compareTo(document.toObject(EmotionEvent.class).getDate()) <= 0) {
                                    recent_emote = document.toObject(EmotionEvent.class);
                                    max_date = document.toObject(EmotionEvent.class).getDate();
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return recent_emote;
    }


    public FirebaseFirestore getFireStoreDBReference(){
        return db;
    }

    public String getUsername(){
        return username;
    }
}
