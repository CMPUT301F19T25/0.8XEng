package com.example.emote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.emote.ui.addemote.AddEmoteViewModel;
import com.google.firebase.firestore.GeoPoint;
import com.google.android.gms.maps.model.LatLng;


import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {


    private static final String TAG = "EditEmoteEvent";
    private AddEmoteViewModel addEmoteViewModel;
    private FireStoreHandler fsh;

    private TextView textDate;
    private TextView textTime;
    private EditText textReasonField;
    private Spinner emotionSpinner;
    private Spinner situationSpinner;
    private Button editButton;
    private Button deleteButton;

    private Button locationButton;

    // arbitrary int for a map_request
    private static final int MAP_REQUEST = 1617;
    private GeoPoint location;

    private EmotionEvent emotionEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        initializeViews();

        Intent intent = getIntent();
        emotionEvent = (EmotionEvent) intent.getSerializableExtra("event");
        if (!intent.getBooleanExtra("editable", false)) {
            disableViews();
        }
        fsh = new FireStoreHandler(EmoteApplication.getUsername());
        setFields();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmote();
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fsh.removeEmote(emotionEvent.getFireStoreDocumentID());
                finish();
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { editLocation(view);}
        });
    }

    public void editLocation(View view) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("MAP_MODE", MapsActivity.MapMode.EditLocation);
        if (location != null) {
            extras.putParcelable("location", new LatLng(location.getLatitude(), location.getLongitude()));
        }
        mapIntent.putExtras(extras);
        startActivityForResult(mapIntent, MAP_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST && resultCode == Activity.RESULT_OK) {
            LatLng result = (LatLng) data.getExtras().get("location");
            location = new GeoPoint(result.latitude, result.longitude);
        }
    }

    /**
     * Initialize all the necessary views with findViewById
     */
    public void initializeViews() {
        this.getSupportActionBar().hide();
        textDate = this.findViewById(R.id.text_date);
        textTime = this.findViewById(R.id.text_time);
        textReasonField = this.findViewById(R.id.text_reason_field);
        situationSpinner = this.findViewById(R.id.spinner_situation);
        emotionSpinner = this.findViewById(R.id.spinnner_emote);
        editButton = this.findViewById(R.id.button_edit);
        deleteButton = this.findViewById(R.id.button_delete);

        locationButton = this.findViewById(R.id.addLocationButton);

        situationSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Situation.getStrings(this)));
        emotionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Emotion.getStrings(this)));
    }
    /*
     * Set the fields that we want for
     */
    public void setFields() {
        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(emotionEvent.getDate());
        textDate.setText(String.format("%02d/%02d/%02d",
                eventCal.get(Calendar.YEAR),
                eventCal.get(Calendar.MONTH),
                eventCal.get(Calendar.DATE)));
        textTime.setText(String.format("%02d:%02d",
                eventCal.get(Calendar.HOUR),
                eventCal.get(Calendar.MINUTE)));

        location = emotionEvent.getLocation();

        textReasonField.setText(emotionEvent.getReason());
        situationSpinner.setSelection(Situation.getIndex(emotionEvent.getSituation()));
        emotionSpinner.setSelection(Emotion.getIndex(emotionEvent.getEmote()));
    }
    /*
     * Disable views for when the user selects an event that doesn't belong to them.
     */
    public void disableViews() {
        situationSpinner.setEnabled(false);
        emotionSpinner.setEnabled(false);
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        textReasonField.setEnabled(false);
        locationButton.setEnabled(false);
    }

    /**
     * Add the defined Emotion event to the firebase DB and reset the fields.
     */
    public void editEmote() {
        EmotionEvent event;
        try {
            String reasonString = textReasonField.getText().toString();
            Situation situation = Situation.values()[situationSpinner.getSelectedItemPosition()];
            Emotion emotion = Emotion.values()[emotionSpinner.getSelectedItemPosition()];

            emotionEvent.setReason(reasonString);
            emotionEvent.setSituation(situation);
            emotionEvent.setEmote(emotion);
            emotionEvent.setLocation(location);

        } catch (Exception e) {
            // TODO proper error messages
            Toast.makeText(EditEventActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        fsh.removeEmote(emotionEvent.getFireStoreDocumentID());
        fsh.addEmote(emotionEvent);
    }


}
