package com.moutamid.signalcopy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.signalcopy.ImagePick;
import com.moutamid.signalcopy.R;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryVH> {

    Context context;
    ArrayList<String> list;
    ImagePick imagePick;

    public GalleryAdapter(Context context, ArrayList<String> list, ImagePick imagePick) {
        this.context = context;
        this.list = list;
        this.imagePick = imagePick;
    }

    @NonNull
    @Override
    public GalleryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryVH(LayoutInflater.from(context).inflate(R.layout.gallery_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryVH holder, int position) {
        String path = list.get(holder.getAbsoluteAdapterPosition());
        Glide.with(context).load(path).into(holder.image);
        holder.itemView.setOnClickListener(v -> imagePick.imagePick(path));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GalleryVH extends RecyclerView.ViewHolder{

        ImageView image;

        public GalleryVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }

}
