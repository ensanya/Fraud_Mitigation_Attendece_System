package com.example.userinterfaceclientside;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText employeeid, password;
    Button login_btn;
 TextView Login_subtitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize EditText and Button
        employeeid = findViewById(R.id.employeeid);
        password = findViewById(R.id.password);
        login_btn = findViewById(R.id.login_btn);

        Login_subtitle=findViewById(R.id.Login_subtitle);

        //go to signup page
        Login_subtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate username and password fields
                if (!validateUsername() || !validatingPassword()) {
                    Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // If fields are valid, check user credentials
                    checkUser();
                }
            }
        });
    }

    // Method to validate username field
    public Boolean validateUsername() {
        String val = employeeid.getText().toString();
        if (val.isEmpty()) {
            employeeid.setError("Username cannot be empty");
            return false;
        } else {
            employeeid.setError(null);
            return true;
        }
    }

    // Method to validate password field
    public Boolean validatingPassword() {
        String val = password.getText().toString();
        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    // Method to check user credentials
    public void checkUser() {
        String userUsername = employeeid.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        // Get reference to the "Employee" node in Firebase Database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Employee");

        // Query the database to find a user with the provided username
        Query checkUserDatabase = reference.orderByChild("employeeId").equalTo(userUsername);

        getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .edit()
                .putString("employee_id", userUsername)
                .apply();

        // Listen for the result of the database query
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If a user with the provided username exists
                if (snapshot.exists()) {
                    employeeid.setError(null);
                    // Get the password associated with the username from the database
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    // Check if the provided password matches the password from the database
                    if (passwordFromDB != null && userPassword != null && passwordFromDB.equals(userPassword)) {
                        password.setError(null);

                        // If passwords match, start the MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    } else {
                        // If passwords don't match, display error message
                        password.setError("Invalid credentials");
                        password.requestFocus();
                    }
                } else {
                    // If user doesn't exist, display error message
                    employeeid.setError("User does not exist");
                    employeeid.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle cancelled database query
                Toast.makeText(LoginActivity.this, "Cancelled request", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
