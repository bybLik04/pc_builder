package com.example.pc_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_builder.databinding.ActivityMainBinding;
import com.example.pc_builder.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private FirebaseAuth mAuth;
    private ActivityMainBinding mainBinding;
    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentSettingsBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedPreferences preferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Uid", "");
            editor.apply();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        return view;
    }
}