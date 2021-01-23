package com.eeducationgo.markoupdrivers.features.varifcation.view;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;


import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityVerificationBinding;
import com.eeducationgo.markoupdrivers.features.dialog.edit.view.EditDialogFragment;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class VerificationActivity extends BaseActivity {

    private ActivityVerificationBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(VerificationActivity.this, R.layout.activity_verification);
        init();
        initListener();
    }

    private void initListener() {
        btnEmail();
        btnId();
    }

    private void btnId() {
        binding.tvVerfiyId.setOnClickListener(v -> {
            EditDialogFragment editDialogFragment = new EditDialogFragment();
            editDialogFragment.show(getSupportFragmentManager(), editDialogFragment.getTag());
        });
    }

    private void btnEmail() {
        binding.tvVerfiy.setOnClickListener(v -> {
            showProgress();
            try {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        snackSuccessShow(binding.getRoot(), "the email  send");
                        hideProgress();
                    }
                }).addOnFailureListener(e -> {
                    snackErrorShow(binding.getRoot(), e.getMessage());
                    hideProgress();
                });
            } catch (Exception e) {
                e.printStackTrace();
                snackErrorShow(binding.getRoot(), e.getMessage());
                hideProgress();
            }
        });
    }

    private void init() {
        auth = FirebaseAuth.getInstance();

    }

}