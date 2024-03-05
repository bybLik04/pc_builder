package com.example.pc_builder;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pc_builder.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2100;
    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        updateGreeting();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        binding.logoImage.setTranslationY(-800f);
        binding.greetingText.setTranslationY(800f);
        ObjectAnimator logoAnimator = ObjectAnimator.ofFloat(binding.logoImage, "translationY", 0f);
        logoAnimator.setDuration(1200);
        logoAnimator.start();
        ObjectAnimator textAnimator = ObjectAnimator.ofFloat(binding.greetingText, "translationY", 0f);
        textAnimator.setDuration(1200);
        textAnimator.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void updateGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;

        if (hourOfDay >= 6 && hourOfDay < 12) {
            greeting = getString(R.string.morning);
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greeting = getString(R.string.afternoon);
        } else if (hourOfDay >= 18 && hourOfDay < 24) {
            greeting = getString(R.string.evening);
        } else {
            greeting = getString(R.string.night);
        }

        binding.greetingText.setText(greeting);
    }
}