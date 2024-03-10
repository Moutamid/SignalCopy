package com.moutamid.signalcopy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.moutamid.signalcopy.databinding.ActivityMainBinding;
import com.moutamid.signalcopy.fragments.CallFragment;
import com.moutamid.signalcopy.fragments.ChatFragment;
import com.moutamid.signalcopy.fragments.NumberFragment;
import com.moutamid.signalcopy.fragments.StoryFragment;
import com.moutamid.signalcopy.model.UserModel;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        Glide.with(this).load(userModel.getImage()).placeholder(
                new AvatarGenerator.AvatarBuilder(MainActivity.this)
                        .setLabel(userModel.getName().trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(R.color.pink)
                        .setTextSize(13)
                        .toCircle()
                        .build()
        ).into(binding.profile);

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