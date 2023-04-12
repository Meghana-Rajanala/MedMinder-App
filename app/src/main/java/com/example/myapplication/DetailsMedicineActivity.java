package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailsMedicineActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Calendar calendar = Calendar.getInstance();

    TextView medNameTV, dateTimeTV,mealTV, numberOfSlotTV, firstSlotTV, numberOfDaysTV, startDateTV, daysIntervalTV, statusTV,medicineDescription;
    ImageView  optionIV;
    LinearLayout statusLAYOUT;
    SimpleDateFormat inputFormat,dateFormat,timeFormat;
    SendEmail sendMailTask;
    String medicineName,MedicineMeal, dateTime, numberOfSlot, firstSlotTime, numberOfDays, startDate, daysInterval, status, imagePath, type,formattedPhoneNumber;
    Bitmap bitmap;
    String phoneUser,emailUser,id,medName,time,mealStatus,dateStr,timeStr;
    Date date;
   boolean taken;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_medicine);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        sharedPreferences=getSharedPreferences("userInfo", Context.MODE_PRIVATE);
         phoneUser = sharedPreferences.getString("phoneNo", "");
         emailUser = sharedPreferences.getString("email", "");
         formattedPhoneNumber = "+91" + phoneUser;
        inputFormat = new SimpleDateFormat("d MMM yyyy h:mm a");
         dateFormat = new SimpleDateFormat("d MMM yyyy");
         timeFormat = new SimpleDateFormat("h:mm a");
        init();
        receiveIntent();
        setTextAndIMageIntoView();
        setOnClick();
    }

    private void init() {

        medNameTV = (TextView) findViewById(R.id.medicine_name_TV_DA);
        dateTimeTV = (TextView) findViewById(R.id.date_and_time_TV_DA);
      //  numberOfSlotTV = (TextView) findViewById(R.id.number_of_slot_TV_DA);
        firstSlotTV = (TextView) findViewById(R.id.first_slot_TV_DA);
        numberOfDaysTV = (TextView) findViewById(R.id.number_of_days_TV_DA);
        startDateTV = (TextView) findViewById(R.id.start_date_TV_DA);
        daysIntervalTV = (TextView) findViewById(R.id.days_interval_TV_DA);
      //  statusTV = (TextView) findViewById(R.id.status_TV_DA);
        optionIV = (ImageView) findViewById(R.id.option_IV_DA);
        statusLAYOUT = (LinearLayout) findViewById(R.id.status_LAYOUT_DA);
        mealTV=(TextView) findViewById(R.id.mealTV);
    }

    private void receiveIntent() {
        Intent intent = getIntent();
        medicineName = intent.getStringExtra("medName");
        dateTime = intent.getStringExtra("dateTime");
        numberOfSlot = intent.getStringExtra("numberOfSlot");
        firstSlotTime = intent.getStringExtra("firstSlotTime");
        numberOfDays = intent.getStringExtra("numberOfDays");
        startDate = intent.getStringExtra("startDate");
        daysInterval = intent.getStringExtra("daysInterval");
        status = intent.getStringExtra("status");
        type = intent.getStringExtra("type");
        MedicineMeal=intent.getStringExtra("medicineMeal");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setTextAndIMageIntoView() {
        medNameTV.setText(medicineName);
        dateTimeTV.setText(dateTime);
       // numberOfSlotTV.setText(numberOfSlot);
        numberOfDaysTV.setText(numberOfDays);
        firstSlotTV.setText(firstSlotTime);
        startDateTV.setText(startDate);
        daysIntervalTV.setText(daysInterval);
       // statusTV.setText(status);// update the status text view
        mealTV.setText(MedicineMeal);
    }

    public void setOnClick() {

        optionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(DetailsMedicineActivity.this, optionIV);
                popupMenu.getMenuInflater().inflate(R.menu.sms_email_option, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_sms:
                                            // Check if permission is not granted
                                if (ContextCompat.checkSelfPermission(DetailsMedicineActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                                    // Permission is not granted, request the permission
                                    ActivityCompat.requestPermissions(DetailsMedicineActivity.this, new String[]{Manifest.permission.SEND_SMS},1);
                                }
                                int numberOfDays1 = Integer.parseInt(numberOfDaysTV.getText().toString());
                                Calendar now = Calendar.getInstance();
                                String firstSlotTime2 = firstSlotTime; // Replace with the actual first slot time
                                String[] timeParts = firstSlotTime2.split(":");
                                int hourOfDay = Integer.parseInt(timeParts[0]);
                                String minuteStr = timeParts[1];
                                String[] minuteParts = minuteStr.split("\\s+");
                                int minute = Integer.parseInt(minuteParts[0]);
                                String amPm = minuteParts[1];
                                if (amPm.equalsIgnoreCase("pm") && hourOfDay != 12) {
                                    hourOfDay += 12;
                                } else if (amPm.equalsIgnoreCase("am") && hourOfDay == 12) {
                                    hourOfDay = 0;
                                }
                                Calendar firstSlotTime1 = Calendar.getInstance();
                                firstSlotTime1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                firstSlotTime1.set(Calendar.MINUTE, minute);
                                long timeUntilFirstSlot = firstSlotTime1.getTimeInMillis() - now.getTimeInMillis();
                             /*   new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        sendSms(formattedPhoneNumber);
                                    }
                                }, timeUntilFirstSlot);*/
                                for (int i = 0; i < numberOfDays1; i++) {
                                    // Calculate the delay for the next day
                                    long delay = (i * 24 * 60 * 60 * 1000) + timeUntilFirstSlot;

                                    // Delay the message using a Handler
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sendSms(formattedPhoneNumber);
                                            // Display toast message
                                            Toast.makeText(getApplicationContext(), "SMS reminder sent", Toast.LENGTH_SHORT).show();

                                        }
                                    }, delay);
                                }
                                return true;
                            case R.id.action_email:
                                // Get the number of days to schedule the reminders
                                int numberOfDays = Integer.parseInt(numberOfDaysTV.getText().toString());
                                Calendar now1 = Calendar.getInstance();
                                String firstSlotTime4 = firstSlotTime; // Replace with the actual first slot time
                                String[] timeParts1 = firstSlotTime4.split(":");
                                int hourOfDay1 = Integer.parseInt(timeParts1[0]);
                                String minuteStr1 = timeParts1[1];
                                String[] minuteParts1 = minuteStr1.split("\\s+");
                                int minute1 = Integer.parseInt(minuteParts1[0]);
                                String amPm1 = minuteParts1[1];
                                if (amPm1.equalsIgnoreCase("pm") && hourOfDay1 != 12) {
                                    hourOfDay1 += 12;
                                } else if (amPm1.equalsIgnoreCase("am") && hourOfDay1 == 12) {
                                    hourOfDay1 = 0;
                                }
                                Calendar firstSlotTime3 = Calendar.getInstance();
                                firstSlotTime3.set(Calendar.HOUR_OF_DAY, hourOfDay1);
                                firstSlotTime3.set(Calendar.MINUTE, minute1);
                                long timeUntilFirstSlot1 = firstSlotTime3.getTimeInMillis() - now1.getTimeInMillis();

                                for (int i = 0; i < numberOfDays; i++) {
                                    // Calculate the delay for the next day
                                    long delay = (i * 24 * 60 * 60 * 1000) + timeUntilFirstSlot1;

                                    // Delay the message using a Handler
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            String recipient =emailUser;
                                            String subject = "Medication Reminder";
                                            String body = "it's  time  to  take  " +medicineName +" at  "+firstSlotTime+"  on  " + dateTime + "  and  "+ status + " ( " + MedicineMeal + " )";
                                            SendEmail email = new SendEmail(recipient, subject, body);
                                            email.execute();

                                            // Display toast message
                                            Toast.makeText(getApplicationContext(), "Email reminder sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }, delay);
                                }
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                popupMenu.show();
            }
        });
        //medminderappproject
        //MedMinder@fourth
    }
    private void sendSms(String phoneNumber) {
        SmsManager smsManager = SmsManager.getDefault();
        String message="it's  time  to  take  " +medicineName +" at  "+firstSlotTime+"  on  " + dateTime + "  and  "+ status + " ( " + MedicineMeal + " )";
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
