package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class NotificationDetailsActivity extends AppCompatActivity {
    private TextView medNameTextView;
    private TextView timeTextView;
    private TextView mealStatusTextView;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);

        medNameTextView = findViewById(R.id.medicineName);
        timeTextView = findViewById(R.id.medicineTime);
        mealStatusTextView = findViewById(R.id.medicineStatus);
        next=findViewById(R.id.nextbutton);
        // Get the details passed from the AlarmReceiver
        String medName = getIntent().getStringExtra("medicine_name");
        String time = getIntent().getStringExtra("medicine_time");
        String mealStatus = getIntent().getStringExtra("meal_status");
        medNameTextView.setText(medName);
        timeTextView.setText(time);
        mealStatusTextView.setText(mealStatus);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationDetailsActivity.this,AlarmView.class);

                // Get the medication details
                String medName = medNameTextView.getText().toString();
                String time = timeTextView.getText().toString();
                String mealStatus = mealStatusTextView.getText().toString();

                // Add the medication details as extras to the intent
                intent.putExtra("med_name", medName);
                intent.putExtra("time", time);
                intent.putExtra("meal_status", mealStatus);
                startActivity(intent);
            }

        });

    }
}
