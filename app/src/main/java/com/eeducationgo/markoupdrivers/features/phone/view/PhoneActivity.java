package com.eeducationgo.markoupdrivers.features.phone.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityPhoneBinding;
import com.eeducationgo.markoupdrivers.features.code.CodeVarActivity;
import com.eeducationgo.markoupdrivers.features.phone.presenter.PhonePresenter;
import com.eeducationgo.markoupdrivers.features.social.view.SocialActivity;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneActivity extends BaseActivity implements PhoneView {
    private ActivityPhoneBinding binding;
    public static String mVerificationId;
    public static PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private PhonePresenter presenter;
    private static final String TAG = "PhoneActivity";
    private String indComp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(PhoneActivity.this, R.layout.activity_phone);

        init();
        initListener();
    }

    private void init() {
        indComp = getIntent().getStringExtra(Constance.typeKey);
        mAuth = FirebaseAuth.getInstance();
        presenter = new PhonePresenter(this, this);
    }

    private void initListener() {
        onSendBtnClick();
        skipBtn();
    }

    private void skipBtn() {
        binding.buttonSkipEmailRegesteration.setOnClickListener(v -> startActivity(new Intent(getBaseContext(), SocialActivity.class)
                .putExtra(Constance.typeKey, indComp)));
    }


    private void onSendBtnClick() {
        binding.buttonNextRegisterNewAccount.setOnClickListener(view -> {
            if (AppShareData.isEmptyEditText(binding.inputTelephoneNumberRegisteration)) {
                AppShareData.errorInput(binding.inputTelephoneNumberRegisteration, getString(R.string.required_filed));
                return;
            }
            String phoneNumber = AppShareData.returnTextFromEditText(binding.inputTelephoneNumberRegisteration);
            showProgress();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60,
                    TimeUnit.SECONDS,
                    PhoneActivity.this,
                    mCallbacks);
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                hideProgress();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                snackErrorShow(binding.getRoot(), e.toString() + " " + e.hashCode());
                hideProgress();
            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;
                Toast.makeText(getBaseContext(), "the code is send", Toast.LENGTH_SHORT).show();
                if (!TextUtils.isEmpty(indComp) &&indComp.equalsIgnoreCase("Comp")) {
                    AppShareData.setUserType(getString(R.string.companies));
                } else {
                    AppShareData.setUserType(getString(R.string.individuals));
                }
                startActivity(new Intent(getBaseContext(), CodeVarActivity.class).putExtra("id", mVerificationId));
                hideProgress();
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        try {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            hideProgress();
                            if (!TextUtils.isEmpty(indComp) &&indComp.equalsIgnoreCase("Comp")) {
                                AppShareData.setUserType(getString(R.string.companies));
                            } else {
                                AppShareData.setUserType(getString(R.string.individuals));
                            }
                            startActivity(new Intent(getBaseContext(), CodeVarActivity.class));
                            finish();
                        } else {
                            hideProgress();
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
}