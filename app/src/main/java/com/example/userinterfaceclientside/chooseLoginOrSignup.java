package com.example.userinterfaceclientside;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class chooseLoginOrSignup extends AppCompatActivity {
    Button btn_login,btn_signup,btn_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_or_sinup);
        btn_login=findViewById(R.id.btn_login);
        btn_signup=findViewById(R.id.btn_signup);
        //btn_main = findViewById(R.id.btn_main);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pntent=new Intent(chooseLoginOrSignup.this,LoginActivity.class);
                startActivity(pntent);
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(chooseLoginOrSignup.this, "signup butn is clicked", Toast.LENGTH_SHORT).show();
                Intent lntent= new Intent(chooseLoginOrSignup.this,SignUpActivity.class);
                startActivity(lntent);
            }
        });
//        btn_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(chooseLoginOrSignup.this, "Main Activity", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(chooseLoginOrSignup.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}