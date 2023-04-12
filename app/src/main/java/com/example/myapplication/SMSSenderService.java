package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.Nullable;

public class SMSSenderService extends Service {

        private static final String TAG = "SMSSenderService";

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
                Log.d(TAG, "onStartCommand");

                // Get the phone number and message from the intent
                String phoneNumber = intent.getStringExtra("phoneNumber");
                String message = intent.getStringExtra("message");

                // Send the SMS message
                try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                        Log.d(TAG, "SMS sent to " + phoneNumber);
                } catch (Exception e) {
                        Log.e(TAG, "Failed to send SMS", e);
                }

                // Stop the service
                stopSelf();

                return START_NOT_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
                return null;
        }
}