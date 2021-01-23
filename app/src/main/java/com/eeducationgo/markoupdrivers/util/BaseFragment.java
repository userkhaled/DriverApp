package com.eeducationgo.markoupdrivers.util;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eeducationgo.markoupdrivers.R;
import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment implements BaseView {
    private Snackbar snackbar;


    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time, container, false);


        return view;
    }

    public void snackErrorShow(View view, String text) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setDuration(4000);
        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
        snackbar.show();
    }

    public void snackSuccessShow(View view, String text) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setDuration(4000);
        snackbar.setBackgroundTint(Color.parseColor("#00ff00"));
        snackbar.show();
    }
    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showErrorMessage(String message) {

    }
    @Override
    public void showSuccessMessage(String message) {

    }
}
