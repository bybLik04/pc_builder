package com.example.pc_builder;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pc_builder.databinding.ActivityAboutAppBinding;
import com.example.pc_builder.databinding.ActivityLessonDetailBinding;

public class AboutAppActivity extends AppCompatActivity {
    private ActivityAboutAppBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.LessonDetailAppBar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}