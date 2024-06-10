package com.example.pc_builder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_builder.databinding.ActivityMainBinding;
import com.example.pc_builder.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private MainActivity mainActivity;
    private ActivityMainBinding activityMainBinding;
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        // Устанавливаем новую высоту для изображений
        ViewGroup.LayoutParams layoutParams1 = binding.studyHomeImage.getLayoutParams();
        layoutParams1.height = (int) (height * 0.2);  // Уменьшение на 60% высоты экрана
        binding.studyHomeImage.setLayoutParams(layoutParams1);

        ViewGroup.LayoutParams layoutParams2 = binding.buildHomeImage.getLayoutParams();
        layoutParams2.height = (int) (height * 0.2);  // Уменьшение на 60% высоты экрана
        binding.buildHomeImage.setLayoutParams(layoutParams2);

        binding.studyHomeButton.setOnClickListener(v -> {
            StudyFragment studyFragment = new StudyFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            mainActivity.setAppBarTitle(getString(R.string.study_title));

            transaction.replace(R.id.frameLayout, studyFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        binding.buildHomeButton.setOnClickListener(v -> {
            BuildFragment buildFragment = new BuildFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            mainActivity.setAppBarTitle(getString(R.string.build_title));

            transaction.replace(R.id.frameLayout, buildFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return view;
    }
}