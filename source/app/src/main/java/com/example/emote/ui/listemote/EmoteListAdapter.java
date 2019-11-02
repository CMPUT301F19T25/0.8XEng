package com.example.emote.ui.listemote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.emote.R;
import com.example.emote.EmotionEvent;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EmoteListAdapter extends ArrayAdapter<EmotionEvent> {

    private ArrayList<EmotionEvent> emoteDataList;
    private Context context;

    public EmoteListAdapter(Context context, ArrayList<EmotionEvent> emoteDataList){
        super(context, 0, emoteDataList);
        this.emoteDataList = emoteDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_individual_emote, parent,false);
        }

        EmotionEvent emote = emoteDataList.get(position);
        TextView emoteTextView = view.findViewById(R.id.emote_text);
        TextView userTextView = view.findViewById(R.id.user_name);
        TextView dateTextView = view.findViewById(R.id.date);
        emoteTextView.setText(emote.getEmote().toString());
        userTextView.setText(emote.getUsername());
        try{
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            dateTextView.setText(df.format(emote.getDate()));
        }catch (NullPointerException np){
            dateTextView.setText("No date");
        }

        return view;
    }


}
