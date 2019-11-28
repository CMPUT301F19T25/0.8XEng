package com.example.emote.ui.listemote;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.Arrays;
import java.util.List;

/**
 * Fragment for the History of Emotion Events.
 * This fragment show's the user and other user's
 * emotion events.
 */
public class ListEmoteFragment extends Fragment {

    private static final String TAG = "ListEmoteFragment";
    private ListEmoteViewModel listEmoteViewModel;

    private ArrayList<EmotionEvent> emoteDataList;
    private EmoteListAdapter emoteAdapter;

    private ListView emoteListView;
    private Spinner spinner;
    private CheckBox showFriends;
    private ProgressBar progressBar;

    /**
     * Main Method for this Emote fragment. Initializes the fragment,
     * sets the spinner items and onclicklisteners.
     *
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
        progressBar = root.findViewById(R.id.progress_bar);

        List<String> emotionStrings = new ArrayList<>(Arrays.asList(Emotion.getStrings(getContext())));
        emotionStrings.add(0, "All");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, emotionStrings);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(adapter);
        emoteDataList = new ArrayList<>();
        emoteAdapter = new EmoteListAdapter(getContext(), emoteDataList);
        emoteListView.setAdapter(emoteAdapter);
        listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList, this);


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
        // Refresh on check box clicks
        showFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refresh();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    /**
     * Override the resume so that the list is refreshed after an edit or delete
     */
    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * Refresh the list to get the changes from firebase.
     */
    public void refresh() {
        progressBar.setVisibility(View.VISIBLE);
        if (spinner.getSelectedItem().toString() != "All" && showFriends.isChecked()) {
            listEmoteViewModel.grabFirebaseWithFriends(emoteAdapter, emoteDataList,
                    Emotion.values()[spinner.getSelectedItemPosition()-1]);
        } else if (spinner.getSelectedItem().toString() != "All") {
            listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList, Emotion.values()[spinner.getSelectedItemPosition()-1],this);
        } else if (showFriends.isChecked()) {
            listEmoteViewModel.grabFirebaseWithFriends(emoteAdapter, emoteDataList, this);
        } else {
            listEmoteViewModel.grabFirebase(emoteAdapter, emoteDataList,this);
        }
    }

    /**
     * Called when a particular loading event from firebase is complete.
     */
    public void onLoadComplete(){
        progressBar.setVisibility(View.GONE);
    }

}