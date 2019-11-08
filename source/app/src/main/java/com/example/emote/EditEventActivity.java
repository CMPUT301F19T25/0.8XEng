package com.example.emote;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.emote.ui.addemote.AddEmoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class EditEventActivity extends AppCompatActivity {


    private static final String TAG = "AddEmoteFragment";
    private AddEmoteViewModel addEmoteViewModel;

    private TextView textDate;
    private TextView textTime;
    private EditText textReasonField;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Spinner emotionSpinner;
    private Spinner situationSpinner;
    private Button submitButton;

    private EmotionEvent emotionEvent;

    /**
     * Initialize all the necessary views with findViewById
     */
    public void initializeViews() {
        textDate = this.findViewById(R.id.text_date);
        textTime = this.findViewById(R.id.text_time);
        textReasonField = this.findViewById(R.id.text_reason_field);
        situationSpinner = this.findViewById(R.id.spinner_situation);
        emotionSpinner = this.findViewById(R.id.spinnner_emote);
        submitButton = this.findViewById(R.id.submitButton);

        situationSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Situation.getStrings(this)));
        emotionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Emotion.getStrings(this)));
    }

    public void setFields(){
        Calendar eventCal = Calendar.getInstance();
        eventCal.setTime(emotionEvent.getDate());
        textDate.setText(String.format("%02d/%02d/%02d",
                eventCal.get(Calendar.YEAR),
                eventCal.get(Calendar.MONTH),
                eventCal.get(Calendar.DATE)));
        textTime.setText(String.format("%02d:%02d",
                eventCal.get(Calendar.HOUR),
                eventCal.get(Calendar.MINUTE)));
        textReasonField.setText(emotionEvent.getReason());
        situationSpinner.setSelection(Situation.getIndex(emotionEvent.getSituation()));
        emotionSpinner.setSelection(Emotion.getIndex(emotionEvent.getEmote()));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        initializeViews();

        Intent intent = getIntent();
        emotionEvent = (EmotionEvent) intent.getSerializableExtra("event");
        setFields();

    }



    /**
     * Add the defined Emotion event to the firebase DB and reset the fields.
     * @param view

    public void addEmote(View view) {
        EmotionEvent event;
        try {
            Date date = pickerToDate(textDateField.getText().toString(), textTimeField.getText().toString());
            String reasonString = textReasonField.getText().toString();
            Situation situation = Situation.values()[situationSpinner.getSelectedItemPosition()];
            Emotion emotion = Emotion.values()[emotionSpinner.getSelectedItemPosition()];
            event = new EmotionEvent(emotion, situation, reasonString, date);

        } catch (Exception e) {
            // TODO proper error messages
            Toast.makeText(EditEventActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            resetFields();
            return;
        }

        FireStoreHandler fsh = new FireStoreHandler("dman");
        fsh.addEmote(event);
        Toast.makeText(getContext(), "Emotion Event Added", Toast.LENGTH_LONG).show();
        resetFields();
    }
    */


}
