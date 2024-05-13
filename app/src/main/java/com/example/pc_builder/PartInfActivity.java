package com.example.pc_builder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pc_builder.databinding.ActivityPartInfBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class PartInfActivity extends AppCompatActivity {
    private ActivityPartInfBinding binding;
    private String docId;
    private String partType;
    private String partRef;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartInfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        docId = getIntent().getStringExtra("DOC");
        partType = getIntent().getStringExtra("PART");
        partRef = getIntent().getStringExtra("REF");
        loadPartInfFromFirestore(partRef);
        binding.partInfAppBar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(this, BuildInfActivity.class);
            intent.putExtra("TITLE", docId);
            startActivity(intent);
            finish();
        });
        binding.extendedFab.setOnClickListener(v -> {
            deletePartFromBuild();
        });
    }

    private void initViews(Map<String, Object> partData, String partName) {
        if(partRef.contains("Processors ")) {
            String[] image = partData.get("image").toString().split(",");
            Log.d("IMAGE",image[0]);
            Picasso.get().load(image[0].replace("[", "")).into(binding.partImage);
            binding.partName.setText(partName);
            binding.partInf1Title.setText("Ядра / Потоки");
            binding.partInf1.setText(String.valueOf(partData.get("cores")) + " / " + String.valueOf(partData.get("threads")));
            binding.partInf2Title.setText("Частота процессора");
            binding.partInf2.setText(String.valueOf(partData.get("clock")));
            binding.partInf3Title.setText("Сокет");
            binding.partInf3.setText(String.valueOf(partData.get("socket")));
            binding.partInf4Title.setText("L2, L3 кэш");
            binding.partInf4.setText(String.valueOf(partData.get("L2")) + " / " + String.valueOf(partData.get("L3")));
            binding.partInf5Title.setText("Поддерживаемая память");
            binding.partInf5.setText(String.valueOf(partData.get("mem")));
            binding.partInf6Title.setText("Тепловыделение (TDP)");
            binding.partInf6.setText(String.valueOf(partData.get("TDP")));
            binding.partInf7Title.setText("Дата выхода");
            binding.partInf7.setText(String.valueOf(partData.get("release")));
        }
        if (partRef.contains("Video cards")) {
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "")).into(binding.partImage);
            binding.partName.setText(partName);
            binding.partInf1Title.setText("Архитектура");
            binding.partInf1.setText(String.valueOf(partData.get("Architecture")));
            binding.partInf2Title.setText("Частота процессора");
            binding.partInf2.setText(String.valueOf(partData.get("Clock")));
            binding.partInf3Title.setText("Объем памяти");
            binding.partInf3.setText(String.valueOf(partData.get("VRAMS")));
            binding.partInf4Title.setText("Тип памяти");
            binding.partInf4.setText(String.valueOf(partData.get("VRAMT")));
            binding.partInf5Title.setText("Частота памяти");
            binding.partInf5.setText(String.valueOf(partData.get("VRAMC")));
            binding.partInf6Title.setText("Разрядность шины");
            binding.partInf6.setText(String.valueOf(partData.get("Bus")));
            binding.partInf7Title.setText("Потребляемая мощность");
            binding.partInf7.setText(String.valueOf(partData.get("Power")));
        }
    }
    private void loadPartInfFromFirestore(String part) {
        if (part != null) {
            DocumentReference reference = db.document(part);
            reference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Map<String, Object> partData = document.getData();
                        initViews(partData, document.getId());
                        //initPartsView(part.getPath(), partData, document.getId());
                        Log.d("part", reference.getPath() + " => " + document.getId() + " => " + document.getData());
                    }
                }
            });
        }
    }
    private void deletePartFromBuild() {
        db.collection(auth.getCurrentUser().getUid())
                .document(docId)
                .update(partType, null)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, BuildInfActivity.class);
                        intent.putExtra("TITLE", docId);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}