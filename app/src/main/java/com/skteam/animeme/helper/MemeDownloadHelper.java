package com.skteam.animeme.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.skteam.animeme.BuildConfig;
import com.skteam.animeme.ViewImageActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static com.skteam.animeme.common.Common.isShare;
import static com.skteam.animeme.common.Common.isShareFb;
import static com.skteam.animeme.common.Common.isShareInstagram;
import static com.skteam.animeme.common.Common.isShareWhatsapp;

public class MemeDownloadHelper {
   Context context;
   String title;
   int id;
    String image_path;
    ImageView imageView;
    ProgressDialog pd;

    public MemeDownloadHelper(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
        pd= new ProgressDialog(context);
        pd.setTitle("Sharing Image");
        pd.setMessage("please wait...");
        pd.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        },5000);
    }



    public MemeDownloadHelper(Context context, String title, int id) {
        this.context = context;
        this.title = title;
        this.id = id;
    }

    public class DownloadTask extends AsyncTask<String,Integer,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Download in progress...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String path = strings[0];
            int file_length = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                file_length = urlConnection.getContentLength();
                File new_folder = new File("sdcard/Animeme");
                if (!new_folder.exists()){
                    new_folder.mkdir();
                }
                 image_path = new_folder + "/" + "Meme_" + title + "_" + id + ".jpg";

                File input_file = new File(image_path);
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;

                OutputStream outputStream = new FileOutputStream(input_file);
                while ((count= inputStream.read(data)) != -1){
                    total+=count;
                    outputStream.write(data,0,count);
                    int progress = (int) total*100/file_length;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download Completed";
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();


                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                //Image path
                Log.d("DOWNLOADEDIMAGE", "onPostExecute: "+image_path);




        }
    }



    private void shareContent(String path) {

        Uri imageUri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "Sharing via Animeme app");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "send"));
        if (isShareWhatsapp){

            shareIntent.setPackage("com.whatsapp");
            context.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();

        } else if (isShareFb){
            shareIntent.setPackage("com.facebook.katana");
            context.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();
        } else if (isShareInstagram){
            shareIntent.setPackage("com.instagram.android");
            context.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();
        } else if (isShare){
            context.startActivity(Intent.createChooser(shareIntent, "send"));
            flaseAllValue();
        }

    }

    private void flaseAllValue() {

        isShareWhatsapp = false;
        isShareInstagram = false;
        isShare = false;
        isShareFb = false;
    }

    public void shareImage() {
        Bitmap bitmap = getBitMapFromView(imageView);

        try {
            File file = new File(context.getExternalCacheDir(),"image.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true,false);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, "Sharing via Animeme app");
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",file));
            intent.setType("image/*");
            if (isShareWhatsapp){

                intent.setPackage("com.whatsapp");
                context.startActivity(Intent.createChooser(intent, "send"));
                flaseAllValue();

            } else if (isShareFb){
                intent.setPackage("com.facebook.katana");
                context.startActivity(Intent.createChooser(intent, "send"));
                flaseAllValue();
            } else if (isShareInstagram){
                intent.setPackage("com.instagram.android");
                context.startActivity(Intent.createChooser(intent, "send"));
                flaseAllValue();
            } else if (isShare){
                context.startActivity(Intent.createChooser(intent, "send"));
                flaseAllValue();
            }


        }catch (Exception e){
            e.printStackTrace();
        }

}

    private Bitmap getBitMapFromView(View view) {

        Bitmap returnBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null){
            bgDrawable.draw(canvas);
        }else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return returnBitmap;
    }
}
