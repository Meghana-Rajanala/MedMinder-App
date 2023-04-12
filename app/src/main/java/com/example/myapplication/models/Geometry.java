package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;


public class Geometry {

    @SerializedName("location")
    Location location;

    public Geometry(Location location) {
        this.location = location;
    }

    public Geometry() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
