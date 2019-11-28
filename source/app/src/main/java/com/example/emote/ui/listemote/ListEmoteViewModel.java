package com.example.emote.ui.listemote;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.example.emote.EmoteApplication;
import com.example.emote.Emotion;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * View model for Emote List.
 * Has all the methods to query Firebase
 * with the necessary filters.
 */
public class ListEmoteViewModel extends ViewModel {

    private FireStoreHandler fsh;
    private FirebaseFirestore db;

    /**
     * Constructor which sets the username for firestore access
     * and also gets a firestore reference.
     */
    public ListEmoteViewModel() {
        //
        fsh = new FireStoreHandler(EmoteApplication.getUsername());
        db = fsh.getFireStoreDBReference();
    }

    /**
     * Function to grab the emote history from Firebase
     *
     * @param adapter       EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     */
    public void grabFirebase(final EmoteListAdapter adapter, final ArrayList<EmotionEvent> emoteDataList,
                             ListEmoteFragment fragment) {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.EMOTE_COLLECTION)
                .whereEqualTo(EmotionEvent.USERNAME_KEY, fsh.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            emoteDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                emoteDataList.add(document.toObject(EmotionEvent.class));
                            }
                            Collections.sort(emoteDataList, new Comparator<EmotionEvent>() {
                                @Override
                                public int compare(EmotionEvent o1, EmotionEvent o2) {
                                    return o2.getDate().compareTo(o1.getDate());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        fragment.onLoadComplete();
                        EmoteApplication.getIdlingResource().decrement();
                    }
                });
    }

    /**
     * Helper function to grab the past emotions and filter them by friend and sort by date.
     *
     * @param adapter
     * @param emoteDataList
     * @param friends
     */
    public void grabFirebaseFriendsHelper(final EmoteListAdapter adapter,
                                          final ArrayList<EmotionEvent> emoteDataList,
                                          final List<String> friends) {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.EMOTE_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            emoteDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EmotionEvent currEvent  = document.toObject(EmotionEvent.class);
                                if(friends.contains(currEvent.getUsername())) {
                                    emoteDataList.add(document.toObject(EmotionEvent.class));
                                }
                            }
                            Collections.sort(emoteDataList, new Comparator<EmotionEvent>() {
                                @Override
                                public int compare(EmotionEvent o1, EmotionEvent o2) {
                                    return o2.getDate().compareTo(o1.getDate());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        EmoteApplication.getIdlingResource().decrement();
                    }
                });
    }

    /**
     * Function to grab emote history from firebase for all friends. First all of the friends are
     * fetched and used to filter the emotion events.
     *
     * @param adapter       EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     */
    public void grabFirebaseWithFriends(final EmoteListAdapter adapter,
                                        final ArrayList<EmotionEvent> emoteDataList,
                                        ListEmoteFragment fragment) {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.FRIEND_COLLECTION).document(EmoteApplication.getUsername())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> friends = (List<String>) document.get("CURRENT_FRIENDS");
                        Log.d(TAG, friends.toString());
                        grabFirebaseFriendsHelper(adapter, emoteDataList, friends);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                fragment.onLoadComplete();
                EmoteApplication.getIdlingResource().decrement();
            }
        });
    }

    /**
     * Function to grab the emote history from Firebase but with filtering.
     *
     * @param adapter       EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     * @param filterEmote   Emote to filter for
     */
    public void grabFirebase(final EmoteListAdapter adapter,
                             final ArrayList<EmotionEvent> emoteDataList, Emotion filterEmote,
                             ListEmoteFragment fragment) {

        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.EMOTE_COLLECTION)
                .whereEqualTo(EmotionEvent.USERNAME_KEY, fsh.getUsername())
                .whereEqualTo("emote", filterEmote)
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
                            Collections.sort(emoteDataList, new Comparator<EmotionEvent>() {
                                @Override
                                public int compare(EmotionEvent o1, EmotionEvent o2) {
                                    return o2.getDate().compareTo(o1.getDate());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        fragment.onLoadComplete();
                        EmoteApplication.getIdlingResource().decrement();
                    }
                });
    }

    /**
     * Function to grab the emote history from Firebase but with filtering, filtering by friends
     * and sorted by date.
     *
     * @param adapter       EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     * @param filterEmote   Emote to filter for
     */
    public void grabFirebaseFriendsHelper(final EmoteListAdapter adapter, final ArrayList<EmotionEvent> emoteDataList, Emotion filterEmote, final List<String> friends) {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.EMOTE_COLLECTION)
                .whereEqualTo("emote", filterEmote)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            emoteDataList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                EmotionEvent currEvent = document.toObject(EmotionEvent.class);
                                if(friends.contains(currEvent.getUsername())) {
                                    emoteDataList.add(document.toObject(EmotionEvent.class));
                                }
                            }
                            Collections.sort(emoteDataList, new Comparator<EmotionEvent>() {
                                @Override
                                public int compare(EmotionEvent o1, EmotionEvent o2) {
                                    return o2.getDate().compareTo(o1.getDate());
                                }
                            });
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        EmoteApplication.getIdlingResource().decrement();
                    }
                });
    }



    /**
     * Function to grab the emote history from Firebase but with filtering by emotion and friend.
     *
     * @param adapter       EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     * @param filterEmote   Emote to filter for
     */
    public void grabFirebaseWithFriends(final EmoteListAdapter adapter, final ArrayList<EmotionEvent> emoteDataList, final Emotion filterEmote) {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.FRIEND_COLLECTION).document(EmoteApplication.getUsername())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> friends = (List<String>) document.get("CURRENT_FRIENDS");
                        Log.d(TAG, friends.toString());
                        grabFirebaseFriendsHelper(adapter, emoteDataList, filterEmote, friends);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                EmoteApplication.getIdlingResource().decrement();
            }
        });
    }
}