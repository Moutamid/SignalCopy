package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.databinding.ActivityProfileBinding;
import com.moutamid.signalcopy.model.UserModel;

import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(v -> onBackPressed());

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        Glide.with(this).load(userModel.image).placeholder(
                new AvatarGenerator.AvatarBuilder(ProfileActivity.this)
                        .setLabel(userModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(90)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(16)
                        .toCircle()
                        .build()
        ).into(binding.profile2);
        binding.name.setText(userModel.name);

        binding.name.setOnClickListener(v -> showNameEdit());
        binding.edit.setOnClickListener(v -> showPictureEdit());

    }

    private void showPictureEdit() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picture_edit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        MaterialCardView back = dialog.findViewById(R.id.back);
        MaterialButton save = dialog.findViewById(R.id.save);
        CircleImageView profile2 = dialog.findViewById(R.id.profile2);

        Glide.with(this).load(userModel.image).placeholder(
                new AvatarGenerator.AvatarBuilder(ProfileActivity.this)
                        .setLabel(userModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(90)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(16)
                        .toCircle()
                        .build()
        ).into(profile2);


        back.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {

        });
    }

    private void showNameEdit() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.name_edit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        TextInputLayout firstName = dialog.findViewById(R.id.firstName);
        TextInputLayout lastName = dialog.findViewById(R.id.lastName);
        MaterialCardView back = dialog.findViewById(R.id.back);
        MaterialButton save = dialog.findViewById(R.id.save);
        String[] name = userModel.name.split(" ");
        firstName.getEditText().setText(name[0]);
        if (name.length>1){
            lastName.getEditText().setText(name[1]);
        }
        back.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {

        });

    }
}