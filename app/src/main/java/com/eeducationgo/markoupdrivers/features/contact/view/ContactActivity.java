package com.eeducationgo.markoupdrivers.features.contact.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.eeducationgo.markoupdrivers.R;
import com.eeducationgo.markoupdrivers.databinding.ActivityContactBinding;
import com.eeducationgo.markoupdrivers.features.contact_us.view.ContactUsActivity;


public class ContactActivity extends AppCompatActivity {
    private ActivityContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ContactActivity.this ,R.layout.activity_contact);
        binding.tvContact.setOnClickListener(v -> {
            startActivity(new Intent(getBaseContext() , ContactUsActivity.class));
        });
    }
}