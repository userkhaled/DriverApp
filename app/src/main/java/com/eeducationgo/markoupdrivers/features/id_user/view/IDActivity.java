package com.eeducationgo.markoupdrivers.features.id_user.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;


import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityIDBinding;
import com.eeducationgo.markoupdrivers.features.id_user.presenter.IDPresenter;
import com.eeducationgo.markoupdrivers.util.BaseActivity;
import com.eeducationgo.markoupdrivers.util.Constance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import static com.eeducationgo.markoupdrivers.util.Constance.ChildDriverStorageUser;


public class IDActivity extends BaseActivity implements IDView {

    private ActivityIDBinding binding;
    private FirebaseAuth auth;
    private StorageReference storageReferenceUserImage;
    private DatabaseReference databaseReferenceUserImage;
    private IDPresenter presenter;
    private String uid;
    private Uri resultUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(IDActivity.this, R.layout.activity_i_d);

        init();
        initListener();
    }

    private void initListener() {
        selectImage();
        uploadBtn();
    }

    private void selectImage() {
        binding.ivSelectImage.setOnClickListener(v -> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        });
    }

    private void uploadBtn() {
        binding.buttonNextEmailRegisteration.setOnClickListener(v -> {
            presenter.uploadImageUser(resultUri , auth , databaseReferenceUserImage , storageReferenceUserImage);
        });

    }

    private void init() {
        presenter = new IDPresenter(this, this);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        databaseReferenceUserImage = FirebaseDatabase.getInstance().getReference().child(Constance.RootDriver).child(uid);
        databaseReferenceUserImage.keepSynced(true);
        storageReferenceUserImage = FirebaseStorage.getInstance().getReference().child(Constance.RootDriver)
                .child(ChildDriverStorageUser);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                binding.ivSelectImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                snackErrorShow(binding.getRoot(), error.getMessage().toString());
            }
        }
    }
}