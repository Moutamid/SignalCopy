package com.moutamid.signalcopy.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avatarfirst.avatargenlib.AvatarGenerator;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
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

        if (model.image.isEmpty()){
            holder.profile.setVisibility(View.GONE);
            holder.text.setVisibility(View.VISIBLE);
            holder.text.setText(model.iconName);
            holder.text.setTextColor(model.textColor);
            holder.text.setTextSize(18);
            ((MaterialCardView)holder.view).setCardBackgroundColor(model.bgColor);
        } else {
            holder.profile.setVisibility(View.VISIBLE);
            holder.text.setVisibility(View.GONE);
            Glide.with(context).load(model.image).into(holder.profile);
        }

        holder.name.setText(model.name);
        holder.lastMessage.setText(model.lastMessage);
        holder.time.setText(model.time + "");

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
        View view;
        ImageView profile;
        TextView text;

        public ContactVH(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            name = itemView.findViewById(R.id.name);
            view = itemView.findViewById(R.id.profile2);
            text = view.findViewById(R.id.text);
            profile = view.findViewById(R.id.profile);
        }
    }

}
