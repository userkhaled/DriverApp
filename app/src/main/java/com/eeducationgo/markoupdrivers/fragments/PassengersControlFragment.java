package com.eeducationgo.markoupdrivers.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eeducationgo.markoupdrivers.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PassengersControlFragment extends Fragment {
    FloatingActionButton fabAddPassenger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_passengers_control, container, false);

        fabAddPassenger = root.findViewById(R.id.fab_add_passenger);
        fabAddPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_passengersControlFragment_to_addPassengerFragment);
            }
        });
        return root;
    }
}