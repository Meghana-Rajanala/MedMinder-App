package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;


public class Photo {
    int height, width;

    @SerializedName("photo_reference")
    String photoReference;

    public Photo() {
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
