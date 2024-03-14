package com.moutamid.signalcopy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.databinding.FragmentStoryBinding;
import com.moutamid.signalcopy.model.UserModel;

import java.util.Locale;
import java.util.Random;

public class StoryFragment extends Fragment {
    FragmentStoryBinding binding;
    public StoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStoryBinding.inflate(getLayoutInflater(), container, false);

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

        if (userModel.image.isEmpty()){
            binding.profile.text.setTextSize(20);
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

        return binding.getRoot();
    }
}