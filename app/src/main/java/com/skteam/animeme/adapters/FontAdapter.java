package com.skteam.animeme.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.skteam.animeme.R;

import java.util.ArrayList;
import java.util.List;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    Context context;
    int row_selected = -1;

    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        fontList = loadFontList();
    }

    private List<String> loadFontList() {
        List<String> result = new ArrayList<>();
        result.add("GreatVibes-Regular.otf");
        result.add("Chunk Five Print.otf");
        result.add("GrandHotel-Regular.otf");
        result.add("KaushanScript-Regular.otf");
        result.add("Lobster.otf");
        result.add("Sofia-Regular.otf");
        result.add("FiraSans-Bold.otf");
        result.add("FiraSans-BoldItalic.otf");
        result.add("FiraSans-Book.otf");
        result.add("FiraSans-BookItalic.otf");
        result.add("LobsterTwo-Bold.otf");
        result.add("LobsterTwo-BoldItalic.otf");
        result.add("LobsterTwo-Italic.otf");
        result.add("LobsterTwo-Regular.otf");
        result.add("Titillium-Black.otf");
        result.add("Titillium-Bold.otf");
        result.add("Titillium-BoldItalic.otf");


        return result;
    }

    FontAdapterClickListener listener;
    List<String> fontList;

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item, parent, false);
        return new FontViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new StringBuilder("fonts/")
                .append(fontList.get(position)).toString());

        holder.txt_font_name.setText(fontList.get(position));
        holder.txt_font_demo.setTypeface(typeface);
    }


    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView txt_font_name, txt_font_demo;
        ImageView img_check;

        public FontViewHolder(View itemView) {
            super(itemView);

            txt_font_name = itemView.findViewById(R.id.txt_font_name);
            txt_font_demo = itemView.findViewById(R.id.txt_font_demo);

            img_check = itemView.findViewById(R.id.img_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface FontAdapterClickListener {
        public void onFontSelected(String fontName);
    }
}
