package com.moutamid.signalcopy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.fxn.stash.Stash;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.databinding.ActivityLoginBinding;
import com.moutamid.signalcopy.fragments.NumberFragment;
import com.moutamid.signalcopy.model.UserModel;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);

        if (Stash.getObject(Constants.STASH_USER, UserModel.class) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NumberFragment()).commit();
    }
}