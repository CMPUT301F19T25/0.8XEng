package com.example.emote.ui.friends;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.emote.EmoteApplication;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import java.util.ArrayList;

public class FriendsListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> friendsDataList;
    private Context context;

    public FriendsListAdapter(Context context, ArrayList<String> friendsDataList){
        super(context, 0, friendsDataList);
        this.friendsDataList = friendsDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_individual_friend, parent, false);
        }

        final String friend = friendsDataList.get(position);
        Log.d("FriendsFragment", friend);
        TextView friendTextView = view.findViewById(R.id.friend_text);
        friendTextView.setText(friend);

        Button acceptFollow = view.findViewById(R.id.accept_follow);
        acceptFollow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
                fsh.acceptFriendRequest(friend);
                friendsDataList.remove(position);
                notifyDataSetChanged();
            }
        });

        Button declineFollow = view.findViewById(R.id.decline_follow);
        declineFollow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
                fsh.declineFollowRequest(friend);
                friendsDataList.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
