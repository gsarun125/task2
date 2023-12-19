package com.ka.task1.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka.task1.R;
import com.ka.task1.model.Photo;

import java.util.List;

// ui/gallery/PhotoAdapter.java
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<Photo> photos;
    private Context context;
    public PhotoAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.imageView)
                .load(photos.get(position).getUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (photos==null){
            return 0;
        }
        else {
            return photos.size();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void setData(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }
}
