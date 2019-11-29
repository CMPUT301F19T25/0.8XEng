package com.example.emote.ui.addemote;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.emote.EmoteApplication;
import com.example.emote.Emotion;
import com.example.emote.EmotionEvent;
import com.example.emote.FireStoreHandler;
import com.example.emote.MapsActivity;
import com.example.emote.R;
import com.example.emote.Situation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Fragment to add a new emote to the firebase db.
 */
public class AddEmoteFragment extends Fragment {

    private static final String TAG = "AddEmoteFragment";
    private AddEmoteViewModel addEmoteViewModel;

    private EditText textDateField;
    private EditText textTimeField;
    private EditText textReasonField;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Spinner emotionSpinner;
    private Spinner situationSpinner;
    private Button submitButton;
    private Button takePictureButton;
    private ImageView cameraPreview;
    private Button addLocationButton;

    private TextView mapText;

    FirebaseStorage storage;
    String STORAGE_REF = "gs://emote-f75ce.appspot.com";

    Bitmap cameraImage;

    GeoPoint mapLocation;


    private static final int CAMERA_REQUEST = 1888;

    // arbitrary int for a map_request
    private static final int MAP_REQUEST = 1617;

    /**
     * Initialize all the necessary views with findViewById
     * @param inflater LayoutInflater to inflate Layour
     * @param container ViewGroup Container
     * @return Returns the root view object of fragment
     */
    public View initializeViews(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_add_emote, container, false);
        textDateField = root.findViewById(R.id.text_date_field);
        textTimeField = root.findViewById(R.id.text_time_field);
        textReasonField = root.findViewById(R.id.text_reason_field);
        situationSpinner = root.findViewById(R.id.spinner_situation);
        emotionSpinner = root.findViewById(R.id.spinnner_emote);
        submitButton = root.findViewById(R.id.submitButton);
        takePictureButton = root.findViewById(R.id.addPhotoButton);
        cameraPreview = root.findViewById(R.id.cameraPreview);
        addLocationButton = root.findViewById(R.id.addLocationButton);

        mapText = root.findViewById(R.id.mapText);

        storage = FirebaseStorage.getInstance();
        return root;
    }

    /**
     * the onCreateView that handles creating a view for adding an emote
     * @param inflater: inflater passed for the fragment
     * @param container: the ViewGroup container passed for the fragment
     * @param savedInstanceState: the saved instance state
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addEmoteViewModel =
                ViewModelProviders.of(this).get(AddEmoteViewModel.class);
        View root = initializeViews(inflater, container);

        situationSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Situation.getStrings(getContext())));
        emotionSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Emotion.getStrings(getContext())));

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmote(v);
            }
        });
        takePictureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addPicture(view);
            }
        });
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { addLocation(view);}
        });
        resetFields();
        return root;
    }

    /***
     * Set time and date listeners
     */
    public void setTimeAndDateListeners() {
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
                        textDateField.setText(String.format("%02d/%02d/%02d", i, i1 + 1, i2));
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
                        textTimeField.setText(String.format("%02d:%02d", i, i1));
                    }
                }, hour, minute, true);
                timePicker.show();
            }
        });
    }

    /***
     *
     * @param view
     * Start camera intent
     */
    public void addPicture(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    /**
     * Add location to the emotion event, start Map intent
     * @param view: not used
     */
    public void addLocation(View view) {
        Intent mapIntent = new Intent(this.getContext(), MapsActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("MAP_MODE", MapsActivity.MapMode.EditLocation);
        mapIntent.putExtras(extras);
        startActivityForResult(mapIntent, MAP_REQUEST);
    }

    /**
     * Handle getting the activity result, check if the activity returned correctly
     * @param requestCode: the code
     * @param resultCode: the result returned by the activity
     * @param data: data returned by activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            cameraImage = (Bitmap) data.getExtras().get("data");

            int newHeight = (int) Math.floor((double) cameraImage.getHeight() *( (double) cameraPreview.getWidth() / (double) cameraImage.getWidth()));
            cameraImage = Bitmap.createScaledBitmap(cameraImage, cameraPreview.getWidth(), newHeight, true);

            cameraPreview.setImageBitmap(cameraImage);

        }
        else if (requestCode == MAP_REQUEST && resultCode == Activity.RESULT_OK) {
            LatLng location = (LatLng) data.getExtras().get("location");
            mapLocation = new GeoPoint(location.latitude, location.longitude);
            List<Address> addresses = null;
            try {
                addresses = new Geocoder(this.getContext()).getFromLocation(location.latitude, location.longitude,1);
            }
            catch (Exception e) {
                // do nothing
            }
            mapText.setVisibility(View.VISIBLE);
            try {
                String address = addresses.get(0).getAddressLine(0);
                mapText.setText(address);
            }
            catch (Exception e) {
                mapText.setText("Lat: " + String.format("%.3f", location.latitude) +", Lng: " + String.format("%.3f", location.longitude));
            }
        }
    }

    /**
     *
     * @param bitmap
     * Upload the bitmap to Firebase storage using a random image name
     * @return file name
     */
    public String uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UUID imageFileName = UUID.randomUUID();
        StorageReference storageRef = storage.getReferenceFromUrl(STORAGE_REF).child(imageFileName.toString()+".png");
        UploadTask task = storageRef.putBytes(data);
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return imageFileName.toString();
    }
    /**
     * Add the defined Emotion event to the firebase DB and reset the fields.
     * @param view
     */
    public void addEmote(View view) {
        EmotionEvent event;
        try {
            Date date = pickerToDate(textDateField.getText().toString(), textTimeField.getText().toString());
            String reasonString = textReasonField.getText().toString();
            Situation situation = Situation.values()[situationSpinner.getSelectedItemPosition()];
            Emotion emotion = Emotion.values()[emotionSpinner.getSelectedItemPosition()];
            if (cameraImage != null && mapLocation != null) {
                String fileName = uploadImage(cameraImage);
                event = new EmotionEvent(emotion, situation, reasonString, date, fileName, mapLocation);
            }
            else if (cameraImage != null && mapLocation == null) {
                String fileName = uploadImage(cameraImage);
                event = new EmotionEvent(emotion, situation, reasonString, date, fileName);
            }
            else if (cameraImage == null && mapLocation != null) {
                event = new EmotionEvent(emotion, situation, reasonString, date, mapLocation);
            }
            else{
                event = new EmotionEvent(emotion, situation, reasonString, date);
            }


        } catch (Exception e) {
            // TODO proper error messages
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            resetFields();
            return;
        }

        FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());
        fsh.addEmote(event);
        Toast.makeText(getContext(), "Emotion Event Added", Toast.LENGTH_LONG).show();
        resetFields();
    }

    /**
     * Resets the fields of the Fragment.
     */
    public void resetFields() {
        textReasonField.setText("");
        situationSpinner.setSelection(0);
        situationSpinner.setSelection(0);
        textDateField.setText("");
        textTimeField.setText("");
        setTimeAndDateListeners();
        cameraPreview.setImageResource(0);
        mapText.setText("");
        mapText.setVisibility(View.GONE);
        mapLocation = null;
    }

    /**
     * Converts date and time string to a Date object
     * to pass into the Emote object
     * @param date a string in the yyyy/mm/dd format
     * @param time a string in the hh:mm format
     * @return Date object built by the calendar
     */
    public Date pickerToDate(String date, String time) {
        Calendar calendar = Calendar.getInstance();

        String[] dateSplit = date.split("/");
        int dateInt = Integer.parseInt(dateSplit[2]);
        //Substract 1 since months are 0 indexed
        int monthInt = Integer.parseInt(dateSplit[1]) - 1;
        int yearInt = Integer.parseInt(dateSplit[0]);

        String[] timeSplit = time.split(":");
        int hour = Integer.parseInt(timeSplit[0]);
        int minute = Integer.parseInt(timeSplit[1]);

        calendar.set(Calendar.DATE, dateInt);
        calendar.set(Calendar.MONTH, monthInt);
        calendar.set(Calendar.YEAR, yearInt);

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime();
    }
}