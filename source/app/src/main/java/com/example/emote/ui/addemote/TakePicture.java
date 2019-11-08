package com.example.emote.ui.addemote;
/**
 *  Fragment to add a new emote to the firebase db.
 */

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.Emotion;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.example.emote.R;
import com.example.emote.Situation;

import java.util.Calendar;
import java.util.Date;

public class TakePicture extends Fragment {

    public Button cameraButton;

    public View initializeViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_take_picture, container, false);

        cameraButton = root.findViewById(R.id.cameraButton);

        return root;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = initializeViews(inflater, container);

//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        return root;
    }

    public void createCameraPreview() {

    }

}