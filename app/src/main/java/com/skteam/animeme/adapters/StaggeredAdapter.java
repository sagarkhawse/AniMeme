package com.skteam.animeme.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
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
import com.skteam.animeme.ViewImageActivity;
import com.skteam.animeme.common.Common;
import com.skteam.animeme.models.Image;

import java.util.List;

public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ImageViewHolder>{
Context mContext;
List<Image> mList;

    public StaggeredAdapter(Context context, List<Image> list) {
       mContext = context;
       mList = list;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(mContext).inflate(R.layout.item_image,parent,false);
      return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, final int position) {
final Image data = mList.get(position);

        Glide.with(mContext).load(data.getImage()).into(holder.imageView);


            holder.title.setText(data.getTitle());
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

//
//                Pair[] pairs = new Pair[1];
//                pairs[0] = new Pair<View,String>(holder.imageView,"imageTransition");
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)mContext, pairs);
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
        TextView title;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
