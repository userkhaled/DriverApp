package com.eeducationgo.markoupdrivers.features.car_type.presenter;

import android.app.Activity;
import android.content.Intent;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.car_type.view.CarTypeView;
import com.eeducationgo.markoupdrivers.features.id_user.view.IDActivity;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class CarTypePresenter {

    private Activity activity;
    private CarTypeView view;

    public CarTypePresenter(Activity activity, CarTypeView carTypeView) {
        this.activity = activity;
        this.view = carTypeView;
    }

    public void setCartData(DatabaseReference databaseReferenceDriver, FirebaseAuth auth, String carType,
                            String seatCount, TextInputEditText inputTelephoneNumberRegisteration) {

        errorNull(inputTelephoneNumberRegisteration);
        if (AppShareData.checkIsEmptyString(seatCount) &&
                AppShareData.checkIsEmptyString(carType)
        ) {
            view.showErrorMessage("there are empty field");
            return;
        }
        if (AppShareData.isEmptyEditText(inputTelephoneNumberRegisteration)) {
            AppShareData.errorInput(inputTelephoneNumberRegisteration, activity.getString(R.string.required_filed));
            return;
        }

        setData(databaseReferenceDriver, auth, carType, seatCount, inputTelephoneNumberRegisteration);

    }

    private void setData(DatabaseReference databaseReferenceDriver, FirebaseAuth auth, String carType,
                         String seatCount, TextInputEditText inputEditText) {
        view.showProgress();
        Map<String, Object> map = new HashMap<>();
        map.put(Constance.ChildDriverVehicleNumber, AppShareData.returnTextFromEditText(inputEditText));
        map.put(Constance.ChildDriverVehicleType, carType);
        map.put(Constance.ChildDriverNumberSeats, seatCount);
        try {
            databaseReferenceDriver.updateChildren(map).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    view.showErrorMessage(task.toString());
                    view.hideProgress();
                    return;
                }
                view.showSuccessMessage(activity.getString(R.string.success_process));
                activity.startActivity(new Intent(activity, IDActivity.class));
                activity.finish();
                view.hideProgress();
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

    private void errorNull(TextInputEditText inputTelephoneNumberRegisteration) {
        inputTelephoneNumberRegisteration.setError(null);
    }
}
