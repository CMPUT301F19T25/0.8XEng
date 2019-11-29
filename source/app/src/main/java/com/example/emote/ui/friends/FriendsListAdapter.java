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

/**
 * Adapter to show the list of friends
 */
public class FriendsListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> friendsDataList;
    private Context context;

    /**
     * Adapter for the FriendsList
     * @param context the application context
     * @param friendsDataList the data list for friends
     */
    public FriendsListAdapter(Context context, ArrayList<String> friendsDataList){
        super(context, 0, friendsDataList);
        this.friendsDataList = friendsDataList;
        this.context = context;
    }

    /**
     * Method to get the view for the friends list adapter
     * @param position position to get
     * @param convertView the view to convert
     * @param parent the parent view
     * @return the view for the FriendsList
     */
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
