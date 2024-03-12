package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

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

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        Glide.with(this).load(userModel.image).placeholder(
                new AvatarGenerator.AvatarBuilder(SettingActivity.this)
                        .setLabel(userModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(90)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(16)
                        .toCircle()
                        .build()
        ).into(binding.profile2);
        binding.name.setText(userModel.name);
        binding.number.setText(userModel.number);

        binding.editMode.setChecked(Stash.getBoolean(Constants.EDIT_MODE, false));

        binding.editMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Stash.put(Constants.EDIT_MODE, isChecked);
        });

        binding.profile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

    }
}