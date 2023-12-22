package com.ka.task1.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ka.task1.R;
import com.ka.task1.model.Photo;

public class PhotoAdapter extends PagedListAdapter<Photo, PhotoAdapter.ViewHolder> {

    public PhotoAdapter(@NonNull DiffUtil.ItemCallback<Photo> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = getItem(position);
        if (photo != null) {
            Glide.with(holder.imageView)
                    .load(photo.getUrl())
                    .into(holder.imageView);
        }
    }

    /**
     * ViewHolder class for holding references to views inside each item of the RecyclerView.
     * Typically used in an adapter for efficient view recycling.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    /**
     * DiffUtil.ItemCallback for comparing Photo items in a list.
     * Used to efficiently update the contents of a RecyclerView.
     */
    public static final DiffUtil.ItemCallback<Photo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Photo>() {
        @Override
        public boolean areItemsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Photo oldItem, @NonNull Photo newItem) {
            return oldItem.equals(newItem);
        }
    };
}
