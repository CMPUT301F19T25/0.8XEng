package com.example.emote.ui.addemote;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.Emotion;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import com.example.emote.EmotionEvent;
import com.example.emote.Situation;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;

public class AddEmoteFragment extends Fragment {

    private static final String TAG = "AddEmoteFragment";
    private AddEmoteViewModel addEmoteViewModel;

    private TextInputLayout textLayoutEmoteName;
    private TextInputLayout textLayoutDate;
    private TextInputLayout textLayoutTime;
    private TextInputLayout textLayoutReason;
    private EditText textEmoteField;
    private EditText textDateField;
    private EditText textTimeField;
    private EditText textReasonField;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Spinner emotionSpinner;
    private Spinner situationSpinner;
    private Button submitButton;

    public View initializeViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_add_emote, container, false);
        textLayoutDate = root.findViewById(R.id.text_layout_date);
        textLayoutTime = root.findViewById(R.id.text_layout_time);
        textLayoutReason = root.findViewById(R.id.text_layout_reason);
        textDateField = root.findViewById(R.id.text_date_field);
        textTimeField = root.findViewById(R.id.text_time_field);
        textReasonField = root.findViewById(R.id.text_reason_field);
        situationSpinner = root.findViewById(R.id.spinner_situation);
        emotionSpinner = root.findViewById(R.id.spinnner_emote);
        submitButton = root.findViewById(R.id.submitButton);
        return root;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addEmoteViewModel =
                ViewModelProviders.of(this).get(AddEmoteViewModel.class);
        View root = initializeViews(inflater, container);

        situationSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Situation.getStrings(getContext())));
        emotionSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Emotion.getStrings(getContext())));

        textDateField.setInputType(InputType.TYPE_NULL);
        textDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        textDateField.setText(String.format("%02d:%02d", i, i1));
                    }
                }, year, month, day);
                datePicker.show();
            }
        });

        textTimeField.setInputType(InputType.TYPE_NULL);
        textTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        textTimeField.setText(i + ":" + i1);
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmote(v);
            }
        });
        resetFields();
        return root;
    }

    public void addEmote(View view) {
        String dateString = textDateField.getText().toString();
        String timeString = textTimeField.getText().toString();
        String reasonString = textReasonField.getText().toString();
        Situation situation = Situation.values()[situationSpinner.getSelectedItemPosition()];
        Emotion emotion = Emotion.values()[emotionSpinner.getSelectedItemPosition()];

        EmotionEvent event = new EmotionEvent(emotion, situation, reasonString, new Date(System.currentTimeMillis()));

        FireStoreHandler fsh = new FireStoreHandler("dman");
        fsh.addEmote(event);
        Toast.makeText(getContext(),"Emotion Event Added", Toast.LENGTH_LONG).show();
        resetFields();
    }

    public void resetFields(){
        textReasonField.setText("");
        situationSpinner.setSelection(0);
        situationSpinner.setSelection(0);
    }


}