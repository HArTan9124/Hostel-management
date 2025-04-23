package com.example.finalmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class splashscreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2500;
    private LottieAnimationView lottieView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        lottieView = findViewById(R.id.lottieView);
        lottieView.setRepeatCount(-1);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
            String userType = sharedPreferences.getString("userType", "none");

            if (isLoggedIn) {
                if (userType.equals("staff")) {
                    startActivity(new Intent(this, StaffLanding.class));
                } else if (userType.equals("student")) {
                    startActivity(new Intent(this, StudentLanding.class));
                } else {
                    startActivity(new Intent(this, bothlogin.class));
                }
            } else {
                startActivity(new Intent(this, bothlogin.class));
            }
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
