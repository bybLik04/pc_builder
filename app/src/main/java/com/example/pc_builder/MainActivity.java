package com.example.pc_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pc_builder.databinding.ActivityLoginBinding;
import com.example.pc_builder.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setNavigationOnClickListener( v ->{
            binding.drawerLayout.openDrawer(GravityCompat.START);
        });

        setNewFragment(new HomeFragment());

        binding.navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_item) {
                setNewFragment(new HomeFragment());
                setAppBarTitle(getString(R.string.home_title));
            } else if (item.getItemId() == R.id.study_item) {
                setNewFragment(new StudyFragment());
                setAppBarTitle(getString(R.string.study_title));
            } else if (item.getItemId() == R.id.build_item) {
                setNewFragment(new BuildFragment());
                setAppBarTitle(getString(R.string.build_title));
            } else if (item.getItemId() == R.id.settings_item) {
                setNewFragment(new SettingsFragment());
                setAppBarTitle(getString(R.string.settings_title));
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
    private void setNewFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public ActivityMainBinding getActivityMainBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    public void setAppBarTitle(String title) {
        binding.topAppBar.setTitle(title);
    }
}