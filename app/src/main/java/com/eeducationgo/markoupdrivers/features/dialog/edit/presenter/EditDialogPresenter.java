package com.eeducationgo.markoupdrivers.features.dialog.edit.presenter;

import android.net.Uri;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.dialog.edit.view.EditDialogFragment;
import com.eeducationgo.markoupdrivers.features.dialog.edit.view.EditDialogView;
import com.eeducationgo.markoupdrivers.features.registration.model.User;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditDialogPresenter {
    private EditDialogFragment fragment;
    private EditDialogView view;

    public EditDialogPresenter(EditDialogFragment fragment, EditDialogView view) {
        this.fragment = fragment;
        this.view = view;
    }

    public void updateData(TextInputEditText inputUserEmail, TextInputEditText inputUserName, TextInputEditText inputUserPhone,
                           StorageReference storageReferenceDriver, FirebaseAuth auth, DatabaseReference databaseReferenceDriver, Uri resultUri) {

        setErrorNull(inputUserEmail, inputUserName, inputUserPhone);
        if (AppShareData.checkIsEmptyEmailEdieText(inputUserEmail)) {
            AppShareData.errorInput(inputUserEmail, fragment.getString(R.string.enter_email));
            return;
        }
        if (AppShareData.matcherTemplateEmailEditText(inputUserEmail)) {
            AppShareData.errorInput(inputUserEmail, fragment.getString(R.string.matcher_email));
            return;
        }
        if (AppShareData.checkIsEmptyPasswordEdieText(inputUserName)) {
            AppShareData.errorInput(inputUserName, fragment.getString(R.string.required_filed));
            return;
        }

        if (AppShareData.checkIsEmptyPasswordEdieText(inputUserPhone)) {
            AppShareData.errorInput(inputUserPhone, fragment.getString(R.string.required_filed));
            return;
        }



        updateDataChild(resultUri, auth, databaseReferenceDriver, inputUserEmail, inputUserName, inputUserPhone, storageReferenceDriver);
    }

    private void updateDataChild(Uri resultUri, FirebaseAuth auth, DatabaseReference databaseReferenceDriver, TextInputEditText inputUserEmail, TextInputEditText inputUserName, TextInputEditText inputUserPhone, StorageReference storageReferenceDriver) {
        view.showProgress();
        String uid = auth.getCurrentUser().getUid();
        try {
            if (resultUri == null) {

                String name = AppShareData.returnTextFromEditText(inputUserName);
                String email = AppShareData.returnTextFromEditText(inputUserEmail);
                String phone = AppShareData.returnTextFromEditText(inputUserPhone);
                Map<String, Object> map = new HashMap<>();
                map.put(Constance.ChildDriverEmail, email);
                map.put(Constance.ChildDriverName, name);
                map.put(Constance.ChildDriverPhone, phone);
                databaseReferenceDriver.updateChildren(map).addOnCompleteListener(task1 -> {
                    if (!task1.isSuccessful()) {
                        view.hideProgress();
                        view.showErrorMessage(task1.toString());
                        return;
                    }
                    view.showSuccessMessage(fragment.getString(R.string.success_process));
                    fragment.dismiss();
                    view.hideProgress();
                }).addOnFailureListener(e -> {
                    view.hideProgress();
                    view.showErrorMessage(e.toString());
                });
                view.hideProgress();
            } else {
                StorageReference storageReference = storageReferenceDriver.child(uid).child(resultUri.getLastPathSegment() + ".jpg");
                storageReference.putFile(resultUri).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        view.hideProgress();
                        view.showErrorMessage(task.toString());
                        return;
                    }
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String url = uri.toString();
                        String name = AppShareData.returnTextFromEditText(inputUserName);
                        String email = AppShareData.returnTextFromEditText(inputUserEmail);
                        String phone = AppShareData.returnTextFromEditText(inputUserPhone);
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constance.ChildDriverEmail, email);
                        map.put(Constance.ChildDriverImage, url);
                        map.put(Constance.ChildDriverName, name);
                        map.put(Constance.ChildDriverPhone, phone);
                        databaseReferenceDriver.updateChildren(map).addOnCompleteListener(task1 -> {
                            if (!task1.isSuccessful()) {
                                view.hideProgress();
                                view.showErrorMessage(task1.toString());
                                return;
                            }
                            view.showSuccessMessage(fragment.getString(R.string.success_process));
                            fragment.dismiss();
                        }).addOnFailureListener(e -> {
                            view.hideProgress();
                            view.showErrorMessage(e.toString());
                        });
                    }).addOnFailureListener(
                            e -> {
                                view.hideProgress();
                                view.showErrorMessage(e.toString());
                            }
                    );

                }).addOnFailureListener(e -> {
                    view.hideProgress();
                    view.showErrorMessage(e.toString());
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            view.hideProgress();
            view.showErrorMessage(e.toString());
        }
    }

    private void setErrorNull(TextInputEditText inputUserEmail, TextInputEditText inputUserName,
                              TextInputEditText inputUserPhone) {
        inputUserEmail.setError(null);
        inputUserName.setError(null);
        inputUserPhone.setError(null);
    }

    public void setViewData(DatabaseReference databaseReferenceDriver,
                            TextInputEditText inputUserEmail, TextInputEditText inputUserName,
                            TextInputEditText inputUserPhone, CircleImageView civUserImage, TextView tvTitle) {
        try {
            databaseReferenceDriver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        view.showErrorMessage(fragment.getString(R.string.error));
                        return;
                    }
                    if (fragment.getActivity() == null) {
                        return;
                    }
                    User user = snapshot.getValue(User.class);
                    inputUserEmail.setText(user.getEmail());
                    inputUserPhone.setText(user.getPhone());
                    inputUserName.setText(user.getName());
                    tvTitle.setText(user.getName());
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.holder);
                    Glide.with(fragment).load(user.getImage()).apply(requestOptions).into(civUserImage);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    view.hideProgress();
                    view.showErrorMessage(error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.hideProgress();
            view.showErrorMessage(e.toString());
        }
    }
}
