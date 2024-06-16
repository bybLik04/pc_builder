package com.example.pc_builder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pc_builder.databinding.ActivityLessonsPickBinding;

import java.util.List;

public class LessonsPickActivity extends AppCompatActivity {
    private ActivityLessonsPickBinding binding;
    private LessonsPickAdapter adapter;
    private String query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLessonsPickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rvLessons.setLayoutManager(new LinearLayoutManager(this));

        int part = getIntent().getIntExtra("PART", 1);
        if (part == 1) {
            query = "study_titles";
        } else if (part == 2) {
            query = "build_titles";
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Lessons> lessons = dbHelper.getAllLessons(query);
        dbHelper.close();

        adapter = new LessonsPickAdapter(lessons, this, part, query);
        binding.rvLessons.setAdapter(adapter);

        binding.LessonsPickAppBar.setNavigationOnClickListener( v ->{
            finish();
        });
    }
}