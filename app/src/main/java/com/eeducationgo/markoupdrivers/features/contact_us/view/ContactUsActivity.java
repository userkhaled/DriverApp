package com.eeducationgo.markoupdrivers.features.contact_us.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityContactUsBinding;

public class ContactUsActivity extends AppCompatActivity {

    private ActivityContactUsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ContactUsActivity.this , R.layout.activity_contact_us);

        listener();
    }

    private void listener() {
        email();
        phone();
    }

    private void phone() {
        binding.tvVerfiy.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+binding.tvVerfiy.getText()))));
    }

    private void email() {
        binding.tvVerfiyId.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",""+binding.tvVerfiyId.getText(), null));
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(Intent.createChooser(intent, "Choose an Email client :"));
        });
    }
}