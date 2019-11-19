package com.example.emote.ui.listemote;
/**
 * ArrayAdapter to allow Emote List items to fill up.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.emote.Emotion;
import com.example.emote.R;
import com.example.emote.EmotionEvent;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EmoteListAdapter extends ArrayAdapter<EmotionEvent> {

    private ArrayList<EmotionEvent> emoteDataList;
    private Context context;


    /**
     * Constructor for EmoteListAdapter
     * @param context
     * @param emoteDataList
     */
    public EmoteListAdapter(Context context, ArrayList<EmotionEvent> emoteDataList) {
        super(context, 0, emoteDataList);
        this.emoteDataList = emoteDataList;
        this.context = context;
    }

    /**
     * getView method for the EmoteListAdapter. Allows us to set text fields
     * inside a list item.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_individual_emote, parent, false);
        }

        EmotionEvent emote = emoteDataList.get(position);
        TextView emoteTextView = view.findViewById(R.id.emote_text);
        TextView userTextView = view.findViewById(R.id.user_name);
        TextView dateTextView = view.findViewById(R.id.date);
        ImageView emoticonImage = view.findViewById(R.id.emoticon);

        int identifier = this.context.getResources().getIdentifier(emote.getEmote().toString(), "string", this.context.getPackageName());
        emoteTextView.setText(this.context.getResources().getString(identifier));
        userTextView.setText(emote.getUsername());
        try {
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            dateTextView.setText(df.format(emote.getDate()));
        } catch (NullPointerException np) {
            dateTextView.setText("No date");
        }

        GradientDrawable drawable = (GradientDrawable) view.getBackground();
        drawable.setColor(Emotion.getColor(context, emote.getEmote()));


        int emoticonIdentifier = context.getResources().getIdentifier(emote.getEmote().toString()+"_EMOTICON", "string", context.getPackageName());
        String emotePath = context.getResources().getString(emoticonIdentifier);
        int emoticonId = context.getResources().getIdentifier(emotePath, "drawable", context.getPackageName());
        emoticonImage.setImageResource(emoticonId);

        return view;
    }


}
