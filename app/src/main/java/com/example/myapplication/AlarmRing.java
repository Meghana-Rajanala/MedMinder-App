package com.example.myapplication;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.myapplication.recicver.AlarmReceiver;

public class AlarmRing extends Service {

    Ringtone ringtone;
    private NotificationHelper notificationHelper;

    String medName,time,mealStatus,medicineName;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Uri ringtoneUri = Uri.parse(intent.getStringExtra("ringtone_uri"));
        medName = intent.getStringExtra("med_name");
       time = intent.getStringExtra("time");
        mealStatus = intent.getStringExtra("meal_status");
        //medicineName=medName.toUpperCase();
        if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.O){

            notificationHelper = new NotificationHelper(this);
            Notification notification = notificationHelper.getChannelNotification(medName,
                    "It's time to take " + medName + " at " + time + " (" + mealStatus + ")").build();
            // Set the notification to foreground
            startForeground(1, notification);
        }
        this.ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        ringtone.play();


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        ringtone.stop();
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("medName", medName);
        intent.putExtra("time", time);
        intent.putExtra("mealStatus", mealStatus);
        intent.putExtra("details", 33);

        sendBroadcast(intent);
    }

}
