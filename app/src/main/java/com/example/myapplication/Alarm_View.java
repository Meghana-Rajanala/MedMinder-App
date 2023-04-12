package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.DateCalculations;
import com.example.myapplication.adapter.MedicineAdapter;
import com.example.myapplication.database.MedicineDatabase;
import com.example.myapplication.models.MedicineModel;
import com.example.myapplication.utils.HorizontalCalendarListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class Alarm_View extends Fragment {

    View view;

    FloatingActionButton fab;

    //InterstitialAd mInterstitialAd;

    boolean allPermission;
    RecyclerView recyclerViewBeforeMeal, recyclerViewAfterMeal;
    List<MedicineModel> medicineModelListBeforeMeal, medicineModelListAfterMeal;
    MedicineAdapter adapter;
    MedicineDatabase dbHelper;

    Calendar startDate;
    Calendar endDate;

    TextView beforeMessageTV, afterMessageTV;


    HorizontalCalendar horizontalCalendar;
    ImageView leftIV, rightIV;
    TextView dateTV, beforeTV, afterTV;
    boolean firstStart = true;
    int position = 5;

        Date currentDate;

    public Alarm_View() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_alarm__view, container, false);

        if (!allPermission){

            if (Build.VERSION.SDK_INT >= 23) {
                checkMultiplePermissions();
            }
        }


        setupCalender();
        init();




        return view;
    }



    private void init() {

        currentDate = Calendar.getInstance().getTime();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        String searchQuery = df.format(currentDate);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_add);

        leftIV = (ImageView) view.findViewById(R.id.leftIV);
        rightIV = (ImageView) view.findViewById(R.id.rightIV);
        dateTV = (TextView) view.findViewById(R.id.dateTV);
        beforeTV = (TextView) view.findViewById(R.id.beforeTV);
        afterTV = (TextView) view.findViewById(R.id.afterTV);

        beforeMessageTV = (TextView) view.findViewById(R.id.before_messageTV);
        afterMessageTV = (TextView) view.findViewById(R.id.after_messageTV);


        recyclerViewBeforeMeal = (RecyclerView) view.findViewById(R.id.recyclerView_before_meal);
        recyclerViewAfterMeal = (RecyclerView) view.findViewById(R.id.recyclerView_after_meal);

        RecyclerView.LayoutManager layoutManagerBeforeMeal = new GridLayoutManager(getContext(), 1);
        recyclerViewBeforeMeal.setLayoutManager(layoutManagerBeforeMeal);
        recyclerViewBeforeMeal.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerViewBeforeMeal.setItemAnimator(new DefaultItemAnimator());


        RecyclerView.LayoutManager layoutManagerAfterMeal = new GridLayoutManager(getContext(), 1);
        recyclerViewAfterMeal.setLayoutManager(layoutManagerAfterMeal);
        recyclerViewAfterMeal.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(2), true));
        recyclerViewAfterMeal.setItemAnimator(new DefaultItemAnimator());

        medicineModelListBeforeMeal = new ArrayList<>();
        medicineModelListBeforeMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListBeforeMeal = dbHelper.getSelectedList(searchQuery, "before_table");
        adapter = new MedicineAdapter(getContext(), getActivity(), medicineModelListBeforeMeal,
                "before_table", recyclerViewBeforeMeal , adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewBeforeMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        beforeTV.setText("Before Meal (" + medicineModelListBeforeMeal.size() + ")");



        medicineModelListAfterMeal = new ArrayList<>();
        medicineModelListAfterMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListAfterMeal = dbHelper.getSelectedList(searchQuery, "after_table");
        adapter = new MedicineAdapter(getContext(), getActivity(), medicineModelListAfterMeal,
                "after_table", recyclerViewAfterMeal, adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewAfterMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        afterTV.setText("After Meal ("  + medicineModelListAfterMeal.size() + ")");



        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String formattedCurrentDate = sdf.format(currentDate);

        int sizeBefore = medicineModelListBeforeMeal.size();
        int sizeAfter = medicineModelListAfterMeal.size();
        int sum = sizeBefore + sizeAfter;

        dateTV.setText("Today " +formattedCurrentDate + " (total " + sum + " medicine)");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), alert_medicine.class));
            }
        });



        leftIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setupCurrentDate();

            }
        });

        rightIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setupCurrentDate();

            }
        });


        checkEmptyMedicine();


    }

    @Override
    public void onResume() {
        super.onResume();

        checkEmptyMedicine();
    }



    private void checkEmptyMedicine(){

        if (medicineModelListBeforeMeal.size() == 0){

            recyclerViewBeforeMeal.setVisibility(View.GONE);
            beforeMessageTV.setVisibility(View.VISIBLE);

        }else {

            recyclerViewBeforeMeal.setVisibility(View.VISIBLE);
            beforeMessageTV.setVisibility(View.GONE);
        }

        if (medicineModelListAfterMeal.size() == 0){

            recyclerViewAfterMeal.setVisibility(View.GONE);
            afterMessageTV.setVisibility(View.VISIBLE);

        }else {

            recyclerViewAfterMeal.setVisibility(View.VISIBLE);
            afterMessageTV.setVisibility(View.GONE);
        }
    }

    private void setupCurrentDate() {
        if (horizontalCalendar != null) {
            horizontalCalendar.goToday(true);
        }

       // horizontalCalendar.goToday(true);


/*
horizontalCalendar.selectDate(new Calendar() {
    @Override
    protected void computeTime() {

    }

    @Override
    protected void computeFields() {

    }

    @Override
    public void add(int field, int amount) {

    }

    @Override
    public void roll(int field, boolean up) {

    }

    @Override
    public int getMinimum(int field) {
        return 0;
    }

    @Override
    public int getMaximum(int field) {
        return 0;
    }

    @Override
    public int getGreatestMinimum(int field) {
        return 0;
    }

    @Override
    public int getLeastMaximum(int field) {
        return 0;
    }
}, true);
*/

    }

    private void setupCalender() {
     //  Calendar selectedDate = Calendar.getInstance();
        /* start 2 months ago from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);

        /* end after 2 months from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        // Default Date set to Today.
        final Calendar defaultSelectedDate = Calendar.getInstance();



             /*       HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                            .range(startDate, endDate)
                            .datesNumberOnScreen(5)
                            .configure()
                            .formatTopText("MMM")
                            .formatMiddleText("dd")
                            .formatBottomText("EEE")
                            .showTopText(true)
                            .showBottomText(true)
                            .textColor(Color.LTGRAY, Color.WHITE)
                            .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                            .end()
                            .defaultSelectedDate(Calendar.getInstance())
                .build();


      */  HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate,endDate)
                .datesNumberOnScreen(7)   // Number of Dates cells shown on screen (default to 5).
                .configure()    // starts configuration.
                .formatTopText("MMM")// default to "MMM".
                .formatMiddleText("dd")    // default to "dd".
                .formatBottomText("EEE")    // default to "EEE".
                .showTopText(true)              // show or hide TopText (default to true).
                .showBottomText(false)           // show or hide BottomText (default to true).
                //.textColor(int normalColor, int selectedColor)    // default to (Color.LTGRAY, Color.WHITE).
                //.selectedDateBackground(Drawable background)      // set selected date cell background.
                //.selectorColor(int color)               // set selection indicator bar's color (default to colorAccent).
                .end()          // ends configuration.
                .defaultSelectedDate(Calendar.getInstance())    // Date to be selected at start (default to current day `Calendar.getInstance()`).
                .build();





        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                imageViewIssue(date, position);
                prepareDataForRecyclerView(date);
                setTextViewDate(date);


            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {

            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });

    }

    private void prepareDataForRecyclerView(Calendar date) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
        Date selectedDate = date.getTime();
        String searchQuery = df.format(selectedDate);



        medicineModelListBeforeMeal = new ArrayList<>();
        medicineModelListBeforeMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListBeforeMeal = dbHelper.getSelectedList(searchQuery, "before_table");
        adapter = new MedicineAdapter(getContext(), getActivity(), medicineModelListBeforeMeal,
                "before_table", recyclerViewBeforeMeal, adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewBeforeMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        beforeTV.setText("Before Meal ("  + medicineModelListBeforeMeal.size() + ")");




        medicineModelListAfterMeal = new ArrayList<>();
        medicineModelListAfterMeal.clear();
        dbHelper = new MedicineDatabase(getContext());
        medicineModelListAfterMeal = dbHelper.getSelectedList(searchQuery, "after_table");
        adapter = new MedicineAdapter(getContext(), getActivity(), medicineModelListAfterMeal,
                "after_table", recyclerViewAfterMeal, adapter, searchQuery);
        adapter.notifyDataSetChanged();
        recyclerViewAfterMeal.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        afterTV.setText("After Meal ("  + medicineModelListAfterMeal.size() + ")");

    }

    private void imageViewIssue(Calendar date, int position) {

        Date selectedDate = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        String formattedDate = sdf.format(selectedDate);

        String result = new DateCalculations().compareDate(formattedDate);

        if (result.equals("big")){

            leftIV.setVisibility(View.VISIBLE);
            rightIV.setVisibility(View.GONE);


        }else if (result.equals("small")){

            rightIV.setVisibility(View.VISIBLE);
            leftIV.setVisibility(View.GONE);

        }

    }

    private void setTextViewDate(Calendar date) {

        Date currentDate = Calendar.getInstance().getTime();
        Date myDate = date.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM - yyyy");
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM - yyyy");

        String formattedCurrentDate = sdf.format(currentDate);
        String formattedDate = df.format(myDate);

        if (formattedCurrentDate.equals(formattedDate)){

            int sizeBefore = medicineModelListBeforeMeal.size();
            int sizeAfter = medicineModelListAfterMeal.size();
            int sum = sizeBefore + sizeAfter;

            dateTV.setText("Today " + formattedCurrentDate + " (total " + sum + " medicine)" );

            leftIV.setVisibility(View.GONE);
            rightIV.setVisibility(View.GONE);

        }else {



            String addDate = new DateCalculations().addForHorizontalCalender(formattedCurrentDate, "1");
            String subDate = new DateCalculations().subForHorizontalCalender(formattedCurrentDate, "1");

            if (addDate.equals(formattedDate)){

                int sizeBefore = medicineModelListBeforeMeal.size();
                int sizeAfter = medicineModelListAfterMeal.size();
                int sum = sizeBefore + sizeAfter;

                dateTV.setText("Tomorrow " + formattedDate + " (total " + sum + " medicine)");

            }else if (subDate.equals(formattedDate)){

                int sizeBefore = medicineModelListBeforeMeal.size();
                int sizeAfter = medicineModelListAfterMeal.size();
                int sum = sizeBefore + sizeAfter;

                dateTV.setText("Yesterday " + formattedDate + " (total " + sum + " medicine)");

            }else {

                int sizeBefore = medicineModelListBeforeMeal.size();
                int sizeAfter = medicineModelListAfterMeal.size();
                int sum = sizeBefore + sizeAfter;

                dateTV.setText(formattedDate + " (total " + sum + " medicine)");
            }




        }

    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                permissionsNeeded.add("Camera");
            }

            if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Storage");
            }
            if(!addPermission(permissionsList, Manifest.permission.FOREGROUND_SERVICE)){
                permissionsNeeded.add("Foreground");
            }
            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (requireActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case StaticVariables.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.FOREGROUND_SERVICE,PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && perms.get(Manifest.permission.FOREGROUND_SERVICE)==PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    allPermission = true;

                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                getContext(),
                                "This application cannot run without Camera and Storage " +
                                        "Permissions.\nYou may relaunch the app or allow permissions" +
                                        " from Application Settings",
                                Toast.LENGTH_LONG).show();

                        allPermission = false;


                        if (perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


                        }


                        if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {


                        }
                        if (perms.get(Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {


                        }


                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}