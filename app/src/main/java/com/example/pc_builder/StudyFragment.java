package com.example.pc_builder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_builder.databinding.FragmentHomeBinding;
import com.example.pc_builder.databinding.FragmentStudyBinding;

public class StudyFragment extends Fragment {
    private FragmentStudyBinding binding;

    public StudyFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStudyBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.part1Card.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LessonsPickActivity.class);
            intent.putExtra("PART", 1);
            startActivity(intent);
        });

        binding.part2Card.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LessonsPickActivity.class);
            intent.putExtra("PART", 2);
            startActivity(intent);
        });

        binding.part3Card.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TestActivity.class);
            intent.putExtra("LESSON_NUMBER", "ИТОГОВЫЙ");
            startActivity(intent);
        });
        return view;
    }
}