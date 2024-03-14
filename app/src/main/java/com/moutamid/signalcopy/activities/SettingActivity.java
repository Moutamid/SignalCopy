package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.databinding.ActivitySettingBinding;
import com.moutamid.signalcopy.model.UserModel;

import java.util.Locale;
import java.util.Random;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.editMode.setChecked(Stash.getBoolean(Constants.EDIT_MODE, false));

        binding.editMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Stash.put(Constants.EDIT_MODE, isChecked);
        });

        binding.profile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        if (userModel.image.isEmpty()){
            binding.profile2.text.setTextSize(26);
            binding.profile2.cardViewRoot.setCardBackgroundColor(userModel.bgColor);
            binding.profile2.text.setTextColor(userModel.textColor);
            binding.profile2.text.setText(userModel.iconName);
            binding.profile2.text.setVisibility(View.VISIBLE);
            binding.profile2.profile.setVisibility(View.GONE);
        } else {
            binding.profile2.text.setVisibility(View.GONE);
            binding.profile2.profile.setVisibility(View.VISIBLE);
            Glide.with(this).load(userModel.image).into(binding.profile2.profile);
        }
        binding.name.setText(userModel.name);
        binding.number.setText(userModel.number);
    }
}