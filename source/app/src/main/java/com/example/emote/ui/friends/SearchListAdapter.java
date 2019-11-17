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

public class SearchListAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> otherUsers;

    public SearchListAdapter(Context context, int resourceId, ArrayList<String> otherUsers){
        super(context, resourceId, otherUsers);
        this.context = context;
        this.otherUsers = otherUsers;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_individual_friend_search, parent, false);
        }

        final String friend = otherUsers.get(position);
        TextView friendTextView = view.findViewById(R.id.friend_text);
        friendTextView.setText(friend);

        Button sendFollow = view.findViewById(R.id.button_send);
        sendFollow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
                fsh.sendFriendRequest(friend);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
