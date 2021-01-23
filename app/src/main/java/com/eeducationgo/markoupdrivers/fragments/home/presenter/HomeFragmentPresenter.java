package com.eeducationgo.markoupdrivers.fragments.home.presenter;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.confirem_trip.view.ConfiremTripActivity;
import com.eeducationgo.markoupdrivers.fragments.home.view.HomeFragment;
import com.eeducationgo.markoupdrivers.fragments.home.view.HomeFragmentView;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragmentPresenter {
    private HomeFragmentView view;
    private HomeFragment fragment;

    public HomeFragmentPresenter(HomeFragmentView view, HomeFragment fragment) {
        this.view = view;
        this.fragment = fragment;
    }


    public void setData(TextView tvDate) {
        String format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(new Date());
        tvDate.setText(format);
    }
    public void updateDestination(DatabaseReference databaseReferenceDriver, String destination, String station, String tripTime, String tripDate) {
        if (TextUtils.isEmpty(station)) {
            view.showErrorMessage(fragment.getString(R.string.enter_station));
            return;
        }
        if (TextUtils.isEmpty(destination)) {
            view.showErrorMessage(fragment.getString(R.string.enter_destination));
            return;
        }
        if (TextUtils.isEmpty(tripTime)) {
            view.showErrorMessage(fragment.getString(R.string.choose_trip_time));
            return;
        }
        if (TextUtils.isEmpty(tripDate)) {
            view.showErrorMessage(fragment.getString(R.string.choose_trip_time));
            return;
        }

        update(databaseReferenceDriver , destination , station , tripTime , tripDate);
    }

    private void update(DatabaseReference databaseReferenceDriver, String destination, String station, String tripTime, String tripDate) {
        view.showProgress();
        Map<String , Object> map = new HashMap<>();
        map.put(Constance.ChildDriverDestination , destination);
        map.put(Constance.ChildDriverStation , station);
        map.put(Constance.ChildDriverTripTime , tripTime);
        map.put(Constance.ChildDriverTripDate , tripDate);

        try {
            databaseReferenceDriver.updateChildren(map)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()){
                            view.hideProgress();
                            view.showErrorMessage(task.toString());
                            return;
                        }
                        view.hideProgress();
                        view.showSuccessMessage(fragment.getString(R.string.success_process));
                        //todo intent
                        fragment.startActivity(new Intent(fragment.getContext() , ConfiremTripActivity.class));
                    }).
                    addOnFailureListener(e -> {
                        view.hideProgress();
                        view.showErrorMessage(e.toString());
                    });
        } catch (Exception e) {
            e.printStackTrace();
            view.hideProgress();
            view.showErrorMessage(e.toString());
        }
    }
}
