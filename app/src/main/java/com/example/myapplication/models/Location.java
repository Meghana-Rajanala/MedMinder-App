package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;


public class Location{
    @SerializedName("lat")
    Double latitude;

    @SerializedName("lng")
    Double longitude;

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
    }
}
