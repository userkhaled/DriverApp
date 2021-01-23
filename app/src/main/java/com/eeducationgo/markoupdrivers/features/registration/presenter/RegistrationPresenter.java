package com.eeducationgo.markoupdrivers.features.registration.presenter;

import android.app.Activity;
import android.content.Intent;
import android.widget.EditText;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.main.view.MainActivity;
import com.eeducationgo.markoupdrivers.features.registration.view.RegistrationView;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationPresenter {
    private Activity activity;
    private RegistrationView view;

    public RegistrationPresenter(Activity activity, RegistrationView view) {
        this.activity = activity;
        this.view = view;
    }


    public void validationInput(TextInputEditText inputUserEmail, TextInputEditText inputPassword, FirebaseAuth auth) {
        clearError(inputUserEmail, inputPassword);
        if (AppShareData.checkIsEmptyEmailEdieText(inputUserEmail)) {
            AppShareData.errorInput(inputUserEmail, activity.getString(R.string.enter_email));
            return;
        }
        if (AppShareData.matcherTemplateEmailEditText(inputUserEmail)) {
            AppShareData.errorInput(inputUserEmail, activity.getString(R.string.matcher_email));
            return;
        }
        if (AppShareData.checkIsEmptyPasswordEdieText(inputPassword)) {
            AppShareData.errorInput(inputPassword, activity.getString(R.string.required_filed));
            return;
        }
        if (AppShareData.checkLengthOFTextPass(inputPassword)) {
            AppShareData.errorInput(inputPassword, activity.getString(R.string.password_matcher));
            return;
        }

        loginFirebase(inputUserEmail, inputPassword, auth);
    }

    private void loginFirebase(TextInputEditText inputUserEmail, TextInputEditText inputPassword, FirebaseAuth auth) {
        view.showProgress();
        String email = AppShareData.returnTextFromEditText(inputUserEmail);
        String password = AppShareData.returnTextFromEditText(inputPassword);
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    view.showSuccessMessage(task.toString());
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    view.hideProgress();
                    activity.finish();
                }
            }).addOnFailureListener(e -> {
                view.showErrorMessage(e.toString());
                view.hideProgress();
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage(e.toString());
            view.hideProgress();
        }
    }

    private void clearError(EditText inputUserEmail, EditText inputPassword) {
        inputUserEmail.setError(null);
        inputPassword.setError(null);
    }
}
