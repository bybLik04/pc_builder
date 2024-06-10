package com.example.pc_builder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pc_builder.databinding.ActivityBuildInfBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildInfActivity extends AppCompatActivity {
    private ActivityBuildInfBinding binding;
    private String docId;
    private String partRef;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Builds builds;
    private int totalPrice = 0;
    private boolean incomplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBuildInfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.buildInfAppBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        docId = getIntent().getStringExtra("TITLE");

        loadBuildFromFirestore();

        binding.buildInfAppBar.setNavigationOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("INTENT", "back");
            startActivity(intent);
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
            incomplete = true;
            binding.addCPU.setText(R.string.add_cpu);
            binding.cpuName.setText(R.string.empty_cpu);
            binding.cpuImage.setImageResource(R.drawable.add_30dp);
            binding.cpuCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.cpuPrice.setVisibility(View.GONE);
            binding.cpuInf1.setVisibility(View.GONE);
            binding.cpuInf2.setVisibility(View.GONE);
            initButtons(binding.cpuCard, partRef, AddPartActivity.class, "CPU");

        } else {
            loadPartsFromFirestore(builds.getCpu());
        }
        if (builds.getGpu() == null) {
            incomplete = true;
            binding.addGPU.setText(R.string.add_gpu);
            binding.gpuName.setText(R.string.empty_gpu);
            binding.gpuImage.setImageResource(R.drawable.add_30dp);
            binding.gpuCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.gpuPrice.setVisibility(View.GONE);
            binding.gpuInf1.setVisibility(View.GONE);
            binding.gpuInf2.setVisibility(View.GONE);
            initButtons(binding.gpuCard, partRef, AddPartActivity.class, "GPU");
        } else {
            loadPartsFromFirestore(builds.getGpu());
        }
        if (builds.getRam() == null) {
            incomplete = true;
            binding.addRAM.setText(R.string.add_ram);
            binding.ramName.setText(R.string.empty_ram);
            binding.ramImage.setImageResource(R.drawable.add_30dp);
            binding.ramCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.ramPrice.setVisibility(View.GONE);
            binding.ramInf1.setVisibility(View.GONE);
            binding.ramInf2.setVisibility(View.GONE);
            initButtons(binding.ramCard, partRef, AddPartActivity.class, "RAM");
        } else {
            loadPartsFromFirestore(builds.getRam());
        }
        if (builds.getPsu() == null) {
            incomplete = true;
            binding.addPSU.setText(R.string.add_psu);
            binding.psuName.setText(R.string.empty_psu);
            binding.psuImage.setImageResource(R.drawable.add_30dp);
            binding.psuCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.psuPrice.setVisibility(View.GONE);
            binding.psuInf1.setVisibility(View.GONE);
            binding.psuInf2.setVisibility(View.GONE);
            initButtons(binding.psuCard, partRef, AddPartActivity.class, "PSU");
        } else {
            loadPartsFromFirestore(builds.getPsu());
        }
        if (builds.getCases() == null) {
            incomplete = true;
            binding.addCase.setText(R.string.add_case);
            binding.caseName.setText(R.string.empty_case);
            binding.caseImage.setImageResource(R.drawable.add_30dp);
            binding.caseCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.casePrice.setVisibility(View.GONE);
            binding.caseInf1.setVisibility(View.GONE);
            binding.caseInf2.setVisibility(View.GONE);
            initButtons(binding.caseCard, partRef, AddPartActivity.class, "Cases");
        } else {
            loadPartsFromFirestore(builds.getCases());
        }
        if (builds.getStorage() == null) {
            incomplete = true;
            binding.addStorage.setText(R.string.add_storage);
            binding.storageName.setText(R.string.empty_storage);
            binding.storageImage.setImageResource(R.drawable.add_30dp);
            binding.storageCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.storagePrice.setVisibility(View.GONE);
            binding.storageInf1.setVisibility(View.GONE);
            binding.storageInf2.setVisibility(View.GONE);
            initButtons(binding.storageCard, partRef, AddPartActivity.class, "Storage");
        } else {
            loadPartsFromFirestore(builds.getStorage());
        }
        if (builds.getMotherboard() == null) {
            incomplete = true;
            binding.addMotherboard.setText(R.string.add_motherboard);
            binding.motherName.setText(R.string.empty_motherboard);
            binding.motherImage.setImageResource(R.drawable.add_30dp);
            binding.motherCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.motherPrice.setVisibility(View.GONE);
            binding.motherInf1.setVisibility(View.GONE);
            binding.motherInf2.setVisibility(View.GONE);
            initButtons(binding.motherCard, partRef, AddPartActivity.class, "Motherboard");
        } else {
            loadPartsFromFirestore(builds.getMotherboard());
        }
        if (builds.getCooling() == null) {
            incomplete = true;
            binding.addCooling.setText(R.string.add_cooling);
            binding.coolingName.setText(R.string.empty_cooling);
            binding.coolingImage.setImageResource(R.drawable.add_30dp);
            binding.coolingCard.setCardBackgroundColor(getResources().getColor(R.color.red));
            binding.coolingPrice.setVisibility(View.GONE);
            binding.coolingInf1.setVisibility(View.GONE);
            binding.coolingInf2.setVisibility(View.GONE);
            initButtons(binding.coolingCard, partRef, AddPartActivity.class, "Cooling");
        } else {
            loadPartsFromFirestore(builds.getCooling());
        }
        if (!incomplete){
            binding.emptyParts.setVisibility(View.GONE);
        }
    }

    private void initPartsView(String partRef, Map<String, Object> partData, String partName) {
        totalPrice += Integer.parseInt(partData.get("price").toString());
        String totalCost = String.valueOf(totalPrice) + " " + getString(R.string.ruble);
        binding.totalPrice.setText(totalCost);
        setTotalPrice(totalPrice);

        if (partRef.contains("CPU")) {
            binding.addCPU.setText(R.string.cpu);
            binding.cpuName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.cpuImage);

            binding.cpuPrice.setText(String.valueOf(partData.get("price")));
            binding.cpuPrice.setVisibility(View.VISIBLE);

            String inf1 = "Ядра / Потоки: " + String.valueOf(partData.get("inf2")) +
                    " / " + String.valueOf(partData.get("inf3"));
            binding.cpuInf1.setText(inf1);
            binding.cpuInf1.setVisibility(View.VISIBLE);
            binding.cpuInf2.setText("Частота: " + String.valueOf(partData.get("inf1")));
            binding.cpuInf2.setVisibility(View.VISIBLE);
            initButtons(binding.cpuCard, partRef, PartInfActivity.class, "CPU");
        }
        if (partRef.contains("GPU")) {
            binding.addGPU.setText(R.string.gpu);
            binding.gpuName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.gpuImage);


            binding.gpuPrice.setText(String.valueOf(partData.get("price")));
            binding.gpuPrice.setVisibility(View.VISIBLE);

            String inf1 = "Видеопамять: " + String.valueOf(partData.get("VRAMS")) +
                    " / " + String.valueOf(partData.get("VRAMT"));
            binding.gpuInf1.setText(inf1);
            binding.gpuInf1.setVisibility(View.VISIBLE);
            binding.gpuInf2.setText("Частота: " + String.valueOf(partData.get("Clock")));
            binding.gpuInf2.setVisibility(View.VISIBLE);

            initButtons(binding.gpuCard, partRef, PartInfActivity.class, "GPU");
        }
        if (partRef.contains("RAM")) {
            binding.addRAM.setText(R.string.ram);
            binding.ramName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.ramImage);

            binding.ramPrice.setText(String.valueOf(partData.get("price")));
            binding.ramPrice.setVisibility(View.VISIBLE);

            binding.ramInf1.setText("Частота: " + String.valueOf(partData.get("clock")) + " " + String.valueOf(partData.get("CAS")));
            binding.ramInf1.setVisibility(View.VISIBLE);
            binding.ramInf2.setText("Объем: " + String.valueOf(partData.get("size")) + " " + String.valueOf(partData.get("type")));
            binding.ramInf2.setVisibility(View.VISIBLE);
            initButtons(binding.ramCard, partRef, PartInfActivity.class, "RAM");
        }
        if (partRef.contains("Storage")) {
            binding.addStorage.setText(R.string.storage);
            binding.storageName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.storageImage);

            binding.storagePrice.setText(String.valueOf(partData.get("price")));
            binding.storagePrice.setVisibility(View.VISIBLE);

            binding.storageInf1.setText("Объем: " + String.valueOf(partData.get("size")));
            binding.storageInf1.setVisibility(View.VISIBLE);
            binding.storageInf2.setText("Тип: " + String.valueOf(partData.get("type")));
            binding.storageInf2.setVisibility(View.VISIBLE);
            initButtons(binding.storageCard, partRef, PartInfActivity.class, "Storage");
        }
        if (partRef.contains("Motherboard")) {
            binding.addMotherboard.setText(R.string.motherboard);
            binding.motherName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.motherImage);

            binding.motherPrice.setText(String.valueOf(partData.get("price")));
            binding.motherPrice.setVisibility(View.VISIBLE);

            binding.motherInf1.setText(getString(R.string.socket) + String.valueOf(partData.get("socket")));
            binding.motherInf1.setVisibility(View.VISIBLE);
            binding.motherInf2.setText(getString(R.string.chipset) + String.valueOf(partData.get("chipset")));
            binding.motherInf2.setVisibility(View.VISIBLE);
            initButtons(binding.motherCard, partRef, PartInfActivity.class, "Motherboard");
        }
        if (partRef.contains("PSU")) {
            binding.addPSU.setText(R.string.psu);
            binding.psuName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.psuImage);

            binding.psuPrice.setText(String.valueOf(partData.get("price")));
            binding.psuPrice.setVisibility(View.VISIBLE);

            binding.psuInf1.setText("Сертификат: " + String.valueOf(partData.get("efficiency")));
            binding.psuInf1.setVisibility(View.VISIBLE);
            binding.psuInf2.setText("Мощность: " + String.valueOf(partData.get("wattage")));
            binding.psuInf2.setVisibility(View.VISIBLE);
            initButtons(binding.psuCard, partRef, PartInfActivity.class, "PSU");
        }
        if (partRef.contains("Cases")) {
            binding.addCase.setText("Корпус");
            binding.caseName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.caseImage);

            binding.casePrice.setText(String.valueOf(partData.get("price")));
            binding.casePrice.setVisibility(View.VISIBLE);

            binding.caseInf1.setText("Форм-фактор: " + String.valueOf(partData.get("form")));
            binding.caseInf1.setVisibility(View.VISIBLE);
            binding.caseInf2.setText("Совместимость: " + String.valueOf(partData.get("maxform")));
            binding.caseInf2.setVisibility(View.VISIBLE);
            initButtons(binding.caseCard, partRef, PartInfActivity.class, "Cases");
        }
        if (partRef.contains("Cooling")) {
            binding.addCooling.setText("Кулер");
            binding.coolingName.setText(partName);
            String[] image = partData.get("image").toString().split(",");
            Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.coolingImage);

            binding.coolingPrice.setText(String.valueOf(partData.get("price")));
            binding.coolingPrice.setVisibility(View.VISIBLE);

            binding.coolingInf1.setText("Рассеиваемая мощность: " + String.valueOf(partData.get("tdp")));
            binding.coolingInf1.setVisibility(View.VISIBLE);
            binding.coolingInf2.setText("Высота: " + String.valueOf(partData.get("width")));
            binding.coolingInf2.setVisibility(View.VISIBLE);
            initButtons(binding.coolingCard, partRef, PartInfActivity.class, "Cooling");
        }
    }

    private void initButtons(MaterialCardView card, String partref, Class cls, String parttype) {
        card.setOnClickListener(v -> {
            Intent intent = new Intent(this, cls);
            intent.putExtra("PART", parttype);
            intent.putExtra("DOC", docId);
            intent.putExtra("REF", partref);
            startActivity(intent);
            finish();
        });
    }
    private void setTotalPrice(int totalPrice) {
        db.collection(auth.getCurrentUser().getUid())
                .document(docId)
                .update("Cost", totalPrice);
    }

    private void deleteFromFirestore() {
        DocumentReference collectionRef = db.collection(auth.getCurrentUser().getUid()).document(docId);

        collectionRef.delete().addOnSuccessListener(task -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("INTENT", "back");
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.build_top_app_bar, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsItem = menu.findItem(R.id.settings_item);
        settingsItem.setVisible(false);
        MenuItem internetItem = menu.findItem(R.id.delete_item);
        internetItem.setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.delete_item) {
            deleteFromFirestore();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}