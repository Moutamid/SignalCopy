package com.moutamid.signalcopy;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.signalcopy.adapters.GalleryAdapter;
import com.moutamid.signalcopy.databinding.ActivityChatBinding;
import com.moutamid.signalcopy.model.ContactsModel;

import java.util.ArrayList;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    ContactsModel contactsModel;
    boolean isSend = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactsModel = (ContactsModel) Stash.getObject("PASS", ContactsModel.class);

        binding.name.setText(contactsModel.name);
        binding.name2.setText(contactsModel.name);
        binding.number.setText(contactsModel.number);

        Glide.with(this)
                .load(new AvatarGenerator.AvatarBuilder(this)
                        .setLabel(contactsModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(R.color.pink)
                        .setTextSize(13)
                        .toCircle()
                        .build()
                ).into(binding.profile);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.add.setOnClickListener(v -> {
            if (Constants.checkPermission(this)) {
                getList();
                binding.galleryLayout.setVisibility(View.VISIBLE);
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
                ActivityCompat.requestPermissions(this, permissions, 222);
            }
        });

        binding.gallery.setOnClickListener(v -> {
            if (Constants.checkPermission(this)) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Pick Image"), 1001);
            } else {
                String[] permissions;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissions = new String[]{
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
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
                ActivityCompat.requestPermissions(this, permissions, 222);
            }
        });

        binding.message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.galleryLayout.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    isSend = false;
                    binding.iconSend.setImageResource(R.drawable.plus);
                    binding.camera.setVisibility(View.VISIBLE);
                    binding.mic.setVisibility(View.VISIBLE);
                    binding.add.setVisibility(View.GONE);
                } else {
                    isSend = true;
                    binding.iconSend.setImageResource(R.drawable.send_message);
                    binding.camera.setVisibility(View.GONE);
                    binding.mic.setVisibility(View.GONE);
                    binding.add.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.send.setOnClickListener(v -> {
            if (!isSend) {
                if (Constants.checkPermission(this)) {
                    getList();
                    binding.galleryLayout.setVisibility(View.VISIBLE);
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
                    ActivityCompat.requestPermissions(this, permissions, 222);
                }
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setMessage("Send message to whom?")
                        .setPositiveButton("Myself", (dialog, which) -> {
                            dialog.dismiss();
                            sendMessage(true);
                        }).setNegativeButton(contactsModel.name, (dialog, which) -> {
                            dialog.dismiss();
                            sendMessage(false);
                        })
                        .show();
            }
        });
    }

    private void sendMessage(boolean b) {

    }

    public void getList() {
        ArrayList<String> list = new ArrayList<>();
        String[] projection = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder)) {

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    list.add(imagePath);
                }
                Log.d(TAG, "getList: " + list.size());
                GalleryAdapter adapter = new GalleryAdapter(ChatActivity.this, list, imagePick);
                binding.galleryRC.setAdapter(adapter);
            }
        }


//        Uri uri;
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//        ArrayList<String> listOfAllImages = new ArrayList<String>();
//        String absolutePathOfImage = null;
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
////        String[] projection = { MediaStore.MediaColumns.DATA,
////                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
//
//        cursor = getContentResolver().query(uri, projection, null,
//                null, null);
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//        while (cursor.moveToNext()) {
//            absolutePathOfImage = cursor.getString(column_index_data);
//
//            listOfAllImages.add(absolutePathOfImage);
//        }
//        Log.d(TAG, "getList: " + listOfAllImages.size());
//        GalleryAdapter adapter = new GalleryAdapter(ChatActivity.this, listOfAllImages, imagePick);
//        binding.galleryRC.setAdapter(adapter);

    }

    private static final String TAG = "ChatActivity";
    ImagePick imagePick = new ImagePick() {
        @Override
        public void imagePick(String image) {

        }
    };

}