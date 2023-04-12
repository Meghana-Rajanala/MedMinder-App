package com.example.myapplication;


import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.SinglePlace;
import com.example.myapplication.rest_api.GooglePlacesApi;
import com.example.myapplication.rest_api.HospitalListClient;

import java.util.ArrayList;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {

    RecyclerView recyclerHospital;
    ArrayList<SinglePlace> itemList;

    FrameLayout fader, listFrame;

    TextView tvDisplayResult;

    GooglePlacesApi googlePlacesApi;
    HospitalListClient hospitalListClient;

    public static final String TAG = "list";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");


        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        recyclerHospital = (RecyclerView) findViewById(R.id.recyclerHospital);
        recyclerHospital.setLayoutManager(new LinearLayoutManager(this));

        fader = (FrameLayout) findViewById(R.id.fader);
        listFrame = (FrameLayout) findViewById(R.id.content_main);

        tvDisplayResult = findViewById(R.id.tvDisplayResult);

       

      

    /*

        stopLoadingAnimation();
        tvDisplayResult.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
//            Log.d(TAG, "onCreate: search started");

            setLoadingAnimation();
            String query = intent.getStringExtra(SearchManager.QUERY);

            toolbar.setTitle("Search results for '"+query+"'");

            googlePlacesApi = new GooglePlacesApi(getApplicationContext(),"AIzaSyC6iuypedDboEsFIECkR_1DjgRJc6xrx-I");
            hospitalListClient = googlePlacesApi.getHospitalListClient();

            HashMap<String, String > params = googlePlacesApi.getQueryParams(nearest_hospital.curLocation, GooglePlacesApi.TYPE_HOSPITAL, GooglePlacesApi.RANKBY_PROMINENCE);
            params.put("radius","50000");
            params.put("name", query);

            hospitalListClient.getNearbyHospitals(params).enqueue(new Callback<PlaceList>() {
                @Override
                public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
//                    Log.d(TAG, "onResponse: resp received");
                    PlaceList placeList = response.body();

                    if(placeList != null){
                        stopLoadingAnimation();
                        itemList = placeList.places;
                        if(itemList.size() == 0)
                            tvDisplayResult.setVisibility(View.VISIBLE);
                        else
                            bindRecyclerView();

                    }

                }

                @Override
                public void onFailure(Call<PlaceList> call, Throwable t) {
//                    Log.d(TAG, "onFailure: cannot access places api");
                    Toast.makeText(getApplicationContext(),"Unable to access server. Please try again later",Toast.LENGTH_SHORT).show();
                    tvDisplayResult.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            itemList = Parcels.unwrap(intent.getParcelableExtra("itemList"));
            bindRecyclerView();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    void bindRecyclerView(){
        HospitalListRecycler hospitalListRecycler = new HospitalListRecycler(itemList,this);
        recyclerHospital.setAdapter(hospitalListRecycler);
    }

    void setLoadingAnimation(){
        enableDisableView(listFrame, false);
        tvDisplayResult.setVisibility(View.INVISIBLE);
        fader.setVisibility(View.VISIBLE);

    }

    void stopLoadingAnimation(){
        enableDisableView(listFrame, true);
        fader.setVisibility(View.GONE);
*/
    }

}
