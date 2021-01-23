package com.eeducationgo.markoupdrivers.features.code;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityCodeVarBinding;
import com.eeducationgo.markoupdrivers.features.name.view.NameActivity;
import com.eeducationgo.markoupdrivers.features.social.view.SocialActivity;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.ProgressBarDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class CodeVarActivity extends BaseActivity implements CodeVarView {

    private ActivityCodeVarBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "CodeVarActivity";
    private ProgressBarDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CodeVarActivity.this, R.layout.activity_code_var);


        init();
        initListener();

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressBarDialog(this);
    }

    private void initListener() {
        //otpComplet();
        btnClicekd();
        skipBtn();
    }

    private void skipBtn() {
        binding.buttonSkipEmailRegesteration.setOnClickListener(v -> startActivity(new Intent(getBaseContext() , SocialActivity.class)));
    }
    private void btnClicekd() {

        binding.buttonNextRegisterNewAccount.setOnClickListener(v -> otpComplet());
    }

    private void otpComplet() {
        Log.d(TAG, "btnClicekd: clicked \n "+getIntent().getStringExtra("id"));
        binding.otpView.setOtpCompletionListener(otp -> {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getIntent().getStringExtra("id"), otp);
            signInWithPhoneAuthCredential(credential);
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showProgress();
        try {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            hideProgress();
                            startActivity(new Intent(getBaseContext(), NameActivity.class));
                            finish();
                        } else {
                            hideProgress();
                            snackErrorShow(binding.getRoot(), task.toString() + " " + task.hashCode());
                        }
                    }).addOnFailureListener(e -> {
                hideProgress();
                snackErrorShow(binding.getRoot(), e.toString() + " " + e.hashCode());
            });
        } catch (Exception e) {
            e.printStackTrace();
            hideProgress();
            snackErrorShow(binding.getRoot(), e.toString() + " " + e.hashCode());
            Log.d(TAG, "signInWithPhoneAuthCredential: ");
        }

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
    public void showProgress() {
        super.showProgress();
        dialog.show();
    }

    @Override
    public void hideProgress() {
        super.hideProgress();
        dialog.hide();
    }
}