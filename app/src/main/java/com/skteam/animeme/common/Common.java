package com.skteam.animeme.common;


import com.skteam.animeme.models.Image;
import com.skteam.animeme.retrofit.AnimemeApi;
import com.skteam.animeme.retrofit.RetrofitClient;

import java.util.List;

public class Common {
    public static List<Image> imageList;
    public static int imageListPosition;
    public static  boolean isMemeSelected = false;
    public static  boolean isTemplateSelected = false;
    public static  boolean isShare = false;
    public static  boolean isShareWhatsapp = false;
    public static  boolean isShareFb = false;
    public static  boolean isShareInstagram = false;


    private static final String BASE_URL = "https://slouching-steeple.000webhostapp.com/Animeme/";
    public static final String IMAGES_BASE_URL = "https://slouching-steeple.000webhostapp.com/Animeme/Images/";

    public static AnimemeApi getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(AnimemeApi.class);
    }
}
