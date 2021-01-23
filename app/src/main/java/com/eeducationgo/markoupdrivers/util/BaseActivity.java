package com.eeducationgo.markoupdrivers.util;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity implements BaseView {

    public ProgressBarDialog dialog;
    public Snackbar snackbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressBarDialog(this);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setMessage(" please wait ...");
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
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void showSuccessMessage(String message) {

    }
}
