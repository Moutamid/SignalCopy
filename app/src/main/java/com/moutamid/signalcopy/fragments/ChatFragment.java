package com.moutamid.signalcopy.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.adapters.ContactsAdapter;
import com.moutamid.signalcopy.databinding.FragmentChatBinding;
import com.moutamid.signalcopy.listeners.ContactListener;
import com.moutamid.signalcopy.model.ContactsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(getLayoutInflater(), container, false);

        binding.pen.setOnClickListener(v -> {
            showAddContact();
        });

        binding.chatRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatRC.setHasFixedSize(false);

        getList();

        return binding.getRoot();
    }

    private void getList() {
        ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
        ContactsAdapter adapter = new ContactsAdapter(requireContext(), list, contactListener);
        binding.chatRC.setAdapter(adapter);

        if (list.size() > 0) {
            binding.noLayout.setVisibility(View.GONE);
        } else {
            binding.noLayout.setVisibility(View.VISIBLE);
        }
    }

    ContactListener contactListener = new ContactListener() {
        @Override
        public void onCLick(ContactsModel model) {
            updateContact(model);
        }

        @Override
        public void onDelete(ContactsModel model, int pos) {
            ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
            list.remove(pos);
            Stash.put(Constants.USERS, list);
            getList();
        }
    };

    Uri image = Uri.EMPTY;
    CircleImageView profile2;

    private void updateContact(ContactsModel model) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        MaterialButton save = dialog.findViewById(R.id.save);
        MaterialButton add = dialog.findViewById(R.id.add);
        MaterialButton delete = dialog.findViewById(R.id.delete);
        TextInputLayout name = dialog.findViewById(R.id.name);
        TextInputLayout time = dialog.findViewById(R.id.time);
        TextInputLayout number = dialog.findViewById(R.id.number);
        TextInputLayout lastMessage = dialog.findViewById(R.id.lastMessage);

        profile2 = dialog.findViewById(R.id.profile2);

        name.getEditText().setText(model.name);
        number.getEditText().setText(model.number);

        Glide.with(requireContext()).load(model.image).placeholder(
                new AvatarGenerator.AvatarBuilder(requireContext())
                        .setLabel(model.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(13)
                        .toCircle()
                        .build()
        ).into(profile2);

        image = Uri.parse(model.image);

        add.setOnClickListener(v -> {
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

        delete.setOnClickListener(v -> {
            image = Uri.EMPTY;
            Glide.with(requireContext()).load(image).placeholder(
                    new AvatarGenerator.AvatarBuilder(requireContext())
                            .setLabel(name.getEditText().getText().toString().trim().toUpperCase(Locale.ROOT))
                            .setAvatarSize(70)
                            .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                            .setTextSize(13)
                            .toCircle()
                            .build()
            ).into(profile2);
        });

        save.setOnClickListener(v -> {
            if (name.getEditText().getText().toString().isEmpty() || time.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
                int index = retrieveIndex(model, list);
                if (index != -1){
                    list.get(index).image = image.toString();
                    list.get(index).name = name.getEditText().getText().toString();
                    list.get(index).lastMessage = lastMessage.getEditText().getText().toString();
                    list.get(index).number = number.getEditText().getText().toString();
                    list.get(index).time = time.getEditText().getText().toString();
                }
                Stash.put(Constants.USERS, list);
                getList();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private int retrieveIndex(ContactsModel model, ArrayList<ContactsModel> list) {
        for (int i = 0; i < list.size(); i++) {
            ContactsModel contactsModel = list.get(i);
            if (contactsModel.id.equals(model.id)){
                return i;
            }
        }
        return -1;
    }

    private static final int PICK_IMAGE_REQUEST = 1001;

    private void showAddContact() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        MaterialButton save = dialog.findViewById(R.id.save);
        MaterialButton add = dialog.findViewById(R.id.add);
        MaterialButton delete = dialog.findViewById(R.id.delete);
        TextInputLayout name = dialog.findViewById(R.id.name);
        TextInputLayout number = dialog.findViewById(R.id.number);
        TextInputLayout lastMessage = dialog.findViewById(R.id.lastMessage);
        TextInputLayout time = dialog.findViewById(R.id.time);

        profile2 = dialog.findViewById(R.id.profile2);

        delete.setOnClickListener(v -> {
            image = Uri.EMPTY;
            Glide.with(requireContext()).load(image).placeholder(
                    new AvatarGenerator.AvatarBuilder(requireContext())
                            .setLabel(name.getEditText().getText().toString().trim().toUpperCase(Locale.ROOT))
                            .setAvatarSize(70)
                            .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                            .setTextSize(13)
                            .toCircle()
                            .build()
            ).into(profile2);
        });

        add.setOnClickListener(v -> {
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

        save.setOnClickListener(v -> {
            if (name.getEditText().getText().toString().isEmpty() || time.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                ContactsModel model = new ContactsModel(UUID.randomUUID().toString(), name.getEditText().getText().toString(), image.toString(), lastMessage.getEditText().getText().toString(), number.getEditText().getText().toString(), time.getEditText().getText().toString());
                ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
                list.add(model);
                Stash.put(Constants.USERS, list);
                getList();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private long convertToMilliseconds(int hour, int minute) {
        return (hour * 60L + minute) * 60L * 1000L;
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