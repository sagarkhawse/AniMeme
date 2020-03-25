package com.skteam.animeme;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.skteam.animeme.adapters.StaggeredAdapter;
import com.skteam.animeme.common.Common;
import com.skteam.animeme.helper.MemeDownloadHelper;
import com.skteam.animeme.models.Image;
import com.skteam.animeme.models.ImageDataResponse;
import com.skteam.animeme.retrofit.AnimemeApi;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stfalcon.imageviewer.StfalconImageViewer;

import com.stfalcon.imageviewer.listeners.OnImageChangeListener;
import com.stfalcon.imageviewer.loader.ImageLoader;


import java.io.BufferedInputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.skteam.animeme.common.Common.isShare;
import static com.skteam.animeme.common.Common.isShareFb;
import static com.skteam.animeme.common.Common.isShareInstagram;
import static com.skteam.animeme.common.Common.isShareWhatsapp;

public class ViewImageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView;
    private RecyclerView recycler_view;
    private StaggeredGridLayoutManager layoutManager;
    private StaggeredAdapter adapter;
    private String viewed_image, category, title;
    private int id;
    private List<Image> imageList;
    private AnimemeApi mService;
    private TextView image_title, image_category;
    private ProgressDialog progressDialog;
    private ImageView fab_download, fab_share, fab_whatsapp, fab_facebook, fab_instagram;
    String image_path;
    private LinearLayout custom_view_imageviewer_linear_layout;
private  ProgressDialog progressDialog1;
    File input_file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//// set an enter transition
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setEnterTransition(new Slide());
//            getWindow().setExitTransition(new Slide());
//        }
//// set an exit transition
//
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow(); // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }

        setContentView(R.layout.activity_view_image);
        imageView = findViewById(R.id.imageView);
        image_title = findViewById(R.id.image_title);
        image_category = findViewById(R.id.image_category);
        fab_download = findViewById(R.id.fab_download);
        fab_share = findViewById(R.id.fab_share);
        fab_whatsapp = findViewById(R.id.fab_whatsapp);
        fab_facebook = findViewById(R.id.fab_fb);
        fab_instagram = findViewById(R.id.fab_instagram);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();

        progressDialog1 = new ProgressDialog(ViewImageActivity.this);
        progressDialog1.setTitle("Sharing Image");
        progressDialog1.setMessage("please wait");
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(layoutManager);
        try {
            Bundle bundle = getIntent().getExtras();
            viewed_image = bundle.getString("image");
            category = bundle.getString("category");
            title = bundle.getString("title");
            id = bundle.getInt("id");
            Glide.with(this).load(viewed_image).into(imageView);
            image_category.setText("Images related to " + category);
            image_title.setText(title);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mService = Common.getAPI();
        if (Common.isMemeSelected) {
            mService.getMemeListByCategory(category).enqueue(new Callback<ImageDataResponse>() {
                @Override
                public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                    if (!response.body().isError()) {
                        imageList = response.body().getRes();
                        adapter = new StaggeredAdapter(ViewImageActivity.this, imageList);
                        recycler_view.setAdapter(adapter);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ViewImageActivity.this, "failed to load", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ViewImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (Common.isTemplateSelected) {
            mService.getTemplateListByCategory(category).enqueue(new Callback<ImageDataResponse>() {
                @Override
                public void onResponse(Call<ImageDataResponse> call, Response<ImageDataResponse> response) {
                    if (!response.body().isError()) {
                        imageList = response.body().getRes();
                        adapter = new StaggeredAdapter(ViewImageActivity.this, imageList);
                        recycler_view.setAdapter(adapter);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ViewImageActivity.this, "failed to load", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ImageDataResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ViewImageActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View view1 = getLayoutInflater().inflate(R.layout.custom_view_imageviewer, null);
                final StfalconImageViewer.Builder<Image> builder = new StfalconImageViewer.Builder<>(ViewImageActivity.this, Common.imageList, new ImageLoader<Image>() {
                    @Override
                    public void loadImage(ImageView imageView, Image image) {

                        Glide.with(ViewImageActivity.this).load(image.getImage()).into(imageView);
custom_view_imageviewer_linear_layout = view1.findViewById(R.id.custom_view_imageviewer_linear_layout);


                    }
                });

                builder.withStartPosition(Common.imageListPosition)
                        .allowSwipeToDismiss(true)
                        .allowZooming(true)
                        .withHiddenStatusBar(true)
                        .withTransitionFrom(imageView)
                        .withOverlayView(view1)
                        .withImageChangeListener(new OnImageChangeListener() {
                            @Override
                            public void onImageChange(final int position) {
                                final Image image = Common.imageList.get(position);
                                ImageView download, share, whatsapp, fb, instagram;
                                 download = view1.findViewById(R.id.fab_download1);
                                 share = view1.findViewById(R.id.fab_share1);
                                whatsapp = view1.findViewById(R.id.fab_whatsapp1);
                                fb = view1.findViewById(R.id.fab_fb1);
                                instagram = view1.findViewById(R.id.fab_instagram1);
                                custom_view_imageviewer_linear_layout.setVisibility(View.VISIBLE);

                                download.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        MemeDownloadHelper memeDownloadHelper = new MemeDownloadHelper(ViewImageActivity.this, image.getTitle(), image.getId());
                                        memeDownloadHelper.new DownloadTask().execute(image.getImage());
                                    }
                                });

                                share.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isShare = true;
                                        shareItem(image.getImage());
                                    }
                                });
                                whatsapp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isShareWhatsapp = true;
                                        shareItem(image.getImage());
                                    }
                                });
                                fb.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isShareFb = true;
                                        shareItem(image.getImage());
                                    }
                                });
                                instagram.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        isShareInstagram = true;
                                        shareItem(image.getImage());
                                    }
                                });

                            }
                        })
                        .withBackgroundColorResource(android.R.color.background_dark).show();
            }
        });

        fab_download.setOnClickListener(this);
        fab_share.setOnClickListener(this);
        fab_whatsapp.setOnClickListener(this);
        fab_facebook.setOnClickListener(this);
        fab_instagram.setOnClickListener(this);
    }


    public void shareItem(String url) {
        progressDialog1.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog1.dismiss();
            }
        },6000);

        Picasso.get().load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                if (isShareWhatsapp) {

                    i.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(i, "Share Image"));
                    flaseAllValue();

                } else if (isShareFb) {
                    i.setPackage("com.facebook.katana");
                    startActivity(Intent.createChooser(i, "Share Image"));
                    flaseAllValue();
                } else if (isShareInstagram) {
                    i.setPackage("com.instagram.android");
                    startActivity(Intent.createChooser(i, "Share Image"));
                    flaseAllValue();
                } else if (isShare) {
                    startActivity(Intent.createChooser(i, "Share Image"));
                    flaseAllValue();

                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image" + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(ViewImageActivity.this, getPackageName() + ".provider", file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;
    }

    private void flaseAllValue() {

        isShareWhatsapp = false;
        isShareInstagram = false;
        isShare = false;
        isShareFb = false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_download:
                memeDownloadProcess();
                break;

            case R.id.fab_share:
                isShare = true;
                memeShareProcess();
                break;

            case R.id.fab_fb:
                isShareFb = true;
                memeShareProcess();
                break;

            case R.id.fab_whatsapp:
                isShareWhatsapp = true;
                memeShareProcess();
                break;

            case R.id.fab_instagram:
                isShareInstagram = true;
                memeShareProcess();
                break;


        }
    }

    private void memeDownloadProcess() {
        MemeDownloadHelper memeDownloadHelper = new MemeDownloadHelper(ViewImageActivity.this, title, id);
        memeDownloadHelper.new DownloadTask().execute(viewed_image);
    }

    private void memeShareProcess() {
        MemeDownloadHelper memeDownloadHelper = new MemeDownloadHelper(ViewImageActivity.this, imageView);
        memeDownloadHelper.shareImage();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
