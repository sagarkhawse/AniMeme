package com.skteam.animeme;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.skteam.animeme.Utils.BitmapUtils;
import com.skteam.animeme.adapters.ViewPagerAdapter;
import com.skteam.animeme.fragments.AddTextFragment;
import com.skteam.animeme.fragments.BrushFragment;
import com.skteam.animeme.fragments.EditImageFragment;
import com.skteam.animeme.fragments.EmojiFragment;
import com.skteam.animeme.fragments.FilterListFragment;
import com.skteam.animeme.interfac.AddTextFragmentListener;
import com.skteam.animeme.interfac.BrushFragmentListener;
import com.skteam.animeme.interfac.EditImageFragmentListener;
import com.skteam.animeme.interfac.EmojiFragmentListener;
import com.skteam.animeme.interfac.FilterListFragmentListener;

import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.util.List;
import java.util.UUID;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class EditorActivity extends AppCompatActivity implements FilterListFragmentListener, EditImageFragmentListener, BrushFragmentListener, EmojiFragmentListener, AddTextFragmentListener {
    public static final String pictureName = "image.jpeg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    private static final int PERMISSION_INSERT_IMAGE = 2000;

    PhotoEditorView photoEditorView;
   PhotoEditor photoEditor;
   Uri image_selected_uri;

    CoordinatorLayout coordinatorLayout;

    Bitmap originalBitmap, filteredBitmap, finalBitmap;

    FilterListFragment filterListFragment;
    EditImageFragment editImageFragment;

    int brightnessFinal = 0;
    float saturationFinal =  1.0f;
    float constrantFinal = 1.0f;

    LinearLayout filters_btn, edit_btn, brush_btn, emoji_btn, add_text_btn, add_image_button, crop_btn;

    //loading native image filter library
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                        }else {
                            Toast.makeText(EditorActivity.this, "Permission denied! Some Functions may not work", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Qmour Filter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //view
        photoEditorView = findViewById(R.id.image_preview);
       photoEditor = new PhotoEditor.Builder(this,photoEditorView)
               .setPinchTextScalable(true)
               .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf"))
               .build();
        coordinatorLayout = findViewById(R.id.coordinator);

        filters_btn = findViewById(R.id.btn_filters_list);
        edit_btn = findViewById(R.id.btn_edit);
        brush_btn = findViewById(R.id.btn_brush);
        emoji_btn = findViewById(R.id.btn_emoji);
        add_text_btn = findViewById(R.id.btn_add_text);
        add_image_button = findViewById(R.id.btn_add_image);
        crop_btn = findViewById(R.id.btn_crop);



        openImageFromGallery();

        filters_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterListFragment != null){
                    filterListFragment.show(getSupportFragmentManager(),filterListFragment.getTag());
                }else {
                    FilterListFragment filterListFragment = FilterListFragment.getInstance(null);
                    filterListFragment.setListener(EditorActivity.this);
                    filterListFragment.show(getSupportFragmentManager(),filterListFragment.getTag());
                }


            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(EditorActivity.this);
              editImageFragment.show(getSupportFragmentManager(),editImageFragment.getTag());
            }
        });

        brush_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoEditor.setBrushDrawingMode(true);

                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(EditorActivity.this);
                brushFragment.show(getSupportFragmentManager(),brushFragment.getTag());
            }
        });

        emoji_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setListener(EditorActivity.this);
                emojiFragment.show(getSupportFragmentManager(),emojiFragment.getTag());
            }
        });

        add_text_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setListener(EditorActivity.this);
                addTextFragment.show(getSupportFragmentManager(),addTextFragment.getTag());
            }
        });

        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addImageToPicture();
            }
        });

        crop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop(image_selected_uri);
            }
        });

        loadImage();
//        new GetImageFromUrl().execute(bitmapURl);





    }

    private void startCrop(Uri uri) {
        String destinationFileName = new StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString();
        UCrop uCrop = UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.start(EditorActivity.this);
    }

    private void addImageToPicture() {

Dexter.withActivity(this)
        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,PERMISSION_INSERT_IMAGE);

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(EditorActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }).check();

    }

    private void loadImage() {
        originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),0);

        filterListFragment = new FilterListFragment();
        filterListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filterListFragment,"FILTERS");
        adapter.addFragment(editImageFragment, "EDIT");

        viewPager.setAdapter(adapter);



    }

    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onConstrantChanged(float constrant) {
        constrantFinal = constrant;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constrant));  /////////edmt made mistake here instead of write contrast filter saturation sub fiklter
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));

    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

Filter myFilter = new Filter();
myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));

finalBitmap = myFilter.processFilter(bitmap);
    }

    @Override
    public void onFiltersSelected(Filter filter) {

//        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);

    }

    private void resetControl() {
    if (editImageFragment != null)
        editImageFragment.resetControls();
    brightnessFinal=0;
    saturationFinal=1.0f;
    constrantFinal=1.0f;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu_item,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }

        if (id == R.id.open){
            openImageFromGallery();
        return true;
        }

        if (id == R.id.save){
            saveImageToGallery();
            return true;
        }
        return  super.onOptionsItemSelected(item);
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_PICK_IMAGE);
                        }else {
                            Toast.makeText(EditorActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
token.continuePermissionRequest();
                    }
                }).check();
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data); /////
        if (resultCode == RESULT_OK) {
            if (requestCode == PERMISSION_PICK_IMAGE){
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                image_selected_uri = data.getData();
                //clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(originalBitmap);

                bitmap.recycle();

                filterListFragment = FilterListFragment.getInstance(originalBitmap);
                filterListFragment.setListener(this);
            }else if (requestCode == PERMISSION_INSERT_IMAGE){
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this,data.getData(),250,250);
                photoEditor.addImage(bitmap);

            }
            else if (requestCode == UCrop.REQUEST_CROP)
                handleCropResult(data);

        }
        else if (resultCode == UCrop.RESULT_ERROR)
            handleCropError(data);
    }

    private void handleCropError(Intent data) {
        final Throwable cropError = UCrop.getError(data);
        if (cropError != null){
            Toast.makeText(this, ""+cropError.getMessage(), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Unexpected Error Occur", Toast.LENGTH_SHORT).show();
        }

    }

    private void handleCropResult(Intent data) {
        final  Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null){
            photoEditorView.getSource().setImageURI(resultUri);

            Bitmap bitmap = ((BitmapDrawable) photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap = originalBitmap;
            finalBitmap = originalBitmap;
        }
        else
            Toast.makeText(this, "Cannot retrieve crop image", Toast.LENGTH_SHORT).show();
    }

    private void saveImageToGallery(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                           photoEditor.saveAsBitmap(new OnSaveBitmap() {
                               @Override
                               public void onBitmapReady(Bitmap saveBitmap) {
                                   try {

                                       photoEditorView.getSource().setImageBitmap(saveBitmap);
                                       final String path = BitmapUtils.insertImage(getContentResolver(),
                                               saveBitmap,
                                               System.currentTimeMillis()+ "_profile.jpg",
                                               null);
                                       if (!TextUtils.isEmpty(path)){
                                           Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                   "Image saved to gallery",
                                                   Snackbar.LENGTH_LONG).setAction("OPEN", new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
                                                   openImage(path);
                                               }
                                           });
                                           snackbar.show();
                                       }else {
                                           Snackbar snackbar = Snackbar.make(coordinatorLayout,
                                                   "Unable to save image",
                                                   Snackbar.LENGTH_LONG);
                                           snackbar.show();
                                       }

                                   }catch (Exception e){
                                       e.printStackTrace();
                                   }
                               }

                               @Override
                               public void onFailure(Exception e) {

                               }
                           });

                        }else {
                            Toast.makeText(EditorActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openImage(String path) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);


    }

    @Override
    public void onBrushSizeChnagedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {
photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChnagedListener(int color) {
photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChnagedListener(boolean isEraser) {
if (isEraser)
    photoEditor.brushEraser();
else
    photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void onEmojiSelected(String emoji) {
        photoEditor.addEmoji(emoji);
    }


    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {
        photoEditor.addText(typeface,text,color);
    }

//
//    public class GetImageFromUrl extends AsyncTask<String,Void,Bitmap> {
//
//        Bitmap bitmap;
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            String urlDisplay = strings[0];
//            bitmap = null;
//            try {
//                InputStream srt = new java.net.URL(urlDisplay).openStream();
//                bitmap = BitmapFactory.decodeStream(srt);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//          loadImage(bitmap);
//        }
//    }
}

