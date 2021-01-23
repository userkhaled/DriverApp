package com.eeducationgo.markoupdrivers.features.confirem_trip.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityConfiremTripBinding;
import com.eeducationgo.markoupdrivers.features.confirem_trip.presenter.ConfiremTripPresenter;
import com.eeducationgo.markoupdrivers.features.controll.view.ControleActivity;
import com.eeducationgo.markoupdrivers.features.main.view.MainActivity;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConfiremTripActivity extends BaseActivity implements OnMapReadyCallback, ConfiremTripView {

    private ActivityConfiremTripBinding binding;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mapFragment;
    private ConfiremTripPresenter presenter;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceDriver;
    private boolean isChecked = false;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Polyline mPolyline;
    private Marker UserMarker;
    private static final int REQUEST_CODE = 100;
    private static final int REQUEST_CHECK_SETTING = 2;
    private static int AUTOCOMPLETE_REQUEST_CODE = 3;
    private static final String TAG = "ConfiremTripActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ConfiremTripActivity.this, R.layout.activity_confirem_trip);


        init();
        initListener();
    }

    @SuppressLint("MissingPermission")
    private void initListener() {
        controlPanelClicked();
        onOfBooking();
        checkIsFree();
        binding.textControlPanel.setOnClickListener(v -> startActivity(new Intent(getBaseContext() , ControleActivity.class)));
    }

    private void checkIsFree() {
        presenter.checkIsFree(databaseReferenceDriver);
    }

    private void onOfBooking() {
        binding.switchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            this.isChecked = isChecked;
            updateBooking();
        });
    }

    private void updateBooking() {
        presenter.updateBookingStat(isChecked, databaseReferenceDriver);
    }

    private void controlPanelClicked() {
        binding.textControlPanel.setOnClickListener(v -> {
            //todo control panel
        });
    }

    private void init() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        presenter = new ConfiremTripPresenter(this, this);
        auth = FirebaseAuth.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getBaseContext());
        locationRequest = new LocationRequest();
        databaseReferenceDriver = FirebaseDatabase.getInstance().getReference()
                .child(Constance.RootDriver).child(auth.getCurrentUser().getUid());
        databaseReferenceDriver.keepSynced(true);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mGoogleMap.setIndoorEnabled(true);
        UiSettings uiSettings = mGoogleMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        mGoogleMap.setMyLocationEnabled(true);
        mPolyline = mGoogleMap.addPolyline(new PolylineOptions());

//        UserMarker = mGoogleMap.addMarker(new MarkerOptions()
//                .position(new LatLng());
    }

    @Override
    public void showErrorMessage(String message) {
        super.showErrorMessage(message);
        snackErrorShow(binding.getRoot(), message);
    }

    @Override
    public void showSuccessMessage(String message) {
        super.showSuccessMessage(message);
        snackSuccessShow(binding.getRoot(), message);
    }

    @Override
    public void free(boolean b) {
        binding.switchBtn.setChecked(b);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkLocationPermission()) {
            getLastKnownLocation();
            checkUserLocationSittings();
        }
    }

    private boolean checkLocationPermission() {
        return presenter.checkLocationPermission();
    }//end

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //TODO:DONE
                    getLastKnownLocation();
                    checkUserLocationSittings();
                }
            }
            return;
        }
    }//end

    private void getLastKnownLocation() {
        presenter.getLastKnownLocation(fusedLocationProviderClient);
    }//end

    private void checkUserLocationSittings() {

        try {
            LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                    .addLocationRequest(getLocationRequest()).build();
            SettingsClient client = LocationServices.getSettingsClient(this);
            Task<LocationSettingsResponse> responseTask = client.checkLocationSettings(request);
            responseTask.addOnSuccessListener(locationSettingsResponse -> startTracking());
            responseTask.addOnFailureListener(e -> {
                int statusCode = ((ApiException) e).getStatusCode();
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {

                    try {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        resolvableApiException.startResolutionForResult(ConfiremTripActivity.this, REQUEST_CHECK_SETTING);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void startTracking() {

        fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.getMainLooper());
    }//end

    //TODO create location call back for request location frequently
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                //todo to get location from firebase
                //todo to upload location to firebase
                Log.d(TAG, "onLocationResult: " + location.getLatitude() + " " + location.getLongitude() + " " + DateFormat.getTimeInstance().format(new Date()));

//                UserMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
//                List<LatLng> points = mPolyline.getPoints();
//                points.add(new LatLng(location.getLatitude(), location.getLongitude()));
//                mPolyline.setPoints(points);
                 //getLastKnownLocation();
                //updateLocation(location, databaseReferenceDriver);

                presenter.updateLocation(databaseReferenceDriver, location);
            }
        }
    };




    private void updateLocation(Location location, DatabaseReference databaseReference) {
        new AsyncLoadLocation(location, databaseReference).execute();
    }

    private void stopLocationsTrack() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest getLocationRequest() {

        locationRequest.setInterval(300 * 1000);
        locationRequest.setFastestInterval(120 * 1000);
//        locationRequest.setSmallestDisplacement(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTING) {
            if (resultCode == RESULT_CANCELED) {
                stopLocationsTrack();
            } else if (resultCode == RESULT_OK) {
                startTracking();
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationsTrack();
    }

    @Override
    public void setLocation(Location location) {
        Log.d(TAG, "setLocation: " + location.getLongitude() + " " + location.getLatitude());
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));

        presenter.updateLocation(databaseReferenceDriver, location);
//        UserMarker = mGoogleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(location.getLatitude(), location.getLongitude())));

    }

    public static class AsyncLoadLocation extends AsyncTask<Location, DatabaseReference, String> {
        private Location location;
        private DatabaseReference databaseReferenceDriver;

        public AsyncLoadLocation(Location location, DatabaseReference databaseReferenceDriver) {
            this.databaseReferenceDriver = databaseReferenceDriver;
            this.location = location;
        }

        @Override
        protected String doInBackground(Location... locations) {
            Map<String, Object> map = new HashMap<>();
            map.put(Constance.ChildDriverLate, location.getLatitude());
            map.put(Constance.ChildDriverLong, location.getLongitude());
            databaseReferenceDriver
                    .updateChildren(map);

            return null;
        }
    }
}