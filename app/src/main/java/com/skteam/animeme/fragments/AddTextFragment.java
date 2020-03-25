package com.skteam.animeme.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skteam.animeme.R;
import com.skteam.animeme.adapters.ColorAdapter;
import com.skteam.animeme.adapters.FontAdapter;
import com.skteam.animeme.interfac.AddTextFragmentListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener, FontAdapter.FontAdapterClickListener {

int colorSelected = Color.parseColor("#000000"); //default color

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    AddTextFragmentListener listener;

    EditText edit_add_text;
    RecyclerView recyclerView_color,recyclerView_font;
    Button btn_done;

    Typeface typefaceSelected = Typeface.DEFAULT_BOLD;

    static AddTextFragment instance;

    public static AddTextFragment getInstance() {
        if (instance == null)
            instance = new AddTextFragment();
        return instance;
    }

    public AddTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_add_text, container, false);

        edit_add_text = itemView.findViewById(R.id.edit_add_text);
        btn_done = itemView.findViewById(R.id.btn_done);
        recyclerView_color = itemView.findViewById(R.id.recycler_color);
        recyclerView_color.setHasFixedSize(true);
        recyclerView_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));


        recyclerView_font = itemView.findViewById(R.id.recycler_font);
        recyclerView_font.setHasFixedSize(true);
        recyclerView_font.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));


        ColorAdapter colorAdapter = new ColorAdapter(getContext(),this);
        recyclerView_color.setAdapter(colorAdapter);

        FontAdapter fontAdapter = new FontAdapter(getContext(),this);
        recyclerView_font.setAdapter(fontAdapter);

        //event
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddTextButtonClick(typefaceSelected,edit_add_text.getText().toString(),colorSelected);
            }
        });
    return itemView;
    }

    @Override
    public void onColorSelected(int color) {
colorSelected = color; // set color when user selects
    }

    @Override
    public void onFontSelected(String fontName) {
        typefaceSelected = Typeface.createFromAsset(getContext().getAssets(),new StringBuilder("fonts/")
        .append(fontName).toString());

    }
}
