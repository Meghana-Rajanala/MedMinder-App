package com.example.myapplication.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class PlaceList {

    @SerializedName("next_page_token")
    String nextPageToken;

    @SerializedName("results")
    public ArrayList<SinglePlace> places;

    public PlaceList(String nextPageToken, ArrayList<SinglePlace> places) {
        this.nextPageToken = nextPageToken;
        this.places = places;
    }

    public PlaceList(){}

}
