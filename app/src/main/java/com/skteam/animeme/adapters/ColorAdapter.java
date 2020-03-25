package com.skteam.animeme.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.skteam.animeme.R;


import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
   Context context;
   List<Integer> colorList;
   ColorAdapterListener listener;

    public ColorAdapter(Context context,ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(context).inflate(R.layout.color_item,parent,false);
    return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
holder.color_section.setCardBackgroundColor(colorList.get(position));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder{
       public CardView color_section;

        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            color_section = itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorSelected(colorList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ColorAdapterListener{
        void onColorSelected(int color);
    }
    private List<Integer> genColorList() {
        List<Integer> colorList = new ArrayList<>();

        colorList.add(Color.parseColor("#5d8aa8"));
        colorList.add(Color.parseColor("#f0f8ff"));
        colorList.add(Color.parseColor("#e32636"));
        colorList.add(Color.parseColor("#efdecd"));
        colorList.add(Color.parseColor("#ffbf00"));
        colorList.add(Color.parseColor("#9966cc"));
        colorList.add(Color.parseColor("#a4c639"));
        colorList.add(Color.parseColor("#f2f3f4"));
        colorList.add(Color.parseColor("#cd9575"));
        colorList.add(Color.parseColor("#008000"));
        colorList.add(Color.parseColor("#915c83"));
        colorList.add(Color.parseColor("#00ffff"));
        colorList.add(Color.parseColor("#e9d66b"));
        colorList.add(Color.parseColor("#b2beb5"));
        colorList.add(Color.parseColor("#ff9966"));
        colorList.add(Color.parseColor("#a52a2a"));
        colorList.add(Color.parseColor("#007fff"));
        colorList.add(Color.parseColor("#89cff0"));
        colorList.add(Color.parseColor("#3d2b1f"));
        colorList.add(Color.parseColor("#000000"));


        colorList.add(Color.parseColor("#0000ff"));
        colorList.add(Color.parseColor("#8a2be2"));
        colorList.add(Color.parseColor("#79443b"));
        colorList.add(Color.parseColor("#e3dac9"));
        colorList.add(Color.parseColor("#006a4e"));
        colorList.add(Color.parseColor("#873260"));
        colorList.add(Color.parseColor("#b5a642"));
        colorList.add(Color.parseColor("#bf94e4"));
        colorList.add(Color.parseColor("#480607"));
        colorList.add(Color.parseColor("#bd33a4"));
        colorList.add(Color.parseColor("#91a3b0"));
        colorList.add(Color.parseColor("#fff600"));
        colorList.add(Color.parseColor("#4b3621"));

        return colorList;


    }

}
