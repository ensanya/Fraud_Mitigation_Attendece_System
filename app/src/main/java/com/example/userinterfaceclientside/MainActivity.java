package com.example.userinterfaceclientside;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PolygonTest.GeofenceCallback {


    FusedLocationProviderClient mFusedLocationClient;
    TextView latitudeTextView, longitudeTextView, attendenceStatus, todaysDate;
    int PERMISSION_ID = 44;
    String employeeId;
    Location currentLocation;
    Button markAttendence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve employee ID from SharedPreferences
        employeeId = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("employee_id", null);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);

        todaysDate = findViewById(R.id.todaysDate);
        //---------------------------------------------------------------------------------------------
        Date userDate = new Date();    // Create a date instance for the current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault());// Define the desired date format
        String formattedDate = dateFormat.format(userDate);   // Format the date
        todaysDate.setText(formattedDate);  // Set the formatted date string to the TextView
        //------------------------------------------------------------------------------------------

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        // Start the Foreground Service
        Intent serviceIntent = new Intent(this, ForeGroundService.class);
        serviceIntent.putExtra("employee_id", employeeId); // Pass the employee ID to the service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
// Mark the studnent attendece

        markAttendence = findViewById(R.id.markAttendence);
        markAttendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("events");

                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String closingTimeString = snapshot.child("closingTime").getValue(String.class);
                            String eventDateString = snapshot.child("date").getValue(String.class);
                            String eventTimeString = snapshot.child("time").getValue(String.class);

                            if (closingTimeString != null && eventDateString != null && eventTimeString != null) {
                                Log.d("Event Data", "Closing Time: " + closingTimeString + ", Event Date: " + eventDateString + ", Event Time: " + eventTimeString);


                                try {

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                                    Date eventDate = dateFormat.parse(eventDateString);  //event
                                    Date eventTime = timeFormat.parse(eventTimeString);  //event
                                    Date closingTime = timeFormat.parse(closingTimeString);   //event

                                    // Get the current date and time
                                    Date currentDate = new Date();
                                    String currentDateString = dateFormat.format(currentDate);
                                    String currentTimeString = timeFormat.format(currentDate);

                                    Date userDate = dateFormat.parse(currentDateString);
                                    Date userTime = timeFormat.parse(currentTimeString);

                                    Log.e(" User Date ", "Error occurred: " + userDate);
                                    Log.e(" Event Date ", "Error occurred: " + eventDateString);
                                    // Check if the dates match
                                    if (userDate.equals(eventDate)) {
                                        // Compare the times
                                        if (userTime.before(eventTime)) {
                                            // User arrived early
                                            Toast.makeText(MainActivity.this, "You arrived earlier.", Toast.LENGTH_SHORT).show();
                                        } else if (userTime.after(closingTime)) {
                                            // User arrived late
                                            Toast.makeText(MainActivity.this, "The time has ended.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // User arrived within the allowed time window

                                            DatabaseReference empRef = FirebaseDatabase.getInstance().getReference().child("Employee").child(employeeId).child("location");

                                            empRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                                                        Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                                                        // Now you can use latitude and longitude

                                                        Point userLocation = new Point(latitude, longitude); // Example user location
                                                        PolygonTest.main(userLocation, MainActivity.this);
                                                    } else {
                                                        // Handle the case where the data doesn't exist
                                                        Toast.makeText(MainActivity.this, "Location data for employee not found.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    // Handle potential errors here
                                                    Log.e("Firebase Error", "Error occurred: " + databaseError.getMessage());
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Event date does not match.", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("Event Data", "One or more event data fields are null");
                            }
                        } else {
                            Log.e("Event Data", "Snapshot does not exist");
                            Toast.makeText(MainActivity.this, "snapshot does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("DatabaseError", "onCancelled: " + databaseError.getMessage());
                    }
                });
            }
        });
        String ctDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Check event date from Firebase
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events");
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String eventDateString = dataSnapshot.child("date").getValue(String.class);
                    if (eventDateString != null && eventDateString.equals(ctDate)) {
                        // Event date matches current date, check attendance status
                        checkAttendanceStatus(ctDate);
                    } else {
                        // Event date doesn't match current date
                        Toast.makeText(MainActivity.this, "No event scheduled for today.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // No event data found
                    Toast.makeText(MainActivity.this, "No event data found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase Error", "Error occurred: " + databaseError.getMessage());
                Toast.makeText(MainActivity.this, "Failed to check event date.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            currentLocation = location;
                            updateLocationInFirebase(location);
                            updateUIWithLocation(location);
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    public void requestNewLocationData() {
        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(0)
                .setFastestInterval(0)
                .setNumUpdates(1);

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if (mLastLocation != null) {
                currentLocation = mLastLocation;
                updateLocationInFirebase(mLastLocation);
                updateUIWithLocation(mLastLocation);
            } else {
                Toast.makeText(MainActivity.this, "Failed to get location.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }

    private void updateLocationInFirebase(Location location) {
        if (employeeId != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Employee").child(employeeId).child("location");
            databaseRef.child("latitude").setValue(location.getLatitude());
            databaseRef.child("longitude").setValue(location.getLongitude());
        }
    }

    private void updateUIWithLocation(Location location) {
        latitudeTextView.setText("Latitude: " + location.getLatitude());
        longitudeTextView.setText("Longitude: " + location.getLongitude());
    }

    @Override
    public void onGeofenceCheckResult(boolean insideGeofence) {
        // Display a toast message based on the result of the geofence check
        if (insideGeofence) {
            Toast.makeText(MainActivity.this, "Attendence Marked Successfully", Toast.LENGTH_SHORT).show();
            markAttendance(true); // Mark as present
        } else {
            Toast.makeText(MainActivity.this, "Not present ,Outside the geofence area", Toast.LENGTH_SHORT).show();
            markAttendance(false); // Mark as present
        }
    }

    private void markAttendance(boolean present) {
        String employeeId = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("employee_id", null);
        if (employeeId == null) {
            Log.e("Attendance", "Employee ID is null.");
            return;
        }

        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference().child("Employee").child(employeeId).child("attendance");

        // Get the current date in "yyyy-MM-dd" format
        String currentDateString = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Check if the attendance for the current date is already marked
        attendanceRef.child(currentDateString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Attendance is already marked for today
                    //Log.d("Attendance", "Attendance for today is already marked.");
                    Toast.makeText(MainActivity.this, "Attendance for today is already marked.", Toast.LENGTH_SHORT).show();
                } else {
                    // Attendance is not marked yet, proceed to mark it
                    HashMap<String, Object> attendanceData = new HashMap<>();
                    attendanceData.put(currentDateString, present ? "Present" : "Absent");
                    attendenceStatus = findViewById(R.id.attendenceStatus);
                    if (present) {
                        attendenceStatus.setText("Present");
                    } else {
                        attendenceStatus.setText("Absent");
                    }
                    attendanceRef.updateChildren(attendanceData).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Attendance", "Attendance marked successfully.");
                            Toast.makeText(MainActivity.this, "Attendance marked successfully.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Attendance", "Failed to mark attendance.", task.getException());
                            Toast.makeText(MainActivity.this, "Failed to mark attendance.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase Error", "Error occurred: " + databaseError.getMessage());
                Toast.makeText(MainActivity.this, "Failed to check attendance.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAttendanceStatus(String currentDate) {
        // Check attendance status for the current date
        DatabaseReference attendanceRef = FirebaseDatabase.getInstance().getReference()
                .child("Employee").child(employeeId).child("attendance").child(currentDate);

        attendanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String status = dataSnapshot.getValue(String.class);
                    if (status != null) {
                        // Attendance is marked for today, update UI
                        attendenceStatus = findViewById(R.id.attendenceStatus);
                        attendenceStatus.setText(status);
                        int color = status.equals("Present") ? Color.GREEN : Color.RED;
                        attendenceStatus.setTextColor(color);
                    }
                } else {
                    // Attendance is not marked for today
                    Toast.makeText(MainActivity.this, "Attendance not marked for today.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase Error", "Error occurred: " + databaseError.getMessage());
                Toast.makeText(MainActivity.this, "Failed to check attendance.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
