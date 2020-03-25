package com.skteam.animeme.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skteam.animeme.EditorActivity;
import com.skteam.animeme.ImageListActivity;
import com.skteam.animeme.R;
import com.skteam.animeme.adapters.CategoryListAdapter;
import com.skteam.animeme.adapters.CategoryListTemplateAdapter;
import com.skteam.animeme.common.Common;
import com.skteam.animeme.models.Image;
import com.skteam.animeme.models.ImageDataResponse;
import com.skteam.animeme.retrofit.AnimemeApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private Button memes_btn,editor_btn,template_btn;
    private RecyclerView recycler_view_meme, recycler_view_template;
    private List<Image> imageCategoryList;
    private AnimemeApi mService;
    private LinearLayoutManager layoutManager,layoutManager2;
    private CategoryListAdapter adapter;
    private CategoryListTemplateAdapter adapterTemplate;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycler_view_meme = view.findViewById(R.id.recycler_view_meme);
        recycler_view_template = view.findViewById(R.id.recycler_view_template);

        recycler_view_meme.setHasFixedSize(true);
        recycler_view_template.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        layoutManager2 = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

        recycler_view_template.setLayoutManager(layoutManager);
        recycler_view_meme.setLayoutManager(layoutManager2);
mService = Common.getAPI();

loadMemeCategoryList();
        loadTemplateCategoryList();

        memes_btn = view.findViewById(R.id.memes_btn);
        template_btn = view.findViewById(R.id.template_btn);
        template_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageListActivity.class);
                intent.putExtra("type","template");
                startActivity(intent);
            }
        });
        editor_btn = view.findViewById(R.id.editor_btn);
        memes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImageListActivity.class);
                intent.putExtra("type","meme");
                startActivity(intent);
            }
        });
        editor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
    return view;
    }

    private void loadTemplateCategoryList() {
    mService.getImageCategoryList("template").enqueue(new Callback<ImageDataResponse>() {
        @Override
        public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
            if (!response.body().isError()){
               imageCategoryList = response.body().getRes();
                adapterTemplate = new CategoryListTemplateAdapter(getContext(),imageCategoryList);
                recycler_view_template.setAdapter(adapterTemplate);
            }else {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ImageDataResponse> call, Throwable t) {
            Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });

    }

    private void loadMemeCategoryList() {

        mService.getImageCategoryList("meme").enqueue(new Callback<ImageDataResponse>() {
            @Override
            public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                if (!response.body().isError()){
                    imageCategoryList = response.body().getRes();
                    adapter = new CategoryListAdapter(getContext(),imageCategoryList);
                    recycler_view_meme.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                Toast.makeText(getContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
