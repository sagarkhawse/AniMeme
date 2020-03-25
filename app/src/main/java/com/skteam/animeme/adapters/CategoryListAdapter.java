package com.skteam.animeme.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.skteam.animeme.ImageListActivity;
import com.skteam.animeme.R;
import com.skteam.animeme.models.Image;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ImageViewHolder> {
Context mContext;
List<Image> mList;

    public CategoryListAdapter(Context context, List<Image> list) {
       mContext = context;
      mList = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(mContext).inflate(R.layout.item_category_card,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final Image data = mList.get(position);

        Glide.with(mContext).load(data.getImage()).into(holder.imageCategory);
        holder.category.setText(data.getCategory());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ImageListActivity.class);
                intent.putExtra("category",data.getCategory());
                intent.putExtra("categoryType","memeCategory");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
ImageView imageCategory;
TextView category;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCategory = itemView.findViewById(R.id.imageView);
            category = itemView.findViewById(R.id.category);

        }
    }
}
