package com.moutamid.signalcopy.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fxn.stash.Stash;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.databinding.FragmentNumberBinding;

public class NumberFragment extends Fragment {
    FragmentNumberBinding binding;
    public NumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNumberBinding.inflate(getLayoutInflater(), container, false);

        binding.continueBtn.setOnClickListener(v -> {
            if (binding.number.getEditText().getText().toString().isEmpty()){
                new MaterialAlertDialogBuilder(requireContext())
                        .setMessage("Please enter a valid phone number to register.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            } else {
                String numb = binding.ccp.getSelectedCountryCodeWithPlus() + binding.number.getEditText().getText().toString();
                Stash.put("numb", numb);
                requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NameFragment()).commit();
            }
        });

        return binding.getRoot();

    }
}