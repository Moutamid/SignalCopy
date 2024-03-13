package com.moutamid.signalcopy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.activities.ChatActivity;
import com.moutamid.signalcopy.listeners.ContactListener;
import com.moutamid.signalcopy.model.ContactsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactVH> {

    Context context;
    ArrayList<ContactsModel> list;
    ContactListener contactListener;

    public ContactsAdapter(Context context, ArrayList<ContactsModel> list, ContactListener contactListener) {
        this.context = context;
        this.list = list;
        this.contactListener = contactListener;
    }

    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactVH(LayoutInflater.from(context).inflate(R.layout.contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {
        ContactsModel model = list.get(holder.getAbsoluteAdapterPosition());
        Glide.with(context)
                .load(new AvatarGenerator.AvatarBuilder(context)
                        .setLabel(model.name.trim().toUpperCase(Locale.ROOT))
                        .setAvatarSize(70)
                        .setBackgroundColor(Constants.COLORS[new Random().nextInt(Constants.COLORS.length)])
                        .setTextSize(13)
                        .toCircle()
                        .build()
                ).into(holder.profile);
        holder.name.setText(model.name);
        holder.lastMessage.setText(model.lastMessage);
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifference = currentTimeMillis - model.time;
        if (timeDifference <= 60 * 1000) {
            holder.time.setText("now");
        } else if (timeDifference <= 60 * 60 * 1000) {
            long minutesPassed = timeDifference / (60 * 1000);
            holder.time.setText(minutesPassed + "m");
        } else if (timeDifference <= 24 * 60 * 60 * 1000) {
            long hoursPassed = timeDifference / (60 * 60 * 1000);
            holder.time.setText(hoursPassed + "h");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            String dayName = dateFormat.format(new Date(model.time));
            holder.time.setText(dayName);
        }
        holder.itemView.setOnClickListener(v -> {
            Stash.put("PASS", model);
            context.startActivity(new Intent(context, ChatActivity.class));
        });

        holder.itemView.setOnLongClickListener(v -> {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.buttons, null);
            PopupWindow popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAsDropDown(v);

            MaterialButton edit = customView.findViewById(R.id.edit);
            MaterialButton delete = customView.findViewById(R.id.delete);

            edit.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                contactListener.onCLick(list.get(holder.getAbsoluteAdapterPosition()));
            });
            delete.setOnClickListener(v1 -> {
                popupWindow.dismiss();
                contactListener.onDelete(list.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition());
            });

            return false;
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContactVH extends RecyclerView.ViewHolder {
        TextView time, name, lastMessage;
        CircleImageView profile;

        public ContactVH(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            name = itemView.findViewById(R.id.name);
            profile = itemView.findViewById(R.id.profile);
        }
    }

}
