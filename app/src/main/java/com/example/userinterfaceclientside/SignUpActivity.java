package com.example.userinterfaceclientside;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText editName, editEmployeeid, editBranch, editPassword;
    Button submit;
    DatabaseReference mdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editName = findViewById(R.id.editName);
        editEmployeeid = findViewById(R.id.editEmployeeid);
        editBranch = findViewById(R.id.editBranch);
        editPassword = findViewById(R.id.editPassword);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdatabase = FirebaseDatabase.getInstance().getReference("Employee");
                String name = editName.getText().toString();
                String employeeId = editEmployeeid.getText().toString();
                String branch = editBranch.getText().toString();
                String password = editPassword.getText().toString();

                if (name.isEmpty() || employeeId.isEmpty() || branch.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    mdatabase = FirebaseDatabase.getInstance().getReference("Employee");
                    Helper obj = new Helper(name, employeeId, branch, password);
                    mdatabase.child(employeeId).setValue(obj);

                    // Save employee ID in SharedPreferences
                    getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            .edit()
                            .putString("employee_id", employeeId)
                            .apply();

                    Toast.makeText(SignUpActivity.this, "Signup successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
