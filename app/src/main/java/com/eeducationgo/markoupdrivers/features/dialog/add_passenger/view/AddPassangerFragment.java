package com.eeducationgo.markoupdrivers.features.dialog.add_passenger.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.FragmentAddPassangerBinding;
import com.eeducationgo.markoupdrivers.features.dialog.add_passenger.presenter.AddPassangerPresenter;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.eeducationgo.markoupdrivers.util.ProgressBarDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPassangerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPassangerFragment extends DialogFragment implements AddPassangerView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Snackbar snackbar;
    public ProgressBarDialog dialog;

    private FragmentAddPassangerBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceRoot;
    private AddPassangerPresenter presenter;

    public AddPassangerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPassangerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPassangerFragment newInstance(String param1, String param2) {
        AddPassangerFragment fragment = new AddPassangerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = new ProgressBarDialog(getContext());
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_passanger, container, false);
        init();
        initListener();
        return binding.getRoot();
    }

    private void initListener() {
        btnCancel();
        btnAdd();
    }

    private void btnAdd() {
        binding.btnNext.setOnClickListener(v -> {
         presenter.addPassanger(auth , databaseReferenceRoot , binding.inputUserName ,binding.inputPrice);
        });
    }

    private void btnCancel() {
        binding.buttonCancel.setOnClickListener(v -> dismiss());
    }

    private void init() {
        presenter = new AddPassangerPresenter(this, this);
        auth = FirebaseAuth.getInstance();
        databaseReferenceRoot = FirebaseDatabase.getInstance().getReference()
                .child(Constance.RootBookingList).child(auth.getCurrentUser().getUid());
        databaseReferenceRoot.keepSynced(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.horizontalMargin = 0;
        wlp.gravity = Gravity.CENTER_HORIZONTAL;
        window.setAttributes(wlp);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.45);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }

    @Override
    public void showErrorMessage(String message) {
        snackErrorShow(binding.getRoot(), message);
    }

    @Override
    public void showSuccessMessage(String message) {
        snackSuccessShow(binding.getRoot(), message);
    }

    public void snackErrorShow(View view, String text) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setDuration(4000);
        snackbar.setBackgroundTint(Color.parseColor("#ff0000"));
        snackbar.show();
    }

    public void snackSuccessShow(View view, String text) {
        snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        snackbar.setDuration(4000);
        snackbar.setBackgroundTint(Color.parseColor("#00ff00"));
        snackbar.show();
    }
}