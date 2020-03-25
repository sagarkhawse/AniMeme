package com.skteam.animeme.models;

import com.skteam.animeme.common.Common;

public class Image {
    private String image;
    private int id;
    private String title;
    private String category;
    private String type;
    private String banner;
    private String interstitial;

    public String getImage() {
        return Common.IMAGES_BASE_URL + image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getBanner() {
        return banner;
    }

    public String getInterstitial() {
        return interstitial;
    }
}
