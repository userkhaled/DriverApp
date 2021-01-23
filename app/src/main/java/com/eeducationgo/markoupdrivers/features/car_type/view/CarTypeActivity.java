package com.eeducationgo.markoupdrivers.features.car_type.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.databinding.DataBindingUtil;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityCarTypeBinding;
import com.eeducationgo.markoupdrivers.features.car_type.presenter.CarTypePresenter;
import com.eeducationgo.markoupdrivers.features.id_user.view.IDActivity;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CarTypeActivity extends BaseActivity implements CarTypeView {

    private ActivityCarTypeBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceDriver;
    private CarTypePresenter presenter;
    private String carType, seatCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CarTypeActivity.this, R.layout.activity_car_type);

        init();
        initListener();

    }

    private void initListener() {
        carType();
        setCount();
        nextBtn();
    }

    private void nextBtn() {
        binding.buttonNextEmailRegisteration.setOnClickListener(v -> presenter.setCartData(databaseReferenceDriver, auth, carType, seatCount, binding.inputTelephoneNumberRegisteration));
    }

    private void setCount() {
        binding.spinnerSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seatCount = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void carType() {
        binding.spinnerCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init() {
        presenter = new CarTypePresenter(this, this);
        auth = FirebaseAuth.getInstance();
        databaseReferenceDriver = FirebaseDatabase.getInstance().getReference()
                .child(Constance.RootDriver).child(auth.getCurrentUser().getUid());
        databaseReferenceDriver.keepSynced(true);
    }

    @Override
    public void showSuccessMessage(String message) {
        super.showSuccessMessage(message);
        snackSuccessShow(binding.getRoot(), message);
    }

    @Override
    public void showErrorMessage(String message) {
        super.showErrorMessage(message);
        snackErrorShow(binding.getRoot(), message);
    }
}