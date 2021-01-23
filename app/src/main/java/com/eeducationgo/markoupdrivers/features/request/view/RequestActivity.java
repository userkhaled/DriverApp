package com.eeducationgo.markoupdrivers.features.request.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityRequestBinding;
import com.eeducationgo.markoupdrivers.features.request.model.PassangerUser;
import com.eeducationgo.markoupdrivers.features.request.presenter.RequestPresenter;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends BaseActivity implements RequestView {

    private ActivityRequestBinding binding;
    private RequestPresenter presenter;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(RequestActivity.this, R.layout.activity_request);

        init();

    }

    private void init() {
        presenter = new RequestPresenter(this, this);
        auth = FirebaseAuth.getInstance();
        databaseReferenceRoot = FirebaseDatabase.getInstance().getReference();
        databaseReferenceRoot.keepSynced(true);
        initListView();
    }

    private void initListView() {
        binding.rvRequest.setHasFixedSize(true);
        binding.rvRequest.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.rvRequest.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onStart() {
        super.onStart();
        String uid = auth.getCurrentUser().getUid();
        FirebaseRecyclerAdapter<PassangerUser, RequestAdapter> adapter =
                new FirebaseRecyclerAdapter<PassangerUser, RequestAdapter>(
                        PassangerUser.class,
                        R.layout.passanger_layout,
                        RequestAdapter.class,
                        databaseReferenceRoot.child(Constance.RootBooking).child(uid)
                ) {
                    @Override
                    protected void populateViewHolder(RequestAdapter holder, PassangerUser passangerUser, int i) {
                        String userKey = getRef(i).getKey();
                        DatabaseReference typeDb = getRef(i).child(Constance.ChildBookingRequestType).getRef();
                        try {
                            typeDb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        return;
                                    }
                                    String RequestType = (String) snapshot.getValue();
                                    if (RequestType.equalsIgnoreCase(Constance.ChildBookingRequestReceiver)) {
                                        databaseReferenceRoot.child(Constance.RootPassenger)
                                                .child(userKey).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                PassangerUser user = snapshot.getValue(PassangerUser.class);

                                                initDataUser(user, holder);
                                                onCancelledBtn(databaseReferenceRoot, holder, uid, userKey , user);
                                                acceptBtn(databaseReferenceRoot , holder , uid , userKey , user);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
        binding.rvRequest.setAdapter(adapter);
    }

    private void acceptBtn(DatabaseReference databaseReferenceRoot, RequestAdapter holder, String uid, String userKey, PassangerUser passangerUser) {
        holder.buttonAccept.setOnClickListener(v -> {
            presenter.accept(databaseReferenceRoot , uid , userKey , passangerUser);
        });
    }

    private void onCancelledBtn(DatabaseReference databaseReferenceRoot, RequestAdapter holder, String uid, String userKey, PassangerUser passangerUser) {
        holder.buttonDecline.setOnClickListener(v -> {
            presenter.cancel(databaseReferenceRoot , holder , uid , userKey , passangerUser);
        });
    }

    private void initDataUser(PassangerUser user, RequestAdapter holder) {
        holder.setTextViewName(user.getName());
        holder.setCircleImageView(user.getImage());
    }

    public static class RequestAdapter extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView textViewName;
        private Button buttonAccept, buttonDecline;

        public RequestAdapter(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.civ_user_image);
            textViewName = itemView.findViewById(R.id.tv_name);
            buttonAccept = itemView.findViewById(R.id.button_skip_email_regesteration);
            buttonDecline = itemView.findViewById(R.id.btn_decline);
        }

        public void setCircleImageView(String circleImageView) {
            Glide.with(this.circleImageView.getContext()).load(circleImageView)
                    .apply(new RequestOptions().placeholder(R.drawable.holder)).into(this.circleImageView);
        }

        public void setTextViewName(String textViewName) {
            this.textViewName.setText(textViewName);
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