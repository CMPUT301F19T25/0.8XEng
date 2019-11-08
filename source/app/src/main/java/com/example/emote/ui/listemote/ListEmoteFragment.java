package com.example.emote.ui.listemote;
/**
 * Fragment for the History of Emotion Events.
 */
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.EditEventActivity;
import com.example.emote.EmoteApplication;
import com.example.emote.Emotion;
import com.example.emote.EmotionEvent;
import com.example.emote.R;

import java.util.ArrayList;

public class ListEmoteFragment extends Fragment {

    private static final String TAG = "ListEmoteFragment";
    private ListEmoteViewModel listEmoteViewModel;

    private ArrayList<EmotionEvent> emoteDataList;
    private EmoteListAdapter emoteAdapter;

    private ListView emoteListView;
    private Spinner spinner;
    private Button refreshButton;
    private CheckBox showFriends;
    private CheckBox filterEmotes;

    /**
     * Main Method for this Emote fragment. Initializes the fragment,
     * sets the spinner items and onclicklisteners.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listEmoteViewModel =
                ViewModelProviders.of(this).get(ListEmoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_emote, container, false);

        emoteListView = root.findViewById(R.id.emote_list_view);
        spinner = root.findViewById(R.id.spinner);
        showFriends = root.findViewById(R.id.check_box_show_friends);
        filterEmotes = root.findViewById(R.id.check_box_filter);
        refreshButton = root.findViewById(R.id.button_refresh);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Emotion.getStrings(getContext()));
        spinner.setAdapter(adapter);
        emoteDataList = new ArrayList<>();
        emoteAdapter = new EmoteListAdapter(getContext(), emoteDataList);
        emoteListView.setAdapter(emoteAdapter);
        listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList, false);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterEmotes.isChecked()) {
                    listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList,
                            showFriends.isChecked(), Emotion.values()[spinner.getSelectedItemPosition()]);
                } else {
                    listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList, showFriends.isChecked());
                }
            }
        });

        emoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EditEventActivity.class);
                intent.putExtra("event", emoteAdapter.getItem(position));
                boolean isEditable = emoteAdapter.getItem(position).getUsername().equals(EmoteApplication.getUsername());
                intent.putExtra("editable", isEditable);
                startActivity(intent);
            }
        });

        return root;
    }
}