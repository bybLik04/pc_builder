package com.example.pc_builder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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
        List<Lessons> lessons = dbHelper.getAllLessons();
        dbHelper.close();

        adapter = new LessonsPickAdapter(lessons, this);
        binding.rvLessons.setAdapter(adapter);

        binding.LessonsPickAppBar.setNavigationOnClickListener( v ->{
            finish();
        });
    }
}