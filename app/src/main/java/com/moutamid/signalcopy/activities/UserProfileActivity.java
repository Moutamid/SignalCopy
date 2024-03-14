package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.adapters.SharedImageAdapter;
import com.moutamid.signalcopy.databinding.ActivityUserProfileBinding;
import com.moutamid.signalcopy.model.ContactsModel;
import com.moutamid.signalcopy.model.MessageModel;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ContactsModel contactsModel = (ContactsModel) Stash.getObject("PASS", ContactsModel.class);

        binding.back.setOnClickListener(v -> onBackPressed());

        binding.name.setText(contactsModel.name);
        binding.name2.setText(contactsModel.name + " >");

        ArrayList<String> list = new ArrayList<>();
        ArrayList<MessageModel> messages  = Stash.getArrayList(contactsModel.id, MessageModel.class);

        for (MessageModel message : messages){
            if (message.isMedia()){
                list.add(message.getImage());
            }
        }

        SharedImageAdapter adapter = new SharedImageAdapter(this, list);
        binding.imagesRC.setAdapter(adapter);

        if (contactsModel.image.isEmpty()){
            binding.profile.cardViewRoot.setCardBackgroundColor(contactsModel.bgColor);
            binding.profile.text.setTextColor(contactsModel.textColor);
            binding.profile.text.setText(contactsModel.iconName);
            binding.profile.text.setVisibility(View.VISIBLE);
            binding.profile.profile.setVisibility(View.GONE);

            binding.profile.text.setTextSize(11);
            binding.profile2.text.setTextSize(22);

            binding.profile2.cardViewRoot.setCardBackgroundColor(contactsModel.bgColor);
            binding.profile2.text.setTextColor(contactsModel.textColor);
            binding.profile2.text.setText(contactsModel.iconName);
            binding.profile2.text.setVisibility(View.VISIBLE);
            binding.profile2.profile.setVisibility(View.GONE);
        } else {
            binding.profile.text.setVisibility(View.GONE);
            binding.profile.profile.setVisibility(View.VISIBLE);
            binding.profile2.text.setVisibility(View.GONE);
            binding.profile2.profile.setVisibility(View.VISIBLE);
            Glide.with(this).load(contactsModel.image).into(binding.profile.profile);
            Glide.with(this).load(contactsModel.image).into(binding.profile2.profile);
        }

        binding.scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    setStatusBarColor(getResources().getColor(R.color.white));
                    binding.toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                    binding.nameLayout.setVisibility(View.GONE);
                } else {
                    setStatusBarColor(getResources().getColor(R.color.bottom));
                    binding.toolbar.setBackgroundColor(getResources().getColor(R.color.bottom));
                    binding.nameLayout.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

}