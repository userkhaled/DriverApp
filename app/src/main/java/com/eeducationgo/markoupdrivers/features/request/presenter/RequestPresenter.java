package com.eeducationgo.markoupdrivers.features.request.presenter;

import android.app.Activity;
import android.widget.Toast;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.fcm.json.SendNotificationThread;
import com.eeducationgo.markoupdrivers.features.request.model.PassangerUser;
import com.eeducationgo.markoupdrivers.features.request.view.RequestActivity;
import com.eeducationgo.markoupdrivers.features.request.view.RequestView;
import com.eeducationgo.markoupdrivers.util.AppShareData;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.firebase.database.DatabaseReference;

public class RequestPresenter {
    private Activity activity;
    private RequestView view;

    public RequestPresenter(Activity activity, RequestView view) {
        this.activity = activity;
        this.view = view;
    }

    public void cancel(DatabaseReference databaseReferenceRoot, RequestActivity.RequestAdapter holder, String uid, String userKey, PassangerUser passangerUser) {
        view.showProgress();
        try {
            databaseReferenceRoot.child(Constance.RootBooking).child(uid).child(userKey)
                    .removeValue().addOnSuccessListener(aVoid -> {
                databaseReferenceRoot.child(Constance.RootBooking).child(userKey).child(uid)
                        .removeValue().addOnSuccessListener(aVoid1 -> {

                    SendNotificationThread.getInstance().sendNotificationTo(AppShareData.getUserName(),
                            "Cancel your booking request",
                            passangerUser.getToken(),
                            "");
                    view.hideProgress();
                    view.showSuccessMessage(activity.getString(R.string.success_process));
                }).addOnFailureListener(e -> {
                    view.hideProgress();
                    view.showErrorMessage(e.toString());
                });
            }).addOnFailureListener(e -> {
                view.hideProgress();
                view.showErrorMessage(e.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
            view.hideProgress();
            view.showErrorMessage(e.toString());
        }
    }

    public void accept(DatabaseReference databaseReferenceRoot, String uid, String userKey, PassangerUser passangerUser) {
        view.showProgress();
        try {
            databaseReferenceRoot.child(Constance.RootBookingList).child(uid).child(userKey)
                    .child(Constance.ChildBookingListBook)
                    .setValue(Constance.ChildBookingListSaveValue)
                    .addOnSuccessListener(aVoid -> {
                        databaseReferenceRoot.child(Constance.RootBookingList).child(userKey).child(uid)
                                .child(Constance.ChildBookingListBook)
                                .setValue(Constance.ChildBookingListSaveValue)
                                .addOnSuccessListener(aVoid12 -> {
                                    //todo this for delete
                                    databaseReferenceRoot.child(Constance.RootBooking).child(uid).child(userKey)
                                            .removeValue().addOnSuccessListener(aVoids -> {
                                        databaseReferenceRoot.child(Constance.RootBooking).child(userKey).child(uid)
                                                .removeValue().addOnSuccessListener(aVoid1 -> {
                                            SendNotificationThread.getInstance().sendNotificationTo(AppShareData.getUserName(),
                                                    "Accept your booking request",
                                                    passangerUser.getToken(),
                                                    "");
                                            view.hideProgress();
                                            view.showSuccessMessage(activity.getString(R.string.success_process));
                                        }).addOnFailureListener(e -> {
                                            view.hideProgress();
                                            view.showErrorMessage(e.toString());
                                        });
                                    }).addOnFailureListener(e -> {
                                        view.hideProgress();
                                        view.showErrorMessage(e.toString());
                                    });
                                })
                                .addOnFailureListener(e -> {
                                    view.hideProgress();
                                    view.showErrorMessage(e.toString());
                                });
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
}
