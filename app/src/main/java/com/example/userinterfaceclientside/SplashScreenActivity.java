package com.example.userinterfaceclientside;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    // Duration of splash screen in milliseconds
    private static final int SPLASH_DISPLAY_DURATION = 1000; // 1 seconds

    ImageView splashImage;
    TextView appNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Find splash screen views
        splashImage = findViewById(R.id.splashImage);
        appNameText = findViewById(R.id.appNameText);

        // Delay for splash screen and then start the next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent that will start the next activity
                Intent intent = new Intent(SplashScreenActivity.this, chooseLoginOrSignup.class);
                startActivity(intent);
                finish(); // Finish this activity so it cannot be returned to
            }
        }, SPLASH_DISPLAY_DURATION);
    }
}
