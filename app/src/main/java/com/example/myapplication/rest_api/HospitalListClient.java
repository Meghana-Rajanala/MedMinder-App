package com.example.myapplication.rest_api;

import com.example.myapplication.models.DetailResult;
import com.example.myapplication.models.DistanceResult;
import com.example.myapplication.models.PlaceList;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;


public interface HospitalListClient {

    @GET("place/nearbysearch/json")
    Call<PlaceList> getNearbyHospitals (@QueryMap Map<String,String> params);

    @GET("distancematrix/json")
    Call<DistanceResult> getHospitalDistances(@QueryMap Map<String,String> params);

    @GET("place/details/json")
    Call<DetailResult> getHospitalDetails(@QueryMap Map<String ,String> params);
}
