package com.moutamid.signalcopy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.signalcopy.activities.ChatActivity;
import com.moutamid.signalcopy.Constants;
import com.moutamid.signalcopy.R;
import com.moutamid.signalcopy.model.ContactsModel;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactVH> {

    Context context;
    ArrayList<ContactsModel> list;

    public ContactsAdapter(Context context, ArrayList<ContactsModel> list) {
        this.context = context;
        this.list = list;
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
                .load(
                        new AvatarGenerator.AvatarBuilder(context)
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
        } else {
            long hoursPassed = timeDifference / (60 * 60 * 1000);
            holder.time.setText(hoursPassed + "h");
        }
        holder.itemView.setOnClickListener(v -> {
            Stash.put("PASS", model);
            context.startActivity(new Intent(context, ChatActivity.class));
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
