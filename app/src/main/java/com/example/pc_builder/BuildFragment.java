package com.example.pc_builder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pc_builder.databinding.FragmentBuildBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuildFragment extends Fragment implements AddBuildDialogFragment.AddBuildDialogListener{
    private FragmentBuildBinding binding;
    private BuildsAdapter buildsAdapter;
    private List<Builds> builds;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    static final int REQUEST_CODE_BUILD_INF = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        builds = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBuildBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        loadBuildsFromFirestore();

        init();

        binding.fabAdd.setOnClickListener(v -> {
            showAddBuildDialog();
        });

        return view;
    }
    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        binding.rvBuild.setLayoutManager(layoutManager);
        buildsAdapter = new BuildsAdapter(builds, getActivity());
        binding.rvBuild.setAdapter(buildsAdapter);
    }
    private void showAddBuildDialog() {
        AddBuildDialogFragment dialog = new AddBuildDialogFragment(auth, db);
        dialog.setListener(this);
        dialog.show(getChildFragmentManager(), "AddBuildDialog");
    }
    @Override
    public void onBuildCreated(String buildName, String notificationTitle) {
        loadBuildsFromFirestore();
        Toast.makeText(getActivity(), notificationTitle, Toast.LENGTH_LONG).show();
    }

    private void loadBuildsFromFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            CollectionReference dreamsCollectionRef = db.collection(userId);

            dreamsCollectionRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    builds.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Builds build = document.toObject(Builds.class);
                        build.setTitle(document.getId());
                        builds.add(build);
                        if (document.exists()){
                            binding.buildsEmptyText.setVisibility(View.GONE);
                        }   else {
                            binding.buildsEmptyText.setVisibility(View.VISIBLE);
                        }
                    }
                    buildsAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}