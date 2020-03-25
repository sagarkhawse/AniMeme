package com.skteam.animeme.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.skteam.animeme.R;
import com.skteam.animeme.adapters.EmojiAdapter;
import com.skteam.animeme.interfac.EmojiFragmentListener;


import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmojiFragment extends BottomSheetDialogFragment implements EmojiAdapter.EmojiAdapterListener {

RecyclerView recyclerViewEmoji;
static EmojiFragment instance;

    public void setListener(EmojiFragmentListener listener) {
        this.listener = listener;
    }

    EmojiFragmentListener listener;



    public static EmojiFragment getInstance() {
        if (instance == null)
            instance = new EmojiFragment();
        return instance;
    }

    public EmojiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_emoji, container, false);

        recyclerViewEmoji = itemView.findViewById(R.id.recycler_view_emoji);
        recyclerViewEmoji.setHasFixedSize(true);
        recyclerViewEmoji.setLayoutManager(new GridLayoutManager(getActivity(),5));

        EmojiAdapter adapter = new EmojiAdapter(getContext(), PhotoEditor.getEmojis(getContext()),this);
recyclerViewEmoji.setAdapter(adapter);
    return itemView;
    }

    @Override
    public void onEmojiItemSelected(String emoji) {
listener.onEmojiSelected(emoji);
    }
}
