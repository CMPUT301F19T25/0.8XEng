package com.example.emote.ui.friends;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.EmoteApplication;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import com.example.emote.EmotionEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
public class FriendsFragment extends Fragment {

    private static final String TAG = "FriendsFragment";
    private FriendsViewModel friendsViewModel;

    private ArrayList<String> friendsDataList = new ArrayList<>();
    private ArrayAdapter<String> friendsAdapter;
    private ListView friendsListView;
    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        friendsViewModel = ViewModelProviders.of(this).get(FriendsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        friendsListView = root.findViewById(R.id.friends_list_view);
        friendsDataList = new ArrayList<>();
        friendsAdapter = new FriendsListAdapter(getContext(), friendsDataList);
        friendsListView.setAdapter(friendsAdapter);

        String username = EmoteApplication.getUsername();
        FireStoreHandler fsh = new FireStoreHandler(username);
        FirebaseFirestore db = fsh.getFireStoreDBReference();
        db.collection(FireStoreHandler.FRIEND_COLLECTION).document(fsh.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        friendsDataList.addAll ((ArrayList<String>) document.get(FireStoreHandler.INCOMING_FRIENDS));
                        Log.d(TAG, "got some friends: " + friendsDataList.size());
                        for(int i = 0; i < friendsDataList.size(); i++){
                            Log.d(TAG, "friend: " + friendsDataList.get(i));
                        }
                        friendsAdapter.notifyDataSetChanged();
                    } else {
                        Log.d(TAG, "Error getting friends: ", task.getException());
                    }
                }
            });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }
}
