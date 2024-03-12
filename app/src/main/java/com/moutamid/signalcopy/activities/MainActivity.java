package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;

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

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        Log.d(TAG, "onCreate: " + userModel);

        Glide.with(this).load(userModel.image).placeholder(
                new AvatarGenerator.AvatarBuilder(MainActivity.this)
                        .setLabel(userModel.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(13)
                        .toCircle()
                        .build()
        ).into(binding.profile);

        binding.profile.setOnClickListener(v -> {
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
}