package com.example.pc_builder;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pc_builder.databinding.ActivityAddPartBinding;
import com.example.pc_builder.databinding.ActivityBuildInfBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddPartActivity extends AppCompatActivity {
    private ActivityAddPartBinding binding;
    private PartsAdapter adapter;
    private List<String> parts;
    private String partType;
    private String docId;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String partPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        docId = getIntent().getStringExtra("DOC");
        partType = getIntent().getStringExtra("PART");

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadPartsFromFirestore();
        initRv();

        binding.partAddAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, BuildInfActivity.class);
            intent.putExtra("TITLE", docId);
            startActivity(intent);
            finish();
        });
    }

    private void initRv() {
        binding.rvParts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PartsAdapter(docId, partPath, this, db, partType);
        binding.rvParts.setAdapter(adapter);
    }

    private void loadPartsFromFirestore() {
        partPath = "/configurator/components/" + partType;
        CollectionReference partsCollectionRef = db.collection(partPath);
        partsCollectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                parts = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    parts.add(document.getId());
                }
                adapter.setParts(parts);
                Log.d("List", parts.toString());
                adapter.notifyDataSetChanged();
            }
        });
    }
}