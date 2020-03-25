package com.skteam.animeme.models;

import java.util.List;

public class ImageDataResponse {
    private boolean error;
    private List<Image> res;

    public boolean isError() {
        return error;
    }

    public List<Image> getRes() {
        return res;
    }
}


