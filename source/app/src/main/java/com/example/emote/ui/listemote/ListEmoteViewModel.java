package com.example.emote.ui.listemote;
/**
 * View model for Emote List.
 * Has all the methods to query Firebase
 * with the necessary filters.
 */

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.emote.EmoteApplication;
import com.example.emote.Emotion;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
     * @param adapter EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     * @param showFriends Whether or not to show friends' event history
     */
    public void grabFirebase(final EmoteListAdapter adapter, final ArrayList<EmotionEvent> emoteDataList,
                             boolean showFriends) {

        if (showFriends) {
            db.collection(FireStoreHandler.EMOTE_COLLECTION).get()
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
                        }
                    });
        } else {
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
                        }
                    });
        }

    }

    /**
     * Function to grab the emote history from Firebase but with filtering.
     *  @param adapter EmoteListAdapter that is used by the list view
     * @param emoteDataList ArrayList used by the EmoteListAdapter
     * @param showFriends Whether or not to show friends' event history
     * @param filterEmote Emote to filter for
     */
    public void grabFirebase(final EmoteListAdapter adapter, final ArrayList<EmotionEvent> emoteDataList,
                             boolean showFriends, Emotion filterEmote) {

        if (showFriends) {
            db.collection(FireStoreHandler.EMOTE_COLLECTION)
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
                        }
                    });
        } else {
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
                        }
                    });
        }

    }

}