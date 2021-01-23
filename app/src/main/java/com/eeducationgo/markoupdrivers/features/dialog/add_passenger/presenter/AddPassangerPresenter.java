package com.eeducationgo.markoupdrivers.features.dialog.add_passenger.presenter;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.dialog.add_passenger.view.AddPassangerFragment;
import com.eeducationgo.markoupdrivers.features.dialog.add_passenger.view.AddPassangerView;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class AddPassangerPresenter {
    private AddPassangerFragment fragment;
    private AddPassangerView view;

    public AddPassangerPresenter(AddPassangerFragment fragment, AddPassangerView view) {
        this.fragment = fragment;
        this.view = view;
    }

    public void addPassanger(FirebaseAuth auth, DatabaseReference databaseReferenceRoot,
                             TextInputEditText inputUserName, TextInputEditText inputPrice) {

        setErrorNull(inputUserName, inputPrice);
        if (AppShareData.checkIsEmptyEmailEdieText(inputUserName)) {
            AppShareData.errorInput(inputUserName, fragment.getString(R.string.required_filed));
            return;
        }
        if (AppShareData.checkIsEmptyEmailEdieText(inputPrice)) {
            AppShareData.errorInput(inputPrice, fragment.getString(R.string.required_filed));
            return;
        }
        addData(inputPrice, inputUserName, auth, databaseReferenceRoot);
    }

    private void addData(TextInputEditText inputPrice, TextInputEditText inputUserName,
                         FirebaseAuth auth, DatabaseReference databaseReferenceRoot) {
        view.showProgress();
        Map<String, Object> map = new HashMap<>();
        map.put(Constance.ChildBookingListUserName, AppShareData.returnTextFromEditText(inputUserName));
        map.put(Constance.ChildBookingListPrice, AppShareData.returnTextFromEditText(inputPrice));
        try {
            databaseReferenceRoot.push()
                    .setValue(map)
                    .addOnSuccessListener(aVoid -> {
                        view.hideProgress();
                        view.showSuccessMessage(fragment.getString(R.string.success_process));
                        fragment.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        view.hideProgress();
                        view.showErrorMessage(e.toString());
                    });
        } catch (Exception e) {
            e.printStackTrace();
            view.hideProgress();
            view.showErrorMessage(e.toString());
        }

    }

    private void setErrorNull(TextInputEditText inputUserName, TextInputEditText inputPrice) {
        inputUserName.setError(null);
        inputPrice.setError(null);
    }
}
