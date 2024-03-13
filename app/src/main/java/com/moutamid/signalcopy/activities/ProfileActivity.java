package com.moutamid.signalcopy.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
    Uri image = Uri.EMPTY;
    CircleImageView profile2;
    private static final int PICK_IMAGE_REQUEST = 1001;
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
        MaterialCardView gallery = dialog.findViewById(R.id.gallery);
        MaterialButton save = dialog.findViewById(R.id.save);
        profile2 = dialog.findViewById(R.id.profile2);

        Glide.with(this).load(userModel.image).placeholder(
                new AvatarGenerator.AvatarBuilder(ProfileActivity.this)
                        .setLabel(userModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(90)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(16)
                        .toCircle()
                        .build()
        ).into(profile2);

        gallery.setOnClickListener(v -> {
            if (Constants.checkPermission(ProfileActivity.this)) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Pick Image"), PICK_IMAGE_REQUEST);
            } else {
                String[] permissions;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions = new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    };
                    shouldShowRequestPermissionRationale(permissions[0]);
                    shouldShowRequestPermissionRationale(permissions[1]);
                    shouldShowRequestPermissionRationale(permissions[2]);
                } else {
                    permissions = new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                    };
                    shouldShowRequestPermissionRationale(permissions[0]);
                }
                ActivityCompat.requestPermissions(ProfileActivity.this, permissions, 222);
            }
        });

        back.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {
            userModel.image = image.toString();
            Stash.put(Constants.STASH_USER, userModel);
            dialog.dismiss();
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
            String s = firstName.getEditText().getText().toString().trim() + " " + lastName.getEditText().getText().toString().trim();
            userModel.name = s.trim();
            Stash.put(Constants.STASH_USER, userModel);
            dialog.dismiss();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            image = data.getData();
            Glide.with(this).load(image).into(profile2);
        }
    }

}