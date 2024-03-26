package com.example.pc_builder;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pc_builder.databinding.ActivityLessonsPickBinding;

import java.util.List;

public class LessonsPickActivity extends AppCompatActivity {
    private ActivityLessonsPickBinding binding;
    private LessonsPickAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonsPickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvLessons.setLayoutManager(new LinearLayoutManager(this));

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Lesson> lessons = dbHelper.getAllLessons();
        dbHelper.close();

        adapter = new LessonsPickAdapter(lessons, this);
        binding.rvLessons.setAdapter(adapter);

        binding.LessonsPickAppBar.setNavigationOnClickListener( v ->{
            finish();
        });
    }
}