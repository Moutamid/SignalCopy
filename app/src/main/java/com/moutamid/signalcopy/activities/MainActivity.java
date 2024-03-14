package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.databinding.ActivityMainBinding;
import com.moutamid.signalcopy.fragments.CallFragment;
import com.moutamid.signalcopy.fragments.ChatFragment;
import com.moutamid.signalcopy.fragments.StoryFragment;
import com.moutamid.signalcopy.model.UserModel;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);


        binding.profile.cardViewRoot.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });
        binding.more.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingActivity.class));
        });

        binding.bottomNav.setItemActiveIndicatorColor(ColorStateList.valueOf(getResources().getColor(R.color.active)));
        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.chat){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ChatFragment()).commit();
            } else if (id == R.id.call){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CallFragment()).commit();
            } else if (id == R.id.story){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new StoryFragment()).commit();
            }

            return true;
        });
        binding.bottomNav.setSelectedItemId(R.id.chat);

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        Log.d(TAG, "onCreate: " + userModel);

        if (userModel.image.isEmpty()){
            binding.profile.text.setTextSize(11);
            binding.profile.cardViewRoot.setCardBackgroundColor(userModel.bgColor);
            binding.profile.text.setTextColor(userModel.textColor);
            binding.profile.text.setText(userModel.iconName);
            binding.profile.text.setVisibility(View.VISIBLE);
            binding.profile.profile.setVisibility(View.GONE);
        } else {
            binding.profile.text.setVisibility(View.GONE);
            binding.profile.profile.setVisibility(View.VISIBLE);
            Glide.with(this).load(userModel.image).into(binding.profile.profile);
        }
    }
}