package com.example.emote.ui.addemote;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.FireStoreHandler;
import com.example.emote.MainActivity;
import com.example.emote.R;
import com.example.emote.EmotionEvent;
import com.example.emote.Situation;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

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
    private EditText textSituationField;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    Spinner situationSpinner;

    public void initializeViews() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addEmoteViewModel =
                ViewModelProviders.of(this).get(AddEmoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_emote, container, false);
//        final TextView textView = root.findViewById(R.id.text_add_emote);
//        addEmoteViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        textLayoutDate = root.findViewById(R.id.text_layout_date);
        textLayoutTime = root.findViewById(R.id.text_layout_time);
        textLayoutReason = root.findViewById(R.id.text_layout_reason);
        textEmoteField = root.findViewById(R.id.text_emote_field);
        textDateField = root.findViewById(R.id.text_date_field);
        textTimeField = root.findViewById(R.id.text_time_field);
        textReasonField = root.findViewById(R.id.text_reason_field);
        situationSpinner = root.findViewById(R.id.spinner_situation);

//        FireStoreHandler fsh = new FireStoreHandler("john123");
//        EmotionEvent emoteEvent1 = new EmotionEvent("Happy", Situation.FEW_PEOPLE, "Good food");
//        EmotionEvent emoteEvent2 = new EmotionEvent("Sad", Situation.FEW_PEOPLE, "Dog died");
//        EmotionEvent emoteEvent3 = new EmotionEvent("Tired", Situation.FEW_PEOPLE, "Sleepy");
//        fsh.addEmote(emoteEvent1);
//        fsh.addEmote(emoteEvent2);
//        fsh.addEmote(emoteEvent3);

        situationSpinner.setAdapter(new ArrayAdapter<Situation>(getContext(), android.R.layout.simple_spinner_item, Situation.values()));

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

        return root;
    }

    public void addEmote(View view) {
        String emoteString = textEmoteField.getText().toString();
        String dateString = textDateField.getText().toString();
        String timeString = textTimeField.getText().toString();
        String reasonString = textReasonField.getText().toString();
        String situationString = textSituationField.getText().toString();

        EmotionEvent event = new EmotionEvent(emoteString, Situation.ALONE,reasonString);

        FireStoreHandler fsh = new FireStoreHandler("dman");
        fsh.addEmote(event);
    }

    public void editDateText() {

    }
}