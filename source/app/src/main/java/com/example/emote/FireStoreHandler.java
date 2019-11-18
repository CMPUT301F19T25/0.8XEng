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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class used to handle FireStore operations
 */
public class FireStoreHandler {

    private static final String TAG = "FireStoreHandler";

    public static final String FRIEND_COLLECTION = "FRIEND_COLLECTION";
    public static final String EMOTE_COLLECTION = "EMOTE_COLLECTION";
    public static final String INCOMING_FRIENDS = "INCOMING_FRIEND_REQUESTS";
    public static final String CURRENT_FRIENDS = "CURRENT_FRIENDS";

    private String username;
    private FirebaseFirestore db;

    /**
     *  setup user at signup
     *  */
    public void setupUser(){
        // Create empty friend and friend request lists if they don't exist
        while (username == null) {}
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
    }

    /**
     * @param username Username of the logged-in user.
     */
    public FireStoreHandler(final String username){
        this.username = username;
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     *  add friend
     * @param username
     * @param friend
     *  */
    public void addFriend(final String username, final String friend){
        // Add to friend's friend list
        db.collection(FRIEND_COLLECTION).document(friend)
                .update(CURRENT_FRIENDS, FieldValue.arrayUnion(username))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Successfully added" + username + " to " + friend + "friends list");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding friend", e);
                    }
                });
    }

    /**
     * Accept a friend request.
     * @param friendUsername Id for the friend to add.
     */
    public void acceptFriendRequest(final String friendUsername){
        // remove the incoming friend request
        removeFriendRequest(friendUsername);
        // add users to each other's friend list
        addFriend(username, friendUsername);
    }

    /**
     remove friend request
     * @param friendUsername
     * @return
     */
    public void removeFriendRequest(String friendUsername) {
        db.collection(FRIEND_COLLECTION).document(this.username)
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
    }

    /**
     decline a follower's friend request*
     * @param friendUsername
     * @return
     */
    public void declineFollowRequest(String friendUsername) {
        removeFriendRequest(friendUsername);
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
     * Delete an emote
     *
     * @param fireStoreDocumentID
     * @return
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

    public FirebaseFirestore getFireStoreDBReference(){
        return db;
    }

    public String getUsername(){
        return username;
    }
}
