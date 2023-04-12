package com.example.myapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.recicver.AlarmReceiver;


public class NotificationHelper extends ContextWrapper {

    public static final String channelId = "channel ID";
    public static final String channelName = "channel Name";



    private NotificationManager manager;


    public NotificationHelper(Context base) {


        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createChannels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {

        NotificationChannel channel1 = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);



    }

    public NotificationManager getManager(){

        if (manager == null){

            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }

        return manager;
    }

    @SuppressLint("NotificationTrampoline")
    public NotificationCompat.Builder getChannelNotification(String title, String message){

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("main", 100);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);


        Intent takenIntent = new Intent(this, AlarmReceiver.class);
        takenIntent.setAction("taken");
        takenIntent.putExtra("taken", 11);

        Intent cancelIntent = new Intent(this, AlarmReceiver.class);
        cancelIntent.setAction("cancel");
        cancelIntent.putExtra("cancel", 22);


        Intent detailsIntent = new Intent(this,AlarmReceiver.class);
        detailsIntent.setAction("details");
        detailsIntent.putExtra("details", 33);
      //  startActivity(detailsIntent);


        PendingIntent takenPendingIntent =
                PendingIntent.getBroadcast(this, 0, takenIntent, PendingIntent.FLAG_MUTABLE);

        PendingIntent cancelPendingIntent =
                PendingIntent.getBroadcast(this, 0, cancelIntent, PendingIntent.FLAG_MUTABLE);

        PendingIntent detailsPendingIntent =
                PendingIntent.getBroadcast(this, 0, detailsIntent, PendingIntent.FLAG_MUTABLE);




        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityIntent)
                .setSmallIcon(R.drawable.ic_medichine)
               .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .addAction(R.drawable.ic_details,
                        "Details",
                        detailsPendingIntent)
                .addAction(R.drawable.ic_cancel,
                        "Skip",
                        cancelPendingIntent)
                .setAutoCancel(true)
                .setOngoing(false);
    }

}
