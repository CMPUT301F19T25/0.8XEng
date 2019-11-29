package com.example.emote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.emote.ui.addemote.AddEmoteViewModel;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Activity used for editing emotion events
 */
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
    private ImageView preview;
    private Button locationButton;
    private Button addPhotoButton;

    FirebaseStorage storage;
    String STORAGE_REF = "gs://emote-f75ce.appspot.com";
    private static final int CAMERA_REQUEST = 1888;

    // arbitrary int for a map_request
    private static final int MAP_REQUEST = 1617;
    private GeoPoint location;

    private EmotionEvent emotionEvent;
    private TextView locationText;
    Bitmap cameraImage;

    /**
     * onCreate event handler to handle when activity starts, sets up the edit event activity
     * @param savedInstanceState: saved instance state passed to activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        initializeViews();

        Intent intent = getIntent();
        emotionEvent = (EmotionEvent) intent.getSerializableExtra("event");
        if (!intent.getBooleanExtra("editable", false)) {
            disableViews();
            addPhotoButton.setVisibility(View.GONE);
            locationButton.setVisibility(View.GONE);
            editButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }
        else{
            addPhotoButton.setVisibility(View.VISIBLE);
            locationButton.setVisibility(View.VISIBLE);
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
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

    /**
     * Open up an activity for a user to pick up a location
     * @param view
     */
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

    /**
     * Get the result from the map activity, check if it's valid, and set the text in edit view
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST && resultCode == Activity.RESULT_OK) {
            LatLng result = (LatLng) data.getExtras().get("location");
            location = new GeoPoint(result.latitude, result.longitude);
            SetLocationText();
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            cameraImage = (Bitmap) data.getExtras().get("data");

            int newHeight = (int) Math.floor((double) cameraImage.getHeight() * ((double) preview.getWidth() / (double) cameraImage.getWidth()));
            cameraImage = Bitmap.createScaledBitmap(cameraImage, preview.getWidth(), newHeight, true);

            preview.setImageBitmap(cameraImage);

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
     * Private method to set the location text
     */
    private void SetLocationText() {
        if (location != null) {
            locationText.setVisibility(View.VISIBLE);
            List<Address> addresses = null;
            try {
                addresses = new Geocoder(this).getFromLocation(location.getLatitude(), location.getLongitude(),1);
            }
            catch (Exception e) {
                // do nothing
            }
            try {
                String address = addresses.get(0).getAddressLine(0);
                locationText.setText(address);
            }
            catch (Exception e) {
                locationText.setText("Lat: " + String.format("%.3f", location.getLatitude()) +", Lng: " + String.format("%.3f", location.getLongitude()));
            }
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
        preview = this.findViewById(R.id.cameraPreview);
        locationButton = this.findViewById(R.id.addLocationButton);
        locationText = this.findViewById(R.id.locationText);
        addPhotoButton = this.findViewById(R.id.addPhotoButton);

        storage = FirebaseStorage.getInstance();

        situationSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Situation.getStrings(this)));
        emotionSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Emotion.getStrings(this)));

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPicture(view);
            }
        });
    }

    /**
     * View to add pictures to events, using the camera
     * @param view: view to use for the edit event 
     */
    public void addPicture(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

        StorageReference storageRef = storage.getReferenceFromUrl(STORAGE_REF).child(emotionEvent.getImageFile()+".png");

        if (storageRef == null || emotionEvent.getImageFile() == null){
            preview.setImageResource(0);
        }
        else {
            Glide.with(getApplicationContext()).using(new FirebaseImageLoader()).load(storageRef).into(preview);
        }



        SetLocationText();

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

            if (cameraImage != null) {
                String fileName = uploadImage(cameraImage);
                emotionEvent.setImageFile(fileName);
            }

        } catch (Exception e) {
            // TODO proper error messages
            Toast.makeText(EditEventActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }

        fsh.removeEmote(emotionEvent.getFireStoreDocumentID());
        fsh.addEmote(emotionEvent);
    }


}
