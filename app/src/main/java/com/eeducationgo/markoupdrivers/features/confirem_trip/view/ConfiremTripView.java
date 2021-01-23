package com.eeducationgo.markoupdrivers.features.confirem_trip.view;

import android.location.Location;

import com.eeducationgo.markoupdrivers.util.BaseView;

public interface ConfiremTripView extends BaseView {
    void free(boolean b);

    void setLocation(Location location);
}
