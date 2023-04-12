package com.example.myapplication.recicver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.example.myapplication.AlarmRing;
import com.example.myapplication.NotificationActivity;
import com.example.myapplication.NotificationDetailsActivity;
import com.example.myapplication.NotificationHelper;


public class AlarmReceiver  extends BroadcastReceiver {
    NotificationHelper helper;

    @RequiresPermission(Manifest.permission.FOREGROUND_SERVICE)
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(Context context, Intent intent) {
        String id=intent.getStringExtra("medId");
        String medName = intent.getStringExtra("medName");
        String time = intent.getStringExtra("time");
        String mealStatus = intent.getStringExtra("mealStatus");
        //  String type = intent.getStringExtra("medType");
        String med = intent.getStringExtra("med");
        String medStatus = intent.getStringExtra("status");

        helper = new NotificationHelper(context);

        int taken = intent.getIntExtra("taken", -5);
        int details = intent.getIntExtra("details", -7);
        int cancel = intent.getIntExtra("cancel", -9);


        int appOk = intent.getIntExtra("appOk", -88);
        int appCancel = intent.getIntExtra("appCancel", -99);

        int main = intent.getIntExtra("main", -100);


        if (main == 100) {

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();

            Intent notificationIntent = new Intent(context, NotificationActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(notificationIntent);


        }


        if (taken == 11) {


            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            helper.getManager().cancelAll();
            Toast.makeText(context, "You taken the medicine", Toast.LENGTH_SHORT).show();
        }


        if (cancel == 22) {

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "You cancel the medicine", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();
        }
        if (details == 33){
            String medName1 = intent.getStringExtra("medName");
            String time1 = intent.getStringExtra("time");
            String mealStatus1 = intent.getStringExtra("mealStatus");
            Intent detailsIntent = new Intent(context, NotificationDetailsActivity.class);
            detailsIntent.putExtra("medicine_name", medName1);
            detailsIntent.putExtra("medicine_time", time1);
            detailsIntent.putExtra("meal_status", mealStatus1);
            detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(detailsIntent);

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

           helper.getManager().cancelAll();
        }

        if (appOk == 88) {

            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);


            helper.getManager().cancelAll();
        }

        if (appCancel == 99) {

            Intent stopIntent = new Intent(context, AlarmRing.class);
            context.stopService(stopIntent);

            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();

            helper.getManager().cancelAll();

        }


        if ("true".equals(med)) {

            Intent stopIntent = new Intent(context, AlarmRing.class);

            context.stopService(stopIntent);

            if (context.stopService(stopIntent)) {

                context.stopService(stopIntent);
            }

            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            Intent startIntent = new Intent(context, AlarmRing.class);
            startIntent.putExtra("med_name", medName);
            startIntent.putExtra("time", time);
            startIntent.putExtra("meal_status", mealStatus);
            // startIntent.putExtra("medStatus",mealStatus);
            startIntent.putExtra("ringtone_uri", "" + ringtoneUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.startForegroundService(startIntent);

            } else {

                context.startService(startIntent);

            }
        }


    }

}
