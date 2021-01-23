package com.eeducationgo.markoupdrivers.features.social.presenter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.name.view.NameActivity;
import com.eeducationgo.markoupdrivers.features.social.view.SocialView;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.concurrent.Executor;

public class SocialPresenter {
    private Activity activity;
    private SocialView view;
    private String TAG_CANCEL = "TAG";
    private String TAG_ERROR = "ERROR";
    private static final String TAG = "SocialPresenter";

    public SocialPresenter(Activity activity, SocialView socialView) {
        this.activity = activity;
        this.view = socialView;
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
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    view.showSuccessMessage(task.toString());
                    activity.startActivity(new Intent(activity, NameActivity.class));
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

    private void clearError(TextInputEditText inputUserEmail, TextInputEditText inputPassword) {
        inputUserEmail.setError(null);
        inputPassword.setError(null);
    }

    public void LoginManager(CallbackManager callbackManager, FirebaseAuth auth) {
        try {
            if (callbackManager == null) {
                view.hideProgress();
                return;
            }
            LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // setGraphRequest(loginResult);
                            checkTokenSignIn(auth);
                            view.hideProgress();
                        }

                        @Override
                        public void onCancel() {
                            Log.d(TAG_CANCEL, "On cancel");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Log.d(TAG_ERROR, error.toString());
                            view.showErrorMessage(error.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG_ERROR, e.toString());
            view.showErrorMessage(e.toString());
        }
    }//end

    private void checkTokenSignIn(FirebaseAuth auth) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.e("KEY", "" + accessToken.getToken());
        if (isLoggedIn) {
            AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(activity, task -> {
                        if (task.isSuccessful()) {
                            view.showSuccessMessage("success operation");
                            activity.startActivity(new Intent(activity, NameActivity.class));
                            activity.finish();
                        } else {

                        }


                    });
        } else {

            view.showErrorMessage(activity.getString(R.string.fb_excit));
        }
    }//end


    //todo this for google

    public void authByGoogle(String idToken, FirebaseAuth auth) {
        firebaseAuthWithGoogle(idToken, auth);
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken, FirebaseAuth auth) {
        // [START_EXCLUDE silent]
        view.showProgress();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        try {
            auth.signInWithCredential(credential)
                    .addOnCompleteListener((Executor) this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            view.hideProgress();
                            view.showSuccessMessage("signInWithCredential:success");
                            activity.startActivity(new Intent(activity, NameActivity.class));
                            activity.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            view.showSuccessMessage("Authentication Failed.");
                            view.hideProgress();
                        }

                        // [START_EXCLUDE]
                        view.hideProgress();
                        // [END_EXCLUDE]
                    });
        } catch (Exception e) {
            e.printStackTrace();
            view.showSuccessMessage(e.toString());
            view.hideProgress();
        }
    }
    // [END auth_with_google]
}
