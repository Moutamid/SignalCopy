package com.moutamid.signalcopy.fragments;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.moutamid.signalcopy.adapters.ContactsAdapter;
import com.moutamid.signalcopy.databinding.FragmentChatBinding;
import com.moutamid.signalcopy.listeners.ContactListener;
import com.moutamid.signalcopy.model.ContactsModel;
import com.moutamid.signalcopy.model.UserModel;

import java.util.ArrayList;
import java.util.UUID;

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

    private static final String TAG = "ChatFragment";
    private void getList() {
        ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);

        if (list.isEmpty()) {

            list.add(new ContactsModel(
                    "8b82249d-bfc1-47b7-9911-69f6e2a48a92",
                    "M",
                    "Moutamid",
                    "",
                    "I am fine how are you",
                    "+123545632365",
                    "Now",
                    -792321,
                    2131099675
            ));
            list.add(new ContactsModel(
                    "c6c1f467-9d80-4241-9f35-a3d4b9e14494",
                    "SM",
                    "Suleman",
                    "",
                    "Start chatting",
                    "+923256489124",
                    "Now",
                    -792321,
                    2131099675
            ));
            Stash.put(Constants.USERS, list);
        }
        ContactsAdapter adapter = new ContactsAdapter(requireContext(), list, contactListener);
        binding.chatRC.setAdapter(adapter);

        if (!list.isEmpty()) {
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
    ImageView profile;
    TextView textView;

    private void updateContact(ContactsModel model) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView title = dialog.findViewById(R.id.title);
        MaterialButton save = dialog.findViewById(R.id.save);
        MaterialButton add = dialog.findViewById(R.id.add);
        MaterialButton delete = dialog.findViewById(R.id.delete);
        MaterialButton back = dialog.findViewById(R.id.back);
        MaterialButton text = dialog.findViewById(R.id.textBtn);
        TextInputLayout name = dialog.findViewById(R.id.name);
        TextInputLayout iconName = dialog.findViewById(R.id.iconName);
        TextInputLayout number = dialog.findViewById(R.id.number);
        TextInputLayout lastMessage = dialog.findViewById(R.id.lastMessage);
        TextInputLayout time = dialog.findViewById(R.id.time);

        View profile2 = dialog.findViewById(R.id.profile2);
        profile = profile2.findViewById(R.id.profile);
        textView = profile2.findViewById(R.id.text);

        textView.setTextSize(20);

        int[] backColor = {model.bgColor};
        int[] textColor = {model.textColor};

        back.setEnabled(false);
        text.setEnabled(false);

        text.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(requireContext())
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

        back.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(requireContext())
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


        iconName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                back.setEnabled(!s.toString().isEmpty());
                text.setEnabled(!s.toString().isEmpty());

                if (s.toString().isEmpty()) {
                    textView.setVisibility(View.GONE);
                    profile.setVisibility(View.VISIBLE);
                } else {
                    if (image.toString().isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.GONE);
                        textView.setText(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        delete.setOnClickListener(v -> {
            image = Uri.EMPTY;
            profile.setImageResource(R.drawable.profile_icon);
            if (!iconName.getEditText().getText().toString().isEmpty()) {
                textView.setVisibility(View.VISIBLE);
                profile.setVisibility(View.GONE);
                textView.setText(iconName.getEditText().getText().toString());
            }
        });

        title.setText("Update Contact");

        profile = dialog.findViewById(R.id.profile2).findViewById(R.id.profile);
        textView = dialog.findViewById(R.id.profile2).findViewById(R.id.text);


        name.getEditText().setText(model.name);
        iconName.getEditText().setText(model.iconName);
        number.getEditText().setText(model.number);
        lastMessage.getEditText().setText(model.lastMessage);
        time.getEditText().setText(model.time);
        image = Uri.parse(model.image);

        if (model.image.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            profile.setVisibility(View.GONE);
            textView.setText(model.iconName);
            textView.setTextColor(model.textColor);
            ((MaterialCardView) profile2).setCardBackgroundColor(model.bgColor);
        }

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
                ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
                int index = retrieveIndex(model, list);
                if (index != -1) {
                    list.get(index).image = image.toString();
                    list.get(index).name = name.getEditText().getText().toString();
                    list.get(index).iconName = iconName.getEditText().getText().toString();
                    list.get(index).lastMessage = lastMessage.getEditText().getText().toString();
                    list.get(index).number = number.getEditText().getText().toString();
                    list.get(index).time = time.getEditText().getText().toString();
                    list.get(index).textColor = textColor[0];
                    list.get(index).bgColor = backColor[0];
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
            if (contactsModel.id.equals(model.id)) {
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
        MaterialButton back = dialog.findViewById(R.id.back);
        MaterialButton text = dialog.findViewById(R.id.textBtn);
        TextInputLayout name = dialog.findViewById(R.id.name);
        TextInputLayout iconName = dialog.findViewById(R.id.iconName);
        TextInputLayout number = dialog.findViewById(R.id.number);
        TextInputLayout lastMessage = dialog.findViewById(R.id.lastMessage);
        TextInputLayout time = dialog.findViewById(R.id.time);

        View profile2 = dialog.findViewById(R.id.profile2);
        profile = profile2.findViewById(R.id.profile);
        textView = profile2.findViewById(R.id.text);

        textView.setTextSize(20);

        int[] backColor = {R.color.active};
        int[] textColor = {R.color.black};

        back.setEnabled(false);
        text.setEnabled(false);

        text.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(requireContext())
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

        back.setOnClickListener(v -> {
            ColorPickerDialogBuilder
                    .with(requireContext())
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
                            Toast.makeText(requireContext(), "" + selectedColor, Toast.LENGTH_SHORT).show();
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
        iconName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                back.setEnabled(!s.toString().isEmpty());
                text.setEnabled(!s.toString().isEmpty());

                if (s.toString().isEmpty()) {
                    textView.setVisibility(View.GONE);
                    profile.setVisibility(View.VISIBLE);
                } else {
                    if (image.toString().isEmpty()) {
                        textView.setVisibility(View.VISIBLE);
                        profile.setVisibility(View.GONE);
                        textView.setText(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        delete.setOnClickListener(v -> {
            image = Uri.EMPTY;
            profile.setImageResource(R.drawable.profile_icon);
            if (!iconName.getEditText().getText().toString().isEmpty()) {
                textView.setVisibility(View.VISIBLE);
                profile.setVisibility(View.GONE);
                textView.setText(iconName.getEditText().getText().toString());
            }
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
                ContactsModel model = new ContactsModel(UUID.randomUUID().toString(),
                        iconName.getEditText().getText().toString(),
                        name.getEditText().getText().toString(),
                        image.toString(),
                        lastMessage.getEditText().getText().toString(),
                        number.getEditText().getText().toString(),
                        time.getEditText().getText().toString(), textColor[0], backColor[0]);

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
            profile.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            Glide.with(this).load(image).into(profile);
        }
    }
}