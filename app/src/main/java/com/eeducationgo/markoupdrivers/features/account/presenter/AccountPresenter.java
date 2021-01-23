package com.eeducationgo.markoupdrivers.features.account.presenter;

import android.app.Activity;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.account.view.AccountView;
import com.eeducationgo.markoupdrivers.features.registration.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class AccountPresenter {
    private Activity activity;
    private AccountView view;

    public AccountPresenter(Activity activity, AccountView view) {
        this.activity = activity;
        this.view = view;
    }

    public void setViewData(DatabaseReference databaseReferenceDriver, TextView tvEmailUser, TextView tvNameUser, TextView tvPhoneUser) {

        try {
            databaseReferenceDriver.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        view.showErrorMessage(activity.getString(R.string.error));
                        return;
                    }
                    User user = snapshot.getValue(User.class);
                    tvEmailUser.setText(user.getEmail());
                    tvPhoneUser.setText(user.getPhone().isEmpty() ?"+972xxxxxxxxx":user.getPhone());
                    tvNameUser.setText(user.getName());
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
