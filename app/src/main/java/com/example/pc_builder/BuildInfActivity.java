package com.example.pc_builder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pc_builder.databinding.ActivityBuildInfBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildInfActivity extends AppCompatActivity {
    private ActivityBuildInfBinding binding;
    private String docId;
    private String partRef;
    private String partType;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Builds builds;
    private int totalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuildInfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        docId = getIntent().getStringExtra("TITLE");

        loadBuildFromFirestore();

        binding.cpuCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, PartInfActivity.class);
            intent.putExtra("PART", "CPU");
            intent.putExtra("DOC", docId);
            intent.putExtra("REF", partRef);
            startActivity(intent);
            finish();
        });
        binding.gpuCard.setOnClickListener(v -> {
            Intent intent = new Intent(this, PartInfActivity.class);
            intent.putExtra("PART", "GPU");
            intent.putExtra("DOC", docId);
            intent.putExtra("REF", partRef);
            startActivity(intent);
            finish();
        });
        binding.buildInfAppBar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
    private void loadBuildFromFirestore() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DocumentReference dreamsCollectionRef = db.collection(userId).document(docId);

            dreamsCollectionRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    builds = document.toObject(Builds.class);
                    builds.setTitle(document.getId());
                    initViews();
                    Log.d("TASK", task.getResult().getId() + " => " + task.getResult().getData());
                }
            });
        }
    }
    private void loadPartsFromFirestore(DocumentReference part) {
        if (part != null) {
            part.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Map<String, Object> partData = document.getData();
                        initPartsView(part.getPath(), partData, document.getId());
                        partRef = part.getPath();
                        Log.d("part", part.getPath() + " => " + document.getId() + " => " + document.getData());
                    }
                }
            });
        }
    }
    private void initViews() {
        binding.buildName.setText(builds.getTitle());

        if (builds.getCpu() == null) {
            binding.addCPU.setText("Add CPU");
            binding.cpuName.setText(R.string.empty_cpu);
            binding.cpuImage.setImageResource(R.drawable.add_30dp);
            binding.cpuPrice.setVisibility(View.GONE);
            binding.cpuInf1.setVisibility(View.GONE);
            binding.cpuInf2.setVisibility(View.GONE);
        } else {
            loadPartsFromFirestore(builds.getCpu());
        }
        if (builds.getGpu() == null) {
            binding.addGPU.setText("Add GPU");
            binding.gpuName.setText(R.string.empty_gpu);
            binding.gpuImage.setImageResource(R.drawable.add_30dp);
            binding.gpuPrice.setVisibility(View.GONE);
            binding.gpuInf1.setVisibility(View.GONE);
            binding.gpuInf2.setVisibility(View.GONE);
        } else {
            loadPartsFromFirestore(builds.getGpu());
        }
    }
    private void initPartsView(String partRef, Map<String, Object> partData, String partName) {
        totalPrice += Integer.parseInt(partData.get("price").toString());
        String totalCost = String.valueOf(totalPrice) + " " + getString(R.string.ruble);
        binding.totalPrice.setText(totalCost);
        setTotalPrice(totalPrice);

        if (partRef.contains("Processors ")) {
            binding.addCPU.setText("CPU");
            binding.cpuName.setText(partName);
            binding.cpuImage.setImageResource(R.drawable.procesor);

            binding.cpuPrice.setText(String.valueOf(partData.get("price")));
            binding.cpuPrice.setVisibility(View.VISIBLE);

            String inf1 = "Ядра / Потоки: " + String.valueOf(partData.get("cores")) +
                    " / " + String.valueOf(partData.get("threads"));
            binding.cpuInf1.setText(inf1);
            binding.cpuInf1.setVisibility(View.VISIBLE);
            binding.cpuInf2.setText("Частота: " + String.valueOf(partData.get("clock")));
            binding.cpuInf2.setVisibility(View.VISIBLE);
        }
        if (partRef.contains("Video cards")) {
            binding.addGPU.setText("GPU");
            binding.gpuName.setText(partName);
            binding.gpuImage.setImageResource(R.drawable.procesor);

            binding.gpuPrice.setText(String.valueOf(partData.get("price")));
            binding.gpuPrice.setVisibility(View.VISIBLE);

            String inf1 = "Видеопамять: " + String.valueOf(partData.get("VRAMS")) +
                    " / " + String.valueOf(partData.get("VRAMT"));
            binding.gpuInf1.setText(inf1);
            binding.gpuInf1.setVisibility(View.VISIBLE);
            binding.gpuInf2.setText("Частота: " + String.valueOf(partData.get("Clock")));
            binding.gpuInf2.setVisibility(View.VISIBLE);
            Log.d(")", "кайф же");
        }
    }
    private void setTotalPrice(int totalPrice) {

        db.collection(auth.getCurrentUser().getUid())
                .document(docId)
                .update("Cost", totalPrice).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    }
                })
                .addOnFailureListener(e -> {

                });
    }
}