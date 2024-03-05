package com.example.pc_builder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BuildFragment extends Fragment {

    public BuildFragment() {
        // Required empty public constructor
    }

    public static BuildFragment newInstance(String param1, String param2) {
        BuildFragment fragment = new BuildFragment();
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
        return inflater.inflate(R.layout.fragment_build, container, false);
    }
}