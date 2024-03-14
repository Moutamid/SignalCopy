package com.moutamid.signalcopy.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.activities.MainActivity;
import com.moutamid.signalcopy.databinding.FragmentNameBinding;
import com.moutamid.signalcopy.model.UserModel;

public class NameFragment extends Fragment {
    FragmentNameBinding binding;
    Uri image = Uri.EMPTY;
    private static final String TAG = "NameFragment";
    private static final int PICK_IMAGE_REQUEST = 1001;
    public NameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNameBinding.inflate(getLayoutInflater(), container, false);
        int[] backColor = {R.color.active};
        int[] textColor = {R.color.black};

        binding.iconName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                back.setEnabled(!s.toString().isEmpty());
//                text.setEnabled(!s.toString().isEmpty());

                if (s.toString().isEmpty()) {
                    binding.profile.profile.setVisibility(View.VISIBLE);
                    binding.profile.text.setVisibility(View.GONE);
                } else {
                    if (image.toString().isEmpty()) {
                        binding.profile.text.setVisibility(View.VISIBLE);
                        binding.profile.profile.setVisibility(View.GONE);
                        binding.profile.text.setText(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.continueBtn.setOnClickListener(v -> {
            String s = Stash.getString("numb");
            Log.d(TAG, "onCreateView: " + s);
            if (binding.iconName.getEditText().getText().toString().isEmpty() || binding.firstName.getEditText().getText().toString().isEmpty()){
                Toast.makeText(requireContext(), "fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                UserModel userModel = new UserModel(binding.iconName.getEditText().getText().toString(),binding.name.getText().toString(), s, image.toString(), textColor[0], backColor[0]);
                Stash.put(Constants.STASH_USER, userModel);

                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
            }
        });

        binding.camera.setOnClickListener(v -> {
            if (Constants.checkPermission(requireContext())) {
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
                ActivityCompat.requestPermissions(requireActivity(), permissions, 222);
            }
        });

        binding.profile.getRoot().setOnClickListener(v -> {
            if (Constants.checkPermission(requireContext())) {
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
                ActivityCompat.requestPermissions(requireActivity(), permissions, 222);
            }
        });

        binding.firstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.name.setText(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.lastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = binding.firstName.getEditText().getText().toString() + " ";
                binding.name.setText(ss + s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            image = data.getData();
            binding.profile.profile.setVisibility(View.VISIBLE);
            binding.profile.text.setVisibility(View.GONE);
            Glide.with(this).load(image).into(binding.profile.profile);
        }
    }
}