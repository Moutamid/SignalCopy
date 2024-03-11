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
import com.moutamid.signalcopy.MainActivity;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.databinding.FragmentStoryBinding;
import com.moutamid.signalcopy.model.UserModel;

import java.util.Locale;

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

        Glide.with(this)
                .load(userModel.image).placeholder(
                        new AvatarGenerator.AvatarBuilder(requireContext())
                                .setLabel(userModel.name.trim().toUpperCase(Locale.ROOT))
                                .setAvatarSize(70)
                                .setBackgroundColor(R.color.pink)
                                .setTextSize(13)
                                .toCircle()
                                .build()
                ).into(binding.profile);

        return binding.getRoot();
    }
}