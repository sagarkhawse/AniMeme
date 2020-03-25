package com.skteam.animeme.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureSession;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Placeholder;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skteam.animeme.R;
import com.skteam.animeme.ViewImageActivity;
import com.skteam.animeme.common.Common;
import com.skteam.animeme.models.Image;

import java.util.List;

public class RandomImagesAdapter extends RecyclerView.Adapter<RandomImagesAdapter.ImageViewHolder>{
Context mContext;
List<Image> mList;

    public RandomImagesAdapter(Context context, List<Image> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.item_random_image,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
final Image data = mList.get(position);
        Glide.with(mContext).load(data.getImage()).placeholder(android.R.color.black).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ViewImageActivity.class);
                intent.putExtra("image",data.getImage());
                intent.putExtra("category",data.getCategory());
                intent.putExtra("title",data.getTitle());
                intent.putExtra("id",data.getId());
                Common.imageList = mList;
                Common.imageListPosition = position;
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
