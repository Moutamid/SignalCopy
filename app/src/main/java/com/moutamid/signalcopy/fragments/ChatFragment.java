package com.moutamid.signalcopy.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.adapters.ContactsAdapter;
import com.moutamid.signalcopy.databinding.FragmentChatBinding;
import com.moutamid.signalcopy.model.ContactsModel;
import com.moutamid.signalcopy.model.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(getLayoutInflater(), container, false);

        binding.pen.setOnClickListener(v -> {
            showAddContact();
        });

        binding.chatRC.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.chatRC.setHasFixedSize(false);

        getList();

        return binding.getRoot();
    }

    private void getList() {
        ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
        ContactsAdapter adapter = new ContactsAdapter(requireContext(), list);
        binding.chatRC.setAdapter(adapter);

        if (list.size() > 0){
            binding.noLayout.setVisibility(View.GONE);
        }

    }

    private void showAddContact() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_user);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        MaterialButton save = dialog.findViewById(R.id.save);
        MaterialButton time = dialog.findViewById(R.id.time);
        TextInputLayout name = dialog.findViewById(R.id.name);
        TextInputLayout number = dialog.findViewById(R.id.number);
        TextInputLayout lastMessage = dialog.findViewById(R.id.lastMessage);
        TextView pikedTime = dialog.findViewById(R.id.pikedTime);
        final String[] t = {""};
        final long[] selectedTimeInMillis = new long[1];
        selectedTimeInMillis[0] = new Date().getTime();
        String s = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTimeInMillis[0]);
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
            picker.show(requireActivity().getSupportFragmentManager(),  "");
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

        save.setOnClickListener(v -> {
            if (name.getEditText().getText().toString().isEmpty() || t[0].isEmpty()){
                Toast.makeText(requireContext(), "Please fill all details", Toast.LENGTH_SHORT).show();
            } else {
                ContactsModel model = new ContactsModel(UUID.randomUUID().toString(), name.getEditText().getText().toString(), lastMessage.getEditText().getText().toString(), number.getEditText().getText().toString(), selectedTimeInMillis[0]);
                ArrayList<ContactsModel> list = Stash.getArrayList(Constants.USERS, ContactsModel.class);
                list.add(model);
                Stash.put(Constants.USERS, list);
                getList();
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private long convertToMilliseconds(int hour, int minute) {
        return (hour * 60L + minute) * 60L * 1000L;
    }

}