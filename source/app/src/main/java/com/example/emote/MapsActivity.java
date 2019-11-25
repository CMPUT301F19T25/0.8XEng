package com.example.emote;

/**
 * most of the basic code used here are from tutorials from google
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

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
//    private ArrayList<Marker> events;

    // a enum to indicate whether the activity is under edit or viewing
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

    private void getPersonalEventLocations() {
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
                    }
                });
    }

    private void getFriendsEventLocations() {
        // TODO: get latest emotion event from friends
    }

    private void SetCustomMarker(EmotionEvent event, LatLng location) {
        // TODO: set custom markers that can display user, date, and emote
        mMap.addMarker(new MarkerOptions()
                .position(location));
    }

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
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
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
