package com.example.emote.ui.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.EmoteApplication;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * The Friend's Fragment, showing a list of friends
 */
public class FriendsFragment extends Fragment {

    private static final String TAG = "FriendsFragment";
    private FriendsViewModel friendsViewModel;

    private ArrayList<String> friendsDataList;
    private FriendsListAdapter friendsAdapter;
    private ListView friendsListView;

    private ArrayList<String> searchFriendDataList;
    private SearchListAdapter searchFriendAdapater;
    private AutoCompleteTextView searchAutoComplete;

    /**
     * Function to initialize all the views, lists and adapters. Returns the root view.
     *
     * @param inflater  LayoutInflater
     * @param container container to grab the root from
     * @return Root view for the fragment
     */
    public View initializeViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsListView = root.findViewById(R.id.friends_list_view);
        friendsDataList = new ArrayList<>();
        friendsAdapter = new FriendsListAdapter(getContext(), friendsDataList);
        friendsListView.setAdapter(friendsAdapter);

        searchFriendDataList = new ArrayList<>();
        searchFriendAdapater = new SearchListAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, searchFriendDataList);
        searchAutoComplete = root.findViewById(R.id.auto_complete_friends);
        searchAutoComplete.setAdapter(searchFriendAdapater);
        searchAutoComplete.setThreshold(1);
        return root;
    }

    /**
     * onCreateView of the Friend's Fragment
     * @param inflater
     * @param container
     * @param savedInstAdanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstAdanceState) {
        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);

        View root = initializeViews(inflater, container);

        String username = EmoteApplication.getUsername();
        final FireStoreHandler fsh = new FireStoreHandler(username);
        FirebaseFirestore db = fsh.getFireStoreDBReference();

        db.collection(FireStoreHandler.FRIEND_COLLECTION).document(fsh.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
//                        friendsDataList.clear();
                            Object obj = document.get(FireStoreHandler.INCOMING_FRIENDS);
                            if (obj != null) {
                                friendsDataList.addAll((ArrayList<String>) obj);
                            }
                            Log.d(TAG, "got some friends: " + friendsDataList.size());
                            for (int i = 0; i < friendsDataList.size(); i++) {
                                Log.d(TAG, "friend: " + friendsDataList.get(i));
                            }
                            friendsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting friends: ", task.getException());
                        }
                    }
                });

        db.collection(FireStoreHandler.FRIEND_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            HashSet<String> currentFriendsHash = new HashSet<>();
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                if (doc.getId().equals(fsh.getUsername())) {
                                    currentFriendsHash = new HashSet((ArrayList<String>) doc.get(FireStoreHandler.CURRENT_FRIENDS));
                                }
                            }
                            Log.d(TAG, "Current friends:" + currentFriendsHash.size());
                            searchFriendDataList.clear();
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                if (!currentFriendsHash.contains(doc.getId()) &&
                                        !doc.getId().equals(fsh.getUsername())) {
                                    searchFriendDataList.add(doc.getId());
                                }
                            }
                            for (int i = 0; i < searchFriendDataList.size(); i++) {
                                Log.d(TAG, "Other User: " + searchFriendDataList.get(i));
                            }
                            searchFriendAdapater.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting all users");
                        }

                    }
                });
        return root;
    }

    /**
     * handle the activity created event
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }
}
