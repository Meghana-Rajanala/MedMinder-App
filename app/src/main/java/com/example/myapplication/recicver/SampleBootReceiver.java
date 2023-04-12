package com.example.myapplication.recicver;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.myapplication.database.MedicineDatabase;
import com.example.myapplication.models.MedicineModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SampleBootReceiver extends BroadcastReceiver {

    int requestCode = 0;
    public SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            sharedPreferences = context.getSharedPreferences("alarmRequestCode", MODE_PRIVATE);

            List<MedicineModel> beforeList = new ArrayList<>();
            List<MedicineModel> afterList = new ArrayList<>();
            MedicineDatabase dbHelper = new MedicineDatabase(context);


            beforeList = dbHelper.getAllData("before_table");
            afterList = dbHelper.getAllData("after_table");

            for (int i = 0; i < beforeList.size(); i++) {

                MedicineModel medicineModel = beforeList.get(i);
                String date = medicineModel.getDate();
                String specificdayofweek = medicineModel.getDaysNameOfWeek();
                boolean everyday = medicineModel.isEveryday();
                boolean specificday = medicineModel.isSpecificDaysOfWeek();
                int numDays = medicineModel.getNumberOfDays();
                String firstSlotTime = medicineModel.getFirstSlotTime();
                String combine = date + " " + firstSlotTime;
                setAlarm(combine, context, medicineModel, numDays);

            }


            for (int i = 0; i < afterList.size(); i++) {

                MedicineModel medicineModel = afterList.get(i);
                String date = medicineModel.getDate();
                int numDays = medicineModel.getNumberOfDays();
                String specificdayofweek = medicineModel.getDaysNameOfWeek();
                boolean everyday = medicineModel.isEveryday();
                boolean specificday = medicineModel.isSpecificDaysOfWeek();
                String firstSlotTime = medicineModel.getFirstSlotTime();
                String combine = date + " " + firstSlotTime;
                setAlarm(combine, context, medicineModel, numDays);
            }

        }

    }


    private void setAlarm(String combine, Context context, MedicineModel medicineModel, int numDays) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm a");
        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(combine));

            int dateForAlarm = calendar.get(Calendar.DAY_OF_MONTH);
            int monthForAlarm = calendar.get(Calendar.MONTH);
            int yearForAlarm = calendar.get(Calendar.YEAR);
            int hourForAlarm = calendar.get(Calendar.HOUR);

            int minuteForAlarm = calendar.get(Calendar.MINUTE);
            int secondForAlarm = 0;
            int am_pm = calendar.get(Calendar.AM_PM);
            if (am_pm == Calendar.PM) {
                hourForAlarm += 12;
            }
            cal.set(yearForAlarm, monthForAlarm, dateForAlarm, hourForAlarm, minuteForAlarm, secondForAlarm);

            Log.d("calender",
                    "calender" + "\n" +
                            "Date " + dateForAlarm + "\n" +
                            "Month " + monthForAlarm + "\n" +
                            "Year " + yearForAlarm + "\n" +
                            "Hour: " + hourForAlarm + "\n" +
                            "Minute " + minuteForAlarm + "\n" +
                            "TimeMills " + cal.getTimeInMillis() + "\n" +
                            "Current TimeMills " + Calendar.getInstance().getTimeInMillis() + "\n" +
                            "Combine " + combine);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Set the alarm only for the first slot time
        if (medicineModel.getFirstSlotTime().equals(combine.split(" ")[1])) {

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("medName", medicineModel.getMedicineName());
            intent.putExtra("mealStatus", medicineModel.getMedicineMeal());
            intent.putExtra("time", combine);
            intent.putExtra("status", medicineModel.getStatus());
            intent.putExtra("med", "true");
            intent.putExtra("medId", medicineModel.getId());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, medicineModel.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

            if (medicineModel.isEveryday()) {
                // Set the alarm for the next numDays days
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                Log.d("SampleBootReceiver", "Alarm set for " + medicineModel.getMedicineName() + " with id " + medicineModel.getId() + " for " + numDays + " days");

                // Increment the date by 1 day for the next alarm
                cal.add(Calendar.DATE, 1);

                // Set the remaining alarms for the next numDays-1 days
                for (int i = 1; i < numDays; i++) {
                    Intent nextIntent = new Intent(context, AlarmReceiver.class);
                    nextIntent.putExtra("medName", medicineModel.getMedicineName());
                    nextIntent.putExtra("mealStatus", medicineModel.getMedicineMeal());
                    nextIntent.putExtra("time", combine);
                    nextIntent.putExtra("status", medicineModel.getStatus());
                    nextIntent.putExtra("med", "true");
                    nextIntent.putExtra("medId", medicineModel.getId());
                    PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, medicineModel.getId() + i, nextIntent, PendingIntent.FLAG_IMMUTABLE);

                    cal.add(Calendar.DATE, 1);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, nextPendingIntent);

                    requestCode++;

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("requestCodeValue", requestCode);
                    editor.commit();

                    // Increment the date by 1 day for the next alarm
                    cal.add(Calendar.DATE, 1);
                }
            } else if (medicineModel.isSpecificDaysOfWeek()) {

                // Set the alarm only if it's the correct day of the week
                String[] daysOfWeek = medicineModel.getDaysNameOfWeek().split(",");
                int[] specificDaysOfWeek = new int[daysOfWeek.length];

                for (int i = 0; i < daysOfWeek.length; i++) {
                    switch (daysOfWeek[i]) {
                        case "Sunday":
                            specificDaysOfWeek[i] = Calendar.SUNDAY;
                            break;
                        case "Monday":
                            specificDaysOfWeek[i] = Calendar.MONDAY;
                            break;
                        case "Tuesday":
                            specificDaysOfWeek[i] = Calendar.TUESDAY;
                            break;
                        case "Wednesday":
                            specificDaysOfWeek[i] = Calendar.WEDNESDAY;
                            break;
                        case "Thursday":
                            specificDaysOfWeek[i] = Calendar.THURSDAY;
                            break;
                        case "Friday":
                            specificDaysOfWeek[i] = Calendar.FRIDAY;
                            break;
                        case "Saturday":
                            specificDaysOfWeek[i] = Calendar.SATURDAY;
                            break;
                    }
                }
                int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

                if (Arrays.asList(specificDaysOfWeek).contains(cal.get(Calendar.DAY_OF_WEEK))) {

                    AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent1 = new Intent(context, AlarmReceiver.class);
                    intent1.putExtra("medName", medicineModel.getMedicineName());
                    intent1.putExtra("mealStatus", medicineModel.getMedicineMeal());
                    intent1.putExtra("time", combine);
                    intent1.putExtra("status", medicineModel.getStatus());
                    intent1.putExtra("med", "true");
                    intent1.putExtra("medId", medicineModel.getId());
                    PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, medicineModel.getId(), intent1, PendingIntent.FLAG_IMMUTABLE);

                    alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);

                    Log.d("SampleBootReceiver", "Alarm set for " + medicineModel.getMedicineName() + " with id " + medicineModel.getId() + " for " + numDays + " days");

                    // Increment the date by 1 day for the next alarm
                    cal.add(Calendar.DATE, 1);

                    // Set the remaining alarms for the next numDays-1 days
                    for (int i = 1; i < numDays; i++) {

                        // Increment the date by 1 day
                        cal.add(Calendar.DATE, 1);

                        // Check if it's the correct day of the week
                        if (Arrays.asList(specificDaysOfWeek).contains(cal.get(Calendar.DAY_OF_WEEK))) {

                            Intent nextIntent = new Intent(context, AlarmReceiver.class);
                            nextIntent.putExtra("medName", medicineModel.getMedicineName());
                            nextIntent.putExtra("mealStatus", medicineModel.getMedicineMeal());
                            nextIntent.putExtra("time", combine);
                            nextIntent.putExtra("status", medicineModel.getStatus());
                            nextIntent.putExtra("med", "true");
                            nextIntent.putExtra("medId", medicineModel.getId());
                            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, medicineModel.getId() + i, nextIntent, PendingIntent.FLAG_IMMUTABLE);

                            alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, nextPendingIntent);

                            requestCode++;

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("requestCodeValue", requestCode);
                            editor.commit();
                        }

                    }
                }
            }
        }
    }
}