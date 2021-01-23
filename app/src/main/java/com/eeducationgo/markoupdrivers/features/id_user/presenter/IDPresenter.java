package com.eeducationgo.markoupdrivers.features.id_user.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.main.view.MainActivity;
import com.eeducationgo.markoupdrivers.features.id_user.view.IDView;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class IDPresenter {

    private Activity activity;
    private IDView view;
    private static final String TAG = "IDPresenter";

    public IDPresenter(Activity activity, IDView view) {
        this.activity = activity;
        this.view = view;
    }

    public void uploadImageUser(Uri resultUri, FirebaseAuth auth, DatabaseReference databaseReferenceUserImage,
                                StorageReference storageReferenceUserImage) {
        view.showProgress();
        if (resultUri == null) {
            view.hideProgress();
            return;
        }
        String uid = auth.getCurrentUser().getUid();
        try {
            StorageReference reference = storageReferenceUserImage.child(uid).child(resultUri.getLastPathSegment() + ".jpg");
            reference.putFile(resultUri).addOnSuccessListener(taskSnapshot -> {
                reference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String urlImage = uri.toString();
                            Map<String , Object> map = new HashMap<>();
                            map.put(Constance.ChildDriverImage , urlImage);
                            databaseReferenceUserImage.updateChildren(map).addOnCompleteListener(task -> {
                                view.hideProgress();
                                view.showSuccessMessage(activity.getString(R.string.success_process));
                                activity.startActivity(new Intent(activity , MainActivity.class));
                                activity.finish();
                            }).addOnFailureListener(e -> {
                                view.showErrorMessage(e.getMessage());
                                view.hideProgress();
                                Log.d(TAG, "uploadImageUser: " + e.toString());
                            });
                        });
                    }).addOnFailureListener(e -> {
                view.showErrorMessage(e.getMessage());
                view.hideProgress();
                Log.d(TAG, "uploadImageUser: " + e.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage(e.getMessage());
            view.hideProgress();
            Log.d(TAG, "uploadImageUser: " + e.toString());
        }

    }
}
