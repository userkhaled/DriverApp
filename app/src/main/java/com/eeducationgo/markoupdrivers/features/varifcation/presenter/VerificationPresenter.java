package com.eeducationgo.markoupdrivers.features.varifcation.presenter;

import android.app.Activity;

import com.eeducationgo.markoupdrivers.features.varifcation.view.VerificationView;


public class VerificationPresenter {
    private Activity activity;
    private VerificationView view;

    public VerificationPresenter(Activity activity, VerificationView view) {
        this.activity = activity;
        this.view = view;
    }
}
