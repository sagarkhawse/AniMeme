package com.skteam.animeme;

import android.app.ActivityOptions;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.skteam.animeme.adapters.StaggeredAdapter;
import com.skteam.animeme.common.Common;
import com.skteam.animeme.models.Image;
import com.skteam.animeme.models.ImageDataResponse;
import com.skteam.animeme.retrofit.AnimemeApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageListActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private StaggeredGridLayoutManager layoutManager;
    private StaggeredAdapter adapter;
    private List<Image> imageList;
    private AnimemeApi mService;
    private String type, category, categoryType;
    private LottieAnimationView animation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        animation_view = findViewById(R.id.animation_view);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorViolate));
//        }

        try {
            Bundle bundle = getIntent().getExtras();
            type = bundle.getString("type");
            categoryType = bundle.getString("categoryType");
            category = bundle.getString("category");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mService = Common.getAPI();

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);

try {


    if (type != null && type.equals("meme")) {
        loadMemes();
        Common.isMemeSelected = true;
    } else if (type != null && type.equals("template")) {
        loadTemplates();
        Common.isTemplateSelected = true;
    } else if (categoryType != null && categoryType.equals("memeCategory")) {
        loadMemeListByCategory();
        Common.isMemeSelected = true;
    } else if (categoryType != null && categoryType.equals("templateCategory")) {
        loadTemplateListByCategory();
        Common.isTemplateSelected = true;
    }
}catch (NullPointerException e){
    e.printStackTrace();
}


    }

    private void loadTemplateListByCategory() {
mService.getTemplateListByCategory(category).enqueue(new Callback<ImageDataResponse>() {
    @Override
    public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
        if (!response.body().isError()) {
            imageList = response.body().getRes();
            animation_view.setVisibility(View.GONE);
            adapter = new StaggeredAdapter(ImageListActivity.this, imageList);
            recycler_view.setAdapter(adapter);
        } else {
            Log.d("MEMELIST", "onResponse: failed to get data");
        }
    }

    @Override
    public void onFailure(Call<ImageDataResponse> call, Throwable t) {
        Toast.makeText(ImageListActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
    }
});
    }

    private void loadMemeListByCategory() {
        mService.getMemeListByCategory(category).enqueue(new Callback<ImageDataResponse>() {
            @Override
            public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                if (!response.body().isError()) {
                    imageList = response.body().getRes();
                    animation_view.setVisibility(View.GONE);
                    adapter = new StaggeredAdapter(ImageListActivity.this, imageList);
                    recycler_view.setAdapter(adapter);
                } else {
                    Log.d("MEMELIST", "onResponse: failed to get data");
                }
            }

            @Override
            public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                Toast.makeText(ImageListActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTemplates() {

        mService.getTemplatesList().enqueue(new Callback<ImageDataResponse>() {
            @Override
            public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                if (!response.body().isError()) {
                    imageList = response.body().getRes();
                    animation_view.setVisibility(View.GONE);

                    adapter = new StaggeredAdapter(ImageListActivity.this, imageList);
                    recycler_view.setAdapter(adapter);
                } else {
                    Log.d("MEMELIST", "onResponse: failed to get data");
                }
            }

            @Override
            public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                Log.d("MEMELIST", "onfailed: " + t.getMessage());
            }
        });
    }

    private void loadMemes() {
        mService.getMemesList().enqueue(new Callback<ImageDataResponse>() {
            @Override
            public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                if (!response.body().isError()) {
                    imageList = response.body().getRes();
                    animation_view.setVisibility(View.GONE);
                    adapter = new StaggeredAdapter(ImageListActivity.this, imageList);
                    recycler_view.setAdapter(adapter);
                } else {
                    Log.d("MEMELIST", "onResponse: failed to get data");
                }
            }

            @Override
            public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                Log.d("MEMELIST", "onfailed: " + t.getMessage());
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Common.isTemplateSelected = false;
        Common.isMemeSelected = false;
    }
}
