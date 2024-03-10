package com.moutamid.signalcopy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.databinding.ActivityChatBinding;
import com.moutamid.signalcopy.model.ContactsModel;

import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    ContactsModel contactsModel;

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
    }
}