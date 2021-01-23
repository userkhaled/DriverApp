package com.eeducationgo.markoupdrivers.features.controll.presenter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.features.controll.view.ControleActivity;
import com.eeducationgo.markoupdrivers.features.controll.view.ControleView;
import com.eeducationgo.markoupdrivers.features.request.model.PassangerUser;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ControlePresenter {
    private Activity activity;
    private ControleView view;

    public ControlePresenter(Activity activity, ControleView view) {
        this.activity = activity;
        this.view = view;
    }

    public void setData(ControleActivity.BookingListHolder bookingListHolder,
                        DatabaseReference databaseReferenceRoot, String userKey, String uid) {
        view.showProgress();
        try {
            databaseReferenceRoot.child(Constance.RootPassenger)
                    .child(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        view.hideProgress();
                        view.showErrorMessage(snapshot.toString());
                        return;
                    }
                    PassangerUser user = snapshot.getValue(PassangerUser.class);
                    String phone = TextUtils.isEmpty(user.getPhone()) ? "+xxxxxxxxxxx" : user.getPhone();
                    if (TextUtils.isEmpty(user.getImage())) {
                        bookingListHolder.imageViewVrefiy.setVisibility(View.GONE);
                    }
                    bookingListHolder.setCircleImageView(user.getImage());
                    bookingListHolder.setUserName(user.getName());
                    bookingListHolder.setUserPhone(phone);
                    view.hideProgress();
                    initListener(bookingListHolder, userKey, uid, databaseReferenceRoot);
                    initCallListener(bookingListHolder, user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    view.showErrorMessage(error.getMessage());
                    view.hideProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage(e.getMessage());
            view.hideProgress();
        }

    }

    public void initCallListener(ControleActivity.BookingListHolder bookingListHolder, PassangerUser user) {

        if (user == null) {
            user = new PassangerUser();
            user.setPhone("+000000000000");
        }
        String phone = TextUtils.isEmpty(user.getPhone()) ? "+000000000000" : user.getPhone();
        bookingListHolder.imageButtonCall.setOnClickListener(v -> {
            activity.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phone)));
        });
    }

    public void initListener(ControleActivity.BookingListHolder bookingListHolder, String userKey,
                             String uid, DatabaseReference databaseReferenceRoot) {
        bookingListHolder.imageButtonClose.setOnClickListener(v -> {
            view.showProgress();
            try {
                databaseReferenceRoot.child(Constance.RootBookingList).child(uid).child(userKey)
                        .removeValue()
                        .addOnSuccessListener(aVoid -> {databaseReferenceRoot.child(Constance.RootBookingList)
                                .child(userKey)
                                .child(uid)
                                .removeValue()
                                .addOnSuccessListener(aVoid1 -> {
                                    view.hideProgress();
                                    view.showSuccessMessage(activity.getString(R.string.success_process));
                                })
                                .addOnFailureListener(e -> {
                                    view.showErrorMessage(e.getMessage());
                                    view.hideProgress();
                                });
                        })
                        .addOnFailureListener(e -> {
                            view.showErrorMessage(e.getMessage());
                            view.hideProgress();
                        });
            } catch (Exception e) {
                e.printStackTrace();
                view.showErrorMessage(e.getMessage());
                view.hideProgress();
            }

        });
    }

    public void initListenerDelete(ControleActivity.BookingListHolder bookingListHolder,
                                   String uid, String userKey, DatabaseReference databaseReferenceRoot) {
        bookingListHolder.imageButtonClose.setOnClickListener(v -> {
            view.showProgress();
            try {
                databaseReferenceRoot.child(Constance.RootBookingList).child(uid).child(userKey)
                        .removeValue()
                        .addOnSuccessListener(aVoid -> databaseReferenceRoot.child(Constance.RootBookingList)
                        .child(userKey)
                        .child(uid)
                        .removeValue()
                        .addOnSuccessListener(aVoid1 -> {
                            view.hideProgress();
                            view.showSuccessMessage(activity.getString(R.string.success_process));
                        })
                        .addOnFailureListener(e -> {
                            view.showErrorMessage(e.getMessage());
                            view.hideProgress();
                        }))
                        .addOnFailureListener(e -> {
                            view.showErrorMessage(e.getMessage());
                            view.hideProgress();
                        });


            } catch (Exception e) {
                e.printStackTrace();
                view.showErrorMessage(e.getMessage());
                view.hideProgress();
            }

        });
    }
}
