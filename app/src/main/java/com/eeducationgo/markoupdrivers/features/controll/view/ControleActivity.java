package com.eeducationgo.markoupdrivers.features.controll.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityControleBinding;
import com.eeducationgo.markoupdrivers.features.controll.presenter.ControlePresenter;
import com.eeducationgo.markoupdrivers.features.dialog.add_passenger.view.AddPassangerFragment;
import com.eeducationgo.markoupdrivers.features.request.model.PassangerUser;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ControleActivity extends BaseActivity implements ControleView {

    private ActivityControleBinding binding;
    private DatabaseReference databaseReferenceRoot;
    private FirebaseAuth auth;
    private ControlePresenter presenter;
    private String totalAmount = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ControleActivity.this, R.layout.activity_controle);
        init();
        initListener();
    }

    private void initListener() {
        btnAddPassanger();
    }

    private void btnAddPassanger() {
        binding.fabAddPassenger.setOnClickListener(v -> {
            AddPassangerFragment fragment = new AddPassangerFragment();
            fragment.show(getSupportFragmentManager(), fragment.getTag());
        });
    }

    private void init() {
        presenter = new ControlePresenter(this, this);
        auth = FirebaseAuth.getInstance();
        databaseReferenceRoot = FirebaseDatabase.getInstance().getReference();
        databaseReferenceRoot.keepSynced(true);
        initListView();
        binding.tvAmount.setText("Total amount : " + totalAmount);
    }

    private void initListView() {
        binding.rvControl.setHasFixedSize(true);
        binding.rvControl.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        binding.rvControl.addItemDecoration(new DividerItemDecoration(getBaseContext() , DividerItemDecoration.VERTICAL));
    }

    private static final String TAG = "ControleActivity";
    public static  int size = 0;
    @Override
    protected void onStart() {
        super.onStart();
        String uid = auth.getCurrentUser().getUid();
        FirebaseRecyclerAdapter<PassangerUser, BookingListHolder> adapter = new FirebaseRecyclerAdapter<PassangerUser, BookingListHolder>(
                PassangerUser.class,
                R.layout.booking_list_layout,
                BookingListHolder.class,
                databaseReferenceRoot.child(Constance.RootBookingList).child(uid)
        ) {
            @Override
            protected void populateViewHolder(BookingListHolder bookingListHolder, PassangerUser passangerUser, int i) {
                size +=i;
                String userKey = getRef(i).getKey();
                ArrayList<String> strings = new ArrayList<>();
                strings.add(String.valueOf(i));
                long total = size * 14;
                totalAmount = "" + total;
                binding.tvAmount.setText("Total amount : " + totalAmount);
                Log.d(TAG, "populateViewHolder: "+size);

                databaseReferenceRoot.child(Constance.RootPassenger).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.hasChild(userKey)) {
                            presenter.setData(bookingListHolder, databaseReferenceRoot, userKey, uid);
                        } else {
                            databaseReferenceRoot.child(Constance.RootBookingList).child(uid).child(userKey).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists() ){
                                        String name = (String) snapshot.child("UserName").getValue();
                                        String price = (String) snapshot.child("Price").getValue();
                                        Log.d(TAG, "onDataChange: "+name);
                                        bookingListHolder.setCircleImageView("");
                                        bookingListHolder.imageViewVrefiy.setVisibility(View.GONE);
                                        bookingListHolder.setUserName(name);
                                        bookingListHolder.setUserAmount("amount : " + price);
                                        bookingListHolder.setUserPhone("+xxxxxxxxxxxxx");

                                        presenter.initCallListener(bookingListHolder ,null);
                                        presenter.initListenerDelete(bookingListHolder ,uid , userKey , databaseReferenceRoot);
                                    }
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


            }
        };
        binding.rvControl.setAdapter(adapter);
    }


    public static class BookingListHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageViewUser;
        private TextView textViewName, textViewPhone, textViewAmount;
        public ImageButton imageButtonClose, imageButtonCall;
        public ImageView imageViewVrefiy;

        public BookingListHolder(@NonNull View itemView) {
            super(itemView);
            circleImageViewUser = itemView.findViewById(R.id.civ_user_image);
            textViewName = itemView.findViewById(R.id.tv_name_user);
            textViewPhone = itemView.findViewById(R.id.tv_phone_user);
            textViewAmount = itemView.findViewById(R.id.tv_amount_user);
            imageButtonClose = itemView.findViewById(R.id.ib_close);
            imageButtonCall = itemView.findViewById(R.id.ib_call);
            imageViewVrefiy = itemView.findViewById(R.id.img_verfiy);

        }

        public void setCircleImageView(String circleImageView) {
            Glide.with(this.circleImageViewUser.getContext()).load(circleImageView)
                    .apply(new RequestOptions().placeholder(R.drawable.holder)).into(this.circleImageViewUser);
        }


        public void setUserName(String userName) {
            this.textViewName.setText(userName);
        }

        public void setUserPhone(String userStatus) {
            this.textViewPhone.setText(userStatus);
        }

        public void setUserAmount(String userStatus) {
            this.textViewAmount.setText(userStatus);
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