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
        // Required empty public constructor
    }
    public static StudyFragment newInstance(String param1, String param2) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
            startActivity(intent);
        });

        return view;
    }
}