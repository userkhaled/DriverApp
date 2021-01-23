package com.eeducationgo.markoupdrivers.features.dialog.edit.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.eeducationgo.markoupdrivers.databinding.FragmentEditDilaogBinding;
import com.eeducationgo.markoupdrivers.features.dialog.edit.presenter.EditDialogPresenter;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.eeducationgo.markoupdrivers.util.ProgressBarDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDialogFragment extends DialogFragment implements EditDialogView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Snackbar snackbar;
    public ProgressBarDialog dialog;
    private FragmentEditDilaogBinding binding;
    private EditDialogPresenter presenter;
    private Uri resultUri;
    private FirebaseAuth auth;
    private DatabaseReference databaseReferenceDriver;
    private StorageReference storageReferenceDriver;

    public EditDialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditDilaogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDialogFragment newInstance(String param1, String param2) {
        EditDialogFragment fragment = new EditDialogFragment();
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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_dilaog, container, false);

        init();
        initData();
        initListener();
        return binding.getRoot();
    }

    private void initData() {
        presenter.setViewData(databaseReferenceDriver , binding.inputUserEmail , binding.inputUserName ,binding.inputUserPhone , binding.civUserImage , binding.tvTitle);
    }

    private void initListener() {
        imageEdit();
        btnClicked();
    }

    private void btnClicked() {
        binding.buttonSkipEmailRegesteration.setOnClickListener(v -> presenter.updateData(binding.inputUserEmail , binding.inputUserName , binding.inputUserPhone , storageReferenceDriver ,auth ,databaseReferenceDriver , resultUri));
    }

    private void imageEdit() {
        binding.ibEdit.setOnClickListener(v ->
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this));
    }

    private void init() {
        dialog = new ProgressBarDialog(getContext());
        presenter = new EditDialogPresenter(this, this);
        auth = FirebaseAuth.getInstance();
        databaseReferenceDriver = FirebaseDatabase.getInstance().getReference()
                .child(Constance.RootDriver).child(auth.getCurrentUser().getUid());
        databaseReferenceDriver.keepSynced(true);
        storageReferenceDriver = FirebaseStorage.getInstance().getReference()
                .child(Constance.RootDriver).child(Constance.ChildDriverStorageUser);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.civUserImage.setImageURI(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                snackErrorShow(binding.getRoot(), error.getMessage().toString());
            }
        }
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
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.9);
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