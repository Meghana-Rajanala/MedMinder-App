package com.example.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.adapter.DateCalculations;
import com.example.myapplication.database.AlarmDatabase;
import com.example.myapplication.database.MedicineDatabase;
import com.example.myapplication.models.AlarmModel;
import com.example.myapplication.models.MedicineModel;
import com.example.myapplication.recicver.AlarmReceiver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SetAlarm extends AppCompatActivity {
    int selectedposition;
    public SharedPreferences sharedPreferences;
    LinearLayout firstSlotLayout,specificDayWeek,DayIntervals;
    TextView medNameET,medTime,firstSlotTV,startDateTV;
    int id, numberOfSlot, noOfDays, daysInterval;
    EditText DayET,etDaysInterval;
    Spinner medCondtion;
    Calendar myCalender;
    String formattedTime;
    ImageView plusIV,mynasIV;
    CheckBox cbSunday,cbMonday,cbTuesday,cbWednesday,cbThursday,cbFriday,cbSaturday;

    String  medName,firstSlotTime, startDate, daysNameOfWeek, status, calculatedDate,
            newStartDate, medicineMeal, finalDate;
    boolean isEveryday, isSpecificDaysOfWeek;
    RadioButton  everydayRB,specificDayRB,daysIntervalRB,beforeMealRB,afterMealRB;
    boolean mon,sun,tue,wed,thu,fri,sat;
    boolean allPermission;
    MedicineDatabase dbHelper;
    String tableName = "";
    int requestCode = 1;
    int flag = 0;
    int uniqueCode = 0;
    int firstRequestCode;

    Button nextPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        sharedPreferences = this.getSharedPreferences("alarmRequestCode", MODE_PRIVATE);
        requestCode = sharedPreferences.getInt("requestCodeValue", 1);
        flag = sharedPreferences.getInt("flagValue", 0);
        uniqueCode = sharedPreferences.getInt("uniqueCodeLastValue", 0);
        medNameET=findViewById(R.id.med_text);
        medTime=findViewById(R.id.med_time_text);
        medNameET.setText(ResultActivity.getValue());
        medCondtion=findViewById(R.id.no_of_times_SP);
        nextPage=findViewById(R.id.set_BTN);
        firstSlotTV=findViewById(R.id.first_slot_TV);
        firstSlotLayout=findViewById(R.id.first_slot_LAYOUT);
        beforeMealRB=findViewById(R.id.before_meal_RB);
        afterMealRB=findViewById(R.id.after_meal_RB);
        everydayRB=findViewById(R.id.everyday_RB);
        specificDayRB=findViewById(R.id.specific_day_RB);
        DayET=findViewById(R.id.no_of_days_ET);
        startDateTV=findViewById(R.id.start_date_TV);
        cbSunday=findViewById(R.id.cb_sunday);
        cbMonday=findViewById(R.id.cb_monday);
        cbTuesday=findViewById(R.id.cb_tuesday);
        cbWednesday=findViewById(R.id.cb_wednesday);
        cbThursday=findViewById(R.id.cb_thursday);
        cbFriday=findViewById(R.id.cb_friday);
        cbSaturday=findViewById(R.id.cb_saturday);
        specificDayWeek=findViewById(R.id.cv_specific_day_of_week);
        everydayRB.setChecked(true);
        beforeMealRB.setChecked(true);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            adapter.addAll(ResultActivity.getMedCondtion());
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medCondtion.setAdapter(adapter);

        medCondtion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    String selectedCondition = (String) parent.getItemAtPosition(position);
                    selectedposition = (ResultActivity.getMedCondtion()).indexOf(selectedCondition);
                    String med_time_taken = (ResultActivity.getMedTime()).get(selectedposition);
                    medTime.setText(med_time_taken);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        firstSlotLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showHourPicker("Select first slot", 1);

            }
        });

        everydayRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    specificDayWeek.setVisibility(View.GONE);

                }
            }
        });


        specificDayRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    specificDayWeek.setVisibility(View.VISIBLE);
                }

            }
        });


        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePicker();
            }
        });



        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT >= 23 && !allPermission) {


                    checkMultiplePermissions();

                } else {


                    mustExecute();

                }


            }
        });
    }

    private String getMedicineMeal() {

        String meal = "";

        if (beforeMealRB.isChecked()) {

            meal = "Before Meal";

        } else if (afterMealRB.isChecked()) {

            meal = "After Meal";
        }

        return meal;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            TextView tv = (TextView) getActivity().findViewById(R.id.start_date_TV);


            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

            DateFormat df_medium_uk = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String df_medium_uk_str = df_medium_uk.format(chosenDate);
            tv.setText(df_medium_uk_str);


        }
    }

    private void mustExecute() {

        if (checkValidity() && checkSpecificDayValidity()) {


            if (beforeMealRB.isChecked()) {

                tableName = "before_table";

            } else if (afterMealRB.isChecked()) {

                tableName = "after_table";
            }


            if (everydayRB.isChecked()) {


                String numberOfDays = DayET.getText().toString();
                DateCalculations dc = new DateCalculations();

                id = 0;
                medName = medNameET.getText().toString();

                noOfDays = Integer.parseInt(numberOfDays);
                isEveryday = true;
                isSpecificDaysOfWeek = false;

                daysNameOfWeek = "null";

                startDate = startDateTV.getText().toString();
                newStartDate = startDate;
                status = "not_taken";
                medicineMeal = getMedicineMeal();


                dbHelper = new MedicineDatabase(this);

                getSlotTime();
                calculatedDate = newStartDate;

                for (int i = 0; i < noOfDays; i++) {


                    MedicineModel medicineModel = new MedicineModel();
                    medicineModel.setId(id);
                    medicineModel.setDate(calculatedDate);
                    medicineModel.setMedicineName(medName);
                    medicineModel.setNumberOfSlot(numberOfSlot);
                    medicineModel.setFirstSlotTime(firstSlotTime);
                    medicineModel.setNumberOfDays(noOfDays);
                    medicineModel.setEveryday(isEveryday);
                    medicineModel.setSpecificDaysOfWeek(isSpecificDaysOfWeek);
                    medicineModel.setDaysNameOfWeek(daysNameOfWeek);
                    medicineModel.setStartDate(startDate);
                    medicineModel.setStatus(status);
                    medicineModel.setMedicineMeal(medicineMeal);
                    medicineModel.setUniqueCode(uniqueCode);

                    setAlarm(calculatedDate, firstSlotTime);

                    dbHelper.insertData(medicineModel, tableName);


                    uniqueCode++;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("uniqueCodeLastValue", uniqueCode);
                    editor.commit();


                    calculatedDate = dc.addDays(newStartDate, "1");
                    newStartDate = calculatedDate;


                }


            } else if (specificDayRB.isChecked()) {

                String numberOfDays = DayET.getText().toString();
                DateCalculations dc = new DateCalculations();

                id = 0;
                medName = medNameET.getText().toString();
                noOfDays = Integer.parseInt(numberOfDays);
                isEveryday = false;
                isSpecificDaysOfWeek = true;
                daysNameOfWeek = getDaysNameOfWeek();
                startDate = startDateTV.getText().toString();
                newStartDate = startDate;
                status = "not_taken";
                medicineMeal = getMedicineMeal();




                dbHelper = new MedicineDatabase(this);

                getSlotTime();
                finalDate = startDate;


                for (int i = 0; i < noOfDays; i++) {


                    calculatedDate = dc.addDays(newStartDate, "1");
                    String singleDayName = dc.daysNameOfWeek(calculatedDate);

                    if (daysNameOfWeek.contains(singleDayName)) {

                        MedicineModel medicineModel = new MedicineModel();
                        medicineModel.setId(id);
                        medicineModel.setDate(finalDate);
                        medicineModel.setMedicineName(medName);

                        medicineModel.setNumberOfSlot(numberOfSlot);
                        medicineModel.setFirstSlotTime(firstSlotTime);
                        medicineModel.setNumberOfDays(noOfDays);
                        medicineModel.setEveryday(isEveryday);
                        medicineModel.setSpecificDaysOfWeek(isSpecificDaysOfWeek);

                        medicineModel.setDaysNameOfWeek(daysNameOfWeek);

                        medicineModel.setStartDate(startDate);
                        medicineModel.setStatus(status);
                        medicineModel.setMedicineMeal(medicineMeal);
                        medicineModel.setUniqueCode(uniqueCode);


                        setAlarm(finalDate, firstSlotTime);
                        dbHelper.insertData(medicineModel, tableName);


                        uniqueCode++;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("uniqueCodeLastValue", uniqueCode);
                        editor.commit();

                        finalDate = calculatedDate;
                        newStartDate = calculatedDate;


                    } else {

                        newStartDate = calculatedDate;
                        i--;


                    }


                }


            }


            Toast.makeText(this, "Successfully added a medicine", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AlarmView.class).putExtra("open", "medicine"));
            finish();

        } else {

            // Toast.makeText(getContext(), "Please check all fields has been filled up", Toast.LENGTH_SHORT).show();
        }


    }

    private void setAlarm(String calculatedDate, String firstSlotTime) {


        String combine = calculatedDate + " " + firstSlotTime;
        setFinalAlarm(combine, 1);
        createAlarmModelObject();
    }
    private void setFinalAlarm(String combine, int value) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aaa");

        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(combine));

            int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
            int monthForAlarm = calendar.get(Calendar.MONTH);
            int yearForAlarm = calendar.get(Calendar.YEAR);
            int hourForAlarm = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteForAlarm = calendar.get(Calendar.MINUTE);
            int secondForAlarm = 0;

            cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);


        } catch (ParseException e) {
            e.printStackTrace();
        }


        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("medName", medName);
        intent.putExtra("mealStatus", medicineMeal);
        intent.putExtra("time", combine);
        intent.putExtra("med", "true");




        if (!firstSlotTime.equals("null") && value == 1) {


            firstRequestCode = requestCode;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, firstRequestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            requestCode++;

        } else if (firstSlotTime.equals("null") && value == 1){

            firstRequestCode = -1;
            Log.d("entered", "entered 1");
        }



    }

    private void createAlarmModelObject() {


        AlarmModel alarmModel = new AlarmModel();
        alarmModel.setId(0);
        alarmModel.setNdt(medName + uniqueCode);
        alarmModel.setNumberOfSlot(numberOfSlot);
        alarmModel.setFirstSlotTime(firstSlotTime);

        alarmModel.setFirstSlotRequestCode(firstRequestCode);


        AlarmDatabase alarmDatabase = new AlarmDatabase(this);
        alarmDatabase.insertAlarn(alarmModel);


        Log.d("firstRequest: ", ""+firstRequestCode);


        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("requestCodeValue", requestCode);
        editor.commit();



    }

    private void getSlotTime() {

        firstSlotTime = firstSlotTV.getText().toString();
    }

    private boolean checkValidity() {


        if (medNameET.getText().toString().equals("")) {

            medNameET.setError("Enter a medicine name");
            Toast.makeText(this, "Please enter a valid medicine name", Toast.LENGTH_SHORT).show();
            return false;


        } else if ((firstSlotTV.getText().toString().contains("Set"))) {

            if (firstSlotTV.getText().toString().contains("Set")) {

                Toast.makeText(this, "Please enter a valid time in slot 1", Toast.LENGTH_SHORT).show();

            }

            return false;


        } else if (DayET.getText().toString().equals("")) {


            DayET.setError("this field is required");
            Toast.makeText(this, "Number of days cannot be empty", Toast.LENGTH_SHORT).show();
            return false;


        } else if (startDateTV.getText().toString().contains("Touch here to set date")) {

            Toast.makeText(this, "Please enter a valid date", Toast.LENGTH_SHORT).show();

            return false;

        }else {

            return true;
        }


    }
    public void showDatePicker() {


        DialogFragment dFragment = new DatePickerFragment();
        dFragment.show(this.getFragmentManager(), "Date Picker");

    }
    public void showHourPicker(String message, final int number) {


        myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);


                    try {
                        SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a");

                        String strTime = myCalender.get(Calendar.HOUR_OF_DAY) + ":" + myCalender.get(Calendar.MINUTE);
                        Date time = sdf24.parse(strTime);
                        formattedTime = sdf12.format(time);
                        firstSlotTV.setText(formattedTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                myTimeListener,
                hour,
                minute,
                false);

        try {

            timePickerDialog.setTitle(message);
            timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            timePickerDialog.show();

        } catch (Exception e) {

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public String getDaysNameOfWeek() {

        String daysName = "";

        if (cbSaturday.isChecked()) {

            daysName = daysName + "Saturday, ";
            sat = true;

        }
        if (cbSunday.isChecked()) {

            daysName = daysName + "Sunday, ";
            sun = true;

        }
        if (cbMonday.isChecked()) {

            daysName = daysName + "Monday, ";
            mon = true;

        }
        if (cbTuesday.isChecked()) {

            daysName = daysName + "Tuesday, ";
            tue = true;

        }
        if (cbWednesday.isChecked()) {

            daysName = daysName + "Wednesday, ";
            wed = true;

        }
        if (cbThursday.isChecked()) {

            daysName = daysName + "Thursday, ";
            thu = true;

        }
        if (cbFriday.isChecked()) {

            daysName = daysName + "Friday, ";
            fri = true;

        }


        return daysName.substring(0, daysName.length() - 2);
    }
    public boolean checkSpecificDayValidity() {

        if (specificDayWeek.getVisibility() == View.VISIBLE && cbSaturday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.VISIBLE && cbSunday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.VISIBLE && cbMonday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.VISIBLE && cbTuesday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.VISIBLE && cbWednesday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.VISIBLE && cbThursday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.VISIBLE && cbFriday.isChecked()) {

            return true;

        } else if (specificDayWeek.getVisibility() == View.GONE) {

            return true;

        } else {

            Toast.makeText(this, "Please select at least one specific day in week\nOr choose Everyday", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, android.Manifest.permission.CAMERA)) {
                permissionsNeeded.add("Camera");
            }

            if (!addPermission(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Storage");
            }


            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

            if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // All Permissions Granted

                allPermission = true;

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

            if (this.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

}