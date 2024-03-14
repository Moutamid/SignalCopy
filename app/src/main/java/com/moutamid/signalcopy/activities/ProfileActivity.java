package com.moutamid.signalcopy.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
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

        setUI();

        binding.name.setOnClickListener(v -> showNameEdit());
        binding.edit.setOnClickListener(v -> showPictureEdit());

    }

    private void setUI() {
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
    }

    Uri image = Uri.EMPTY;
    ImageView profile;
    TextView textView;
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
        MaterialCardView background = dialog.findViewById(R.id.background);
        MaterialCardView text = dialog.findViewById(R.id.textBtn);
        MaterialCardView iconName = dialog.findViewById(R.id.iconName);
        View profile2 = dialog.findViewById(R.id.profile2);
        profile = profile2.findViewById(R.id.profile);
        textView = profile2.findViewById(R.id.text);

        int[] backColor = {userModel.bgColor};
        int[] textColor = {userModel.textColor};

        name = userModel.iconName;

        iconName.setOnClickListener(v -> {
            Dialog icon = new Dialog(ProfileActivity.this);
            icon.requestWindowFeature(Window.FEATURE_NO_TITLE);
            icon.setContentView(R.layout.get_icon);
            icon.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            icon.setCancelable(true);
            icon.getWindow().setGravity(Gravity.CENTER);
            icon.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            icon.show();

            TextInputLayout icon_Name = icon.findViewById(R.id.iconName);
            MaterialButton continueBtn = icon.findViewById(R.id.continueBtn);

            continueBtn.setOnClickListener( v1 -> {
                if (icon_Name.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(this, "Enter valid name", Toast.LENGTH_SHORT).show();
                } else {
                    name = icon_Name.getEditText().getText().toString();
                    textView.setText(name);
                    icon.dismiss();
                }
            });
        });

        text.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {

                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            textColor[0] = selectedColor;
                            textView.setTextColor(selectedColor);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();
        });

        background.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {

                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            backColor[0] = selectedColor;
                            ((MaterialCardView) profile2).setCardBackgroundColor(selectedColor);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show();
        });

        if (userModel.image.isEmpty()){
            textView.setTextSize(26);
            ((MaterialCardView) profile2).setCardBackgroundColor(userModel.bgColor);
            textView.setTextColor(userModel.textColor);
            textView.setText(userModel.iconName);
            textView.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.GONE);
            profile.setVisibility(View.VISIBLE);
            Glide.with(this).load(userModel.image).into(profile);
        }

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
            userModel.iconName = name;
            userModel.bgColor = backColor[0];
            userModel.textColor = textColor[0];
            Stash.put(Constants.STASH_USER, userModel);
            dialog.dismiss();
            setUI();
        });
    }

    String name;

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
            setUI();
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            image = data.getData();
            profile.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            Glide.with(this).load(image).into(profile);
        }
    }

}