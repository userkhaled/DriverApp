package com.eeducationgo.markoupdrivers.features.confirem_trip.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.confirem_trip.view.ConfiremTripView;
import com.eeducationgo.markoupdrivers.features.registration.model.User;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ConfiremTripPresenter {
    private Activity activity;
    private ConfiremTripView view;
    private static final int REQUEST_CODE = 100;

    public ConfiremTripPresenter(Activity activity, ConfiremTripView view) {
        this.activity = activity;
        this.view = view;
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

        } else {
            return true;
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public void getLastKnownLocation(FusedLocationProviderClient fusedLocationProviderClient) {

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                //TODO work
                location.getLatitude();
                location.getLongitude();
                Log.d(TAG, "getLastKnownLocation: " + location.getLatitude() + " \n" + location.getLongitude());
                //showLocationInTextView(location);
                view.setLocation(location);
            }
        });
    }
    private static final String TAG = "ConfiremTripPresenter";

    public void updateBookingStat(boolean isChecked, DatabaseReference databaseReferenceDriver) {
        // view.showProgress();
        Map<String, Object> map = new HashMap<>();
        map.put(Constance.ChildDriverIsAvailable, isChecked);
        try {
            databaseReferenceDriver.updateChildren(map)
                    .addOnCompleteListener(task -> {
                        if (!task.isComplete()) {
                            view.hideProgress();
                            view.showErrorMessage(task.toString());
                            return;
                        }
                       // view.showSuccessMessage(activity.getString(R.string.success_process));
                        view.hideProgress();
                    })
                    .addOnFailureListener(e -> {
                        view.showErrorMessage(e.getMessage());
                        view.hideProgress();
                        Log.d(TAG, "updateBookingStat: " + e.toString());
                    });

        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage(e.getMessage());
            view.hideProgress();
            Log.d(TAG, "updateBookingStat: " + e.toString());
        }
    }

    public void checkIsFree(DatabaseReference databaseReferenceDriver) {
        try {
            databaseReferenceDriver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getIsAvailable()) {
                            view.free(true);
                        } else {
                            view.free(false);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    view.showErrorMessage(error.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage( e.getMessage());
        }
    }

    public void updateLocation(DatabaseReference databaseReferenceDriver, Location location) {
        //view.showProgress();
        Map<String, Object> map = new HashMap<>();
        map.put(Constance.ChildDriverLate, location.getLatitude());
        map.put(Constance.ChildDriverLong, location.getLongitude());
        try {
            databaseReferenceDriver
                    .updateChildren(map)
                    .addOnSuccessListener(aVoid -> {
                       // view.hideProgress();
                    })
                    .addOnFailureListener(e -> {
                      //  view.hideProgress();
                      //  view.showErrorMessage(e.toString());
                    });
        } catch (Exception e) {
            e.printStackTrace();
           // view.hideProgress();
           // view.showErrorMessage(e.toString());
        }
    }
}
