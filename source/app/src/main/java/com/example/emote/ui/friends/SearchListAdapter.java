package com.example.emote.ui.friends;

import android.content.Context;
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

/*
Adapter for the Autocomplete Textbox that allows users to search other users to add.
 */
public class SearchListAdapter extends ArrayAdapter<String> {
    /**
     * Constructor for the Search List Adapter
     * @param context: the Application's context
     * @param resourceId: integer representing the id for the resource
     * @param otherUsers: List of users to search
     */
    public SearchListAdapter(Context context, int resourceId, ArrayList<String> otherUsers) {
        super(context, resourceId, 0, otherUsers);
    }

    /**
     * Called when generating each item in the search list. Also adds the click listener
     * to the follow button
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(this.getContext()).inflate(R.layout.list_individual_friend_search, parent, false);
        }

        final String friend = this.getItem(position);
        TextView friendTextView = view.findViewById(R.id.friend_text);
        friendTextView.setText(friend);

        Button sendFollow = view.findViewById(R.id.button_send);
        sendFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
                fsh.sendFriendRequest(friend);
                notifyDataSetChanged();
                Button send = v.findViewById(R.id.button_send);
                send.setEnabled(false);
                send.setText("FOLLOWED");
            }
        });

        return view;
    }
}
