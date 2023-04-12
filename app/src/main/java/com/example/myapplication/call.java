package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.adapter.ContactListAdapter;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class call extends AppCompatActivity implements LocationListener {
    private ContactManager contactManager;

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    // Define the contact object
    public static class Contact {
        private String name;
        private String phone;


        public Contact(String name, String phone) {

            this.name = name;
            this.phone = phone;

        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
        this.phone=phone;
        }
    }

    // Define the array to store contacts
    private ImageView mEmergencyButton;
    private Button mAddContactButton;
    private ListView mContactListView;
    private ContactListAdapter mAdapter;
    private List<Contact> mContacts;
    private ContactsDatabaseHelper mDbHelper;
    private LocationRequest locationRequest;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        mEmergencyButton = findViewById(R.id.emergency_button);
        mAddContactButton = findViewById(R.id.add_contact_button);
        mContactListView = findViewById(R.id.contact_list);
        mContacts = new ArrayList<>();
        mAdapter = new ContactListAdapter(this, mContacts);
        mContactListView.setAdapter(mAdapter);
        mContactListView.setEnabled(true);
        mContactListView.setClickable(true);
        mDbHelper = new ContactsDatabaseHelper(this);

        mEmergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the app has the SEND_SMS permission
                if (ContextCompat.checkSelfPermission(call.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    ActivityCompat.requestPermissions(call.this, new String[] { Manifest.permission.SEND_SMS }, 12);
                } else {
                    // Permission already granted, send SMS
                    getCurrentLocation();
                }
            }
        });

        mAddContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle add contact button click
                AlertDialog.Builder builder = new AlertDialog.Builder(call.this);
                builder.setTitle("Add Contact");

                View view = LayoutInflater.from(call.this).inflate(R.layout.dialog_add_contact, null);
                final EditText nameEditText = view.findViewById(R.id.name_edittext);
                final EditText phoneEditText = view.findViewById(R.id.phone_edittext);

                builder.setView(view);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = nameEditText.getText().toString().trim();
                        String phone = phoneEditText.getText().toString().trim();

                        if (name.isEmpty() || phone.isEmpty()) {
                            Toast.makeText(call.this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long id = mDbHelper.addContact(new Contact(name, phone));
                        Contact contact = new Contact( name, phone);

                        mContacts.add(contact);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", null);

                builder.show();
            }
        });

        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected contact from the list
                ContactListAdapter adapter = (ContactListAdapter) parent.getAdapter();
                final Contact contact = mContacts.get(position);

                // Create an alert dialog for editing or deleting the contact
                AlertDialog.Builder builder = new AlertDialog.Builder(call.this);
                builder.setTitle("Edit or delete contact");

                // Inflate the dialog view
                View dialogView = LayoutInflater.from(call.this).inflate(R.layout.dialog_add_contact, null);
                final EditText nameEditText = dialogView.findViewById(R.id.name_edittext);
                final EditText phoneEditText = dialogView.findViewById(R.id.phone_edittext);

                // Set the current contact data in the dialog view
                nameEditText.setText(contact.getName());
                phoneEditText.setText(contact.getPhone());

                // Add the dialog view to the alert dialog
                builder.setView(dialogView);

                // Set the positive button for editing the contact
                builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the updated contact data from the dialog view
                        String name = nameEditText.getText().toString().trim();
                        String phone = phoneEditText.getText().toString().trim();

                        // Validate the updated contact data
                        if (name.isEmpty() || phone.isEmpty()) {
                            Toast.makeText(call.this, "Please enter both name and phone number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Update the contact in the database and refresh the list view
                        contact.setName(name);
                        contact.setPhone(phone);
                        mDbHelper.updateContact(contact);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                // Set the negative button for deleting the contact
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the contact from the list and database, and refresh the list view
                        mContacts.remove(contact);
                        mDbHelper.deleteContact(contact);
                        mAdapter.notifyDataSetChanged();
                    }
                });

                // Set the neutral button for canceling the dialog
                builder.setNeutralButton("Cancel", null);

                // Show the alert dialog
                builder.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mContacts.clear();
        mContacts.addAll(mDbHelper.getAllContacts());
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 12) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, send SMS
                sendSMS(latitude, longitude);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    getCurrentLocation();

                } else {

                    turnOnGPS();
                }
            }
        }
    }
    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(call.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {

                    LocationServices.getFusedLocationProviderClient(call.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(call.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                         latitude = locationResult.getLocations().get(index).getLatitude();
                                         longitude = locationResult.getLocations().get(index).getLongitude();
                                        sendSMS(latitude,longitude);

                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(call.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(call.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }


    private void sendSMS(double latitude, double longitude) {
        String location=String.format("http://maps.google.com/maps?q=%s,%s", latitude, longitude);
        SmsManager smsManager = SmsManager.getDefault();
        for (Contact contact : mContacts) {
            smsManager.sendTextMessage(contact.getPhone(), null, "I am in trouble please help me"+location, null, null);
        }
        Toast.makeText(this, "SMS sent to all contacts", Toast.LENGTH_SHORT).show();
    }
}