package com.example.emote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.idling.CountingIdlingResource;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Long.MIN_VALUE;

/**
 * The Map Activity used by other activities to shower user the Google Maps framework
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;

    // set default location to University of Alberta if permission not granted
    private final LatLng DefaultLocation = new LatLng(53.523404, -113.526340);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Marker selectedMarker;
    private MapMode mode;
    private LatLng oldLocation;

    private Button ConfirmButton;
    private Button DeleteButton;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    private FireStoreHandler fsh = new FireStoreHandler(EmoteApplication.getUsername());;
    private FirebaseFirestore db = fsh.getFireStoreDBReference();
    private boolean personalHistory;


    /**
     * an enum to indicate whether the activity is under edit or viewing
     */
    public enum MapMode {
        EditLocation,
        ViewLocation,
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent intent = getIntent();
        mode = (MapMode)intent.getExtras().getSerializable("MAP_MODE");
        if (mode == MapMode.EditLocation) {
            try {
                // get the old location when editing
                oldLocation = (LatLng)intent.getExtras().get("location");
            } catch (Exception e){
                // do nothing
            }
        } else if (mode == MapMode.ViewLocation) {
            try {
                personalHistory = intent.getExtras().getBoolean("PERSONAL");
            } catch (Exception e) {
                // do nothing
            }
        }

        ConfirmButton = findViewById(R.id.confirm_button);
        ConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmButtonOnClickHandler(view);
            }
        });

        DeleteButton = findViewById(R.id.delete_button);
        DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteButtonOnClickHandler(view);
            }
        });

        mapFragment.getMapAsync(this);
    }

    /**
     * Method to get the latest location for all your personal events
     */
    private void getPersonalEventLocations() {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.EMOTE_COLLECTION)
                .whereEqualTo(EmotionEvent.USERNAME_KEY, fsh.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<EmotionEvent> new_emotes;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                EmotionEvent tempEvent = document.toObject(EmotionEvent.class);
                                GeoPoint tempPoint = tempEvent.getLocation();
                                if (tempPoint != null) {
                                    SetCustomMarker(tempEvent, new LatLng(tempPoint.getLatitude(), tempPoint.getLongitude()));
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        EmoteApplication.getIdlingResource().decrement();
                    }
                });
    }

    /**
     * Method to get the latest events location of all the people you follow
     */
    private void getFriendsEventLocations() {
        EmoteApplication.getIdlingResource().increment();
        db.collection(FireStoreHandler.FRIEND_COLLECTION)
                .document(EmoteApplication.getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            ArrayList<String> currentFriends = (ArrayList<String>) document.get("CURRENT_FRIENDS");
                            for (String friend : currentFriends) {
                                EmoteApplication.getIdlingResource().increment();
                                db.collection(FireStoreHandler.EMOTE_COLLECTION)
                                        .whereEqualTo(EmotionEvent.USERNAME_KEY, friend)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    Date max_date = new Date(MIN_VALUE);
                                                    EmotionEvent latest_event = null;
                                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                                        EmotionEvent current_emote = doc.toObject(EmotionEvent.class);
                                                        if (max_date.compareTo(current_emote.getDate()) <= 0) {
                                                            latest_event = current_emote;
                                                            max_date = latest_event.getDate();
                                                        }
                                                    }
                                                    if (latest_event != null) {
                                                        GeoPoint tempPoint = latest_event.getLocation();
                                                        if (tempPoint != null) {
                                                            SetCustomMarker(latest_event, new LatLng(tempPoint.getLatitude(), tempPoint.getLongitude()));
                                                        }
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                                EmoteApplication.getIdlingResource().decrement();
                                            }
                                        });
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        EmoteApplication.getIdlingResource().decrement();
                    }
                });
    }

    /**
     * A method to set custom markers for the map tickers
     * @param event, the emotion event sent to the function
     * @param location, the location to mark
     */
    private void SetCustomMarker(EmotionEvent event, LatLng location) {
        // load icon
        Context context = this.getApplicationContext();

        int emoticonIdentifier = context.getResources().getIdentifier(event.getEmote().toString()+"_EMOTICON", "string", context.getPackageName());
        String emotePath = context.getResources().getString(emoticonIdentifier);
        int emoticonId = context.getResources().getIdentifier(emotePath, "drawable", context.getPackageName());

        Bitmap bmap = BitmapFactory.decodeResource(getResources(), emoticonId);
        Bitmap resized = Bitmap.createScaledBitmap(bmap, 140, 140, false);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        String snippet = String.format("Date: %s", format.format(event.getDate()));

        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(resized))
                .position(location)
                .title(event.getUsername())).setSnippet(snippet);
    }

    /**
     * Handler that handles confirm button clicks
     * @param view
     */
    public void ConfirmButtonOnClickHandler(View view) {
        if (selectedMarker != null) {
            LatLng result = new LatLng(selectedMarker.getPosition().latitude, selectedMarker.getPosition().longitude);
            Bundle extras = new Bundle();
            extras.putParcelable("location", result);
            Intent intent = new Intent();
            intent.putExtras(extras);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Handler that handles delete button clicks
     * @param view
     */
    public void DeleteButtonOnClickHandler(View view) {
        if (selectedMarker != null) {
            selectedMarker.remove();
            selectedMarker = null;

            ConfirmButton.setVisibility(View.GONE);
            DeleteButton.setVisibility(View.GONE);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        getLocationPermission();

        getDeviceLocation();

        if (mode == MapMode.EditLocation) {
            if (oldLocation != null) {
                selectedMarker = mMap.addMarker(new MarkerOptions().position(oldLocation).title("Selected"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(oldLocation, DEFAULT_ZOOM));

                ConfirmButton.setVisibility(View.VISIBLE);
                DeleteButton.setVisibility(View.VISIBLE);
            }

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    if (selectedMarker != null) {
                        selectedMarker.remove();
                    }
                    selectedMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                    ConfirmButton.setVisibility(View.VISIBLE);
                    DeleteButton.setVisibility(View.VISIBLE);
                }
            });
        } else if (mode == MapMode.ViewLocation) {
            if (personalHistory == true) {
                getPersonalEventLocations();
            } else {
                getFriendsEventLocations();
            }
        }
    }

    /**
     * Requests location permission
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Method to get the device's current locations
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            if (oldLocation == null) {
                                mLastKnownLocation = task.getResult();
                                if(mLastKnownLocation != null) {
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                } else {
                                    mMap.moveCamera(CameraUpdateFactory
                                            .newLatLngZoom(DefaultLocation, DEFAULT_ZOOM));
                                }
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(DefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
