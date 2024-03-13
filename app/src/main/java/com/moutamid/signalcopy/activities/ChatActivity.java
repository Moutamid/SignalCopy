package com.moutamid.signalcopy.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.adapters.GalleryAdapter;
import com.moutamid.signalcopy.adapters.MessageAdapter;
import com.moutamid.signalcopy.databinding.ActivityChatBinding;
import com.moutamid.signalcopy.listeners.DeleteListener;
import com.moutamid.signalcopy.listeners.ImagePick;
import com.moutamid.signalcopy.model.ContactsModel;
import com.moutamid.signalcopy.model.MessageModel;
import com.moutamid.signalcopy.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1001;
    ActivityChatBinding binding;
    ContactsModel contactsModel;
    boolean isSend = false;

    private String getShortenedText(String text, int limit) {
        if (text.length() <= limit) {
            return text;
        } else {
            return text.substring(0, limit) + "...";
        }
    }
    private void hideKeyboard() {
        binding.message.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.message.getWindowToken(), 0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        contactsModel = (ContactsModel) Stash.getObject("PASS", ContactsModel.class);

        binding.name.setText(getShortenedText(contactsModel.name, 10));
        binding.name2.setText(contactsModel.name);
        binding.number.setText(contactsModel.number);

        binding.chatRC.setLayoutManager(new LinearLayoutManager(this));
        binding.chatRC.setHasFixedSize(false);

        binding.message.setFocusableInTouchMode(true);
        binding.message.requestFocus();
        hideKeyboard();


        binding.message.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) binding.galleryLayout.setVisibility(View.GONE);
            }
        });

        Glide.with(this)
                .load(contactsModel.image).placeholder(new AvatarGenerator.AvatarBuilder(this)
                        .setLabel(contactsModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(13)
                        .toCircle()
                        .build()).into(binding.profile);

        Glide.with(this)
                .load(contactsModel.image).placeholder(new AvatarGenerator.AvatarBuilder(this)
                        .setLabel(contactsModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(13)
                        .toCircle()
                        .build()).into(binding.profile2);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.add.setOnClickListener(v -> {
            if (Constants.checkPermission(this)) {
                int vis = binding.galleryLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                if (vis == View.VISIBLE) {
                    getList();
                    hideKeyboard();
                }
                binding.galleryLayout.setVisibility(vis);
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
                startActivityForResult(Intent.createChooser(intent, "Pick Image"), PICK_IMAGE_REQUEST);
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
                    binding.iconSend.setImageResource(R.drawable.send_lock);
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
                    int vis = binding.galleryLayout.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
                    if (vis == View.VISIBLE) {
                        getList();
                        hideKeyboard();
                    }
                    binding.galleryLayout.setVisibility(vis);
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
                if (Stash.getBoolean(Constants.EDIT_MODE, false)) {
                    new MaterialAlertDialogBuilder(this)
                            .setMessage("Send message to whom?")
                            .setPositiveButton("Myself", (dialog, which) -> {
                                dialog.dismiss();
                                receive(binding.message.getText().toString() + "\t\t", false, binding.message.getText().toString());
                            }).setNegativeButton(contactsModel.name, (dialog, which) -> {
                                dialog.dismiss();
                                send(binding.message.getText().toString() + "\t\t\t", false, binding.message.getText().toString());
                            })
                            .show();
                } else {
                    send(binding.message.getText().toString() + "\t\t\t", false, binding.message.getText().toString());
                }
            }
        });
    }

    ArrayList<MessageModel> list;
    MessageAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        getMessages();
    }

    DeleteListener deleteListener = new DeleteListener() {
        @Override
        public void onHoldClick(MessageModel messageModel) {
            Dialog dialog = new Dialog(ChatActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.delete_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.show();

            TextView heading = dialog.findViewById(R.id.heading);
            TextView message = dialog.findViewById(R.id.message);
            MaterialButton cancel = dialog.findViewById(R.id.cancel);
            MaterialButton delete = dialog.findViewById(R.id.delete);


            heading.setText("Delete Message");
            message.setText("Are you sure you want to delete this message?");
            delete.setText("Delete");

            cancel.setOnClickListener(v -> dialog.dismiss());

            delete.setOnClickListener(v -> {
                dialog.dismiss();
                deleteMessage(messageModel);
            });
        }

        @Override
        public void onEdit(MessageModel messageModel) {
            Dialog dialog = new Dialog(ChatActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.edit_layout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(true);
            dialog.show();

            TextInputLayout message = dialog.findViewById(R.id.message);
            MaterialButton cancel = dialog.findViewById(R.id.cancel);
            MaterialButton time = dialog.findViewById(R.id.time);
            MaterialButton delete = dialog.findViewById(R.id.delete);
            TextView pikedTime = dialog.findViewById(R.id.pikedTime);

            final String[] t = {""};
            final long[] selectedTimeInMillis = new long[1];
            selectedTimeInMillis[0] = messageModel.getTimestamp();
            String s = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(selectedTimeInMillis[0]);
            pikedTime.setText(s);
            t[0] = s;

            time.setOnClickListener(v -> {
                MaterialTimePicker picker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setPositiveButtonText("Set Time")
                        .build();
                picker.show(ChatActivity.this.getSupportFragmentManager(), "");
                picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int selectedHour = picker.getHour();
                        int selectedMinute = picker.getMinute();
                        selectedTimeInMillis[0] = convertToMilliseconds(selectedHour, selectedMinute);
                        t[0] = selectedHour + ":" + selectedMinute;
                        pikedTime.setText(t[0]);
                    }
                });
            });

            cancel.setOnClickListener(v -> dialog.dismiss());

            delete.setOnClickListener(v -> {
                dialog.dismiss();
                editMessage(messageModel, selectedTimeInMillis[0], message.getEditText().getText().toString());
            });
        }
    };

    private long convertToMilliseconds(int hour, int minute) {
        return (hour * 60L + minute) * 60L * 1000L;
    }

    private void editMessage(MessageModel messageModel, long time, String msg) {
        int i = retrievePosition(list, messageModel.getId());
        if (i != -1) {
            list.get(i).setMessage(msg + "\t\t\t");
            list.get(i).setTimestamp(time);
            adapter.notifyItemRemoved(i);
            Stash.put(contactsModel.id, list);
        }
    }

    public static int retrievePosition(ArrayList<MessageModel> modelList, String id) {
        for (int index = 0; index < modelList.size(); index++) {
            MessageModel model = modelList.get(index);
            if (model.getId().equals(id)) {
                return index;
            }
        }
        return -1;
    }

    private void deleteMessage(MessageModel messageModel) {
        int i = retrievePosition(list, messageModel.getId());
        Log.d(TAG, "deleteMessage: " + messageModel.getId());
        Log.d(TAG, "deleteMessage: " + i);
        if (i != -1) {
            list.remove(i);
            adapter.notifyItemRemoved(i);
            Stash.put(contactsModel.id, list);

            contactsModel.lastMessage = "Message Deleted";
            contactsModel.time = new Date().getTime();
            ArrayList<ContactsModel> chatList = Stash.getArrayList(Constants.USERS, ContactsModel.class);
            int index = retrieveIndex(chatList);
            chatList.set(index, contactsModel);
            Stash.put(Constants.USERS, chatList);
        }
    }

    private void getMessages() {
        list = Stash.getArrayList(contactsModel.id, MessageModel.class);
        adapter = new MessageAdapter(this, list, contactsModel.name, deleteListener);
        binding.chatRC.setAdapter(adapter);
        binding.chatRC.scrollToPosition(list.size() - 1);
    }

    private void receive(String message, boolean isMedia, String last) {
        if (!message.isEmpty() || isMedia) {
            binding.message.setText("");
            contactsModel.lastMessage = last;
            contactsModel.time = new Date().getTime();
            ArrayList<ContactsModel> chatList = Stash.getArrayList(Constants.USERS, ContactsModel.class);
            int index = retrieveIndex(chatList);
            chatList.set(index, contactsModel);
            Stash.put(Constants.USERS, chatList);
            MessageModel messageModel = new MessageModel(UUID.randomUUID().toString(), contactsModel.id, message, imageUri.toString(), new Date().getTime(), isMedia, false);
            list.add(messageModel);
            Stash.put(contactsModel.id, list);
            adapter.notifyItemInserted(list.size() - 1);
            binding.chatRC.scrollToPosition(list.size() - 1);
        }
    }

    private void send(String message, boolean isMedia, String last) {
        if (!message.isEmpty() || isMedia) {
            binding.message.setText("");
            contactsModel.lastMessage = last;
            contactsModel.time = new Date().getTime();
            ArrayList<ContactsModel> chatList = Stash.getArrayList(Constants.USERS, ContactsModel.class);
            int index = retrieveIndex(chatList);
            chatList.set(index, contactsModel);
            Stash.put(Constants.USERS, chatList);
            UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
            MessageModel messageModel = new MessageModel(UUID.randomUUID().toString(), userModel.number, message, imageUri.toString(), new Date().getTime(), isMedia, false);
            list.add(messageModel);
            Stash.put(contactsModel.id, list);
            adapter.notifyItemInserted(list.size() - 1);
            binding.chatRC.scrollToPosition(list.size() - 1);
        }
    }

    private int retrieveIndex(ArrayList<ContactsModel> list) {
        for (int i = 0; i < list.size(); i++) {
            ContactsModel model = list.get(i);
            if (model.id.equals(contactsModel.id)) {
                return i;
            }
        }
        return -1;
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
    }

    Uri imageUri = Uri.EMPTY;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            showImagePick(imageUri.toString());
        }
    }

    private void showImagePick(String image) {

        Dialog imagePicker = new Dialog(this);
        imagePicker.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imagePicker.setContentView(R.layout.image_pick_layout);
        imagePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        imagePicker.setCancelable(true);
        imagePicker.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imagePicker.show();

        TextView name = imagePicker.findViewById(R.id.name);
        TextView add = imagePicker.findViewById(R.id.add);
        ImageView imageView = imagePicker.findViewById(R.id.image);
        MaterialCardView send = imagePicker.findViewById(R.id.send);
        MaterialCardView addSend = imagePicker.findViewById(R.id.addSend);
        EditText message = imagePicker.findViewById(R.id.message);
        EditText messageAdd = imagePicker.findViewById(R.id.messageAdd);
        LinearLayout addMessageLayout = imagePicker.findViewById(R.id.addMessageLayout);

        Glide.with(this).load(image).into(imageView);
        name.setText(contactsModel.name);
        message.setText(binding.message.getText().toString());

        add.setOnClickListener(v -> {
            add.setVisibility(View.GONE);
            addMessageLayout.setVisibility(View.VISIBLE);
            messageAdd.requestFocus();
        });

        if (!binding.message.getText().toString().isEmpty()){
            message.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
        }

        addSend.setOnClickListener(v -> {
            addMessageLayout.setVisibility(View.GONE);
            messageAdd.clearFocus();
            if (messageAdd.getText().toString().isEmpty()){
                message.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
            } else {
                message.setVisibility(View.VISIBLE);
                message.setText(messageAdd.getText().toString());
                add.setVisibility(View.GONE);
            }
            messageAdd.setText("");
            hideKeyboard();
        });

        send.setOnClickListener(v -> {
            imagePicker.dismiss();
            if (Stash.getBoolean(Constants.EDIT_MODE, false)) {
                new MaterialAlertDialogBuilder(ChatActivity.this)
                        .setMessage("Send message to whom?")
                        .setPositiveButton("Myself", (dialog, which) -> {
                            dialog.dismiss();
                            imageUri = Uri.parse(image);
                            receive(message.getText().toString(), true, "Photo");
                            imageUri = Uri.EMPTY;
                        }).setNegativeButton(contactsModel.name, (dialog, which) -> {
                            dialog.dismiss();
                            imageUri = Uri.parse(image);
                            send(message.getText().toString(), true, "Photo");
                            imageUri = Uri.EMPTY;
                        })
                        .show();
            } else {
                imageUri = Uri.parse(image);
                send(message.getText().toString(), true, "Photo");
                imageUri = Uri.EMPTY;
            }
        });
    }

    private static final String TAG = "ChatActivity";
    ImagePick imagePick = image -> showImagePick(image);
}