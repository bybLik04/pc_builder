package com.example.pc_builder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
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
    private String part;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPartInfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.partInfAppBar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        docId = getIntent().getStringExtra("DOC");
        partType = getIntent().getStringExtra("PART");
        String intentType = getIntent().getStringExtra("INTENT");

        if (intentType == null) {
            intentType = "";
        }
        if (intentType.equals("add")) {
            partRef = getIntent().getStringExtra("REF") +"/"+ getIntent().getStringExtra("TITLE");

            binding.partInfAppBar.setNavigationOnClickListener(v -> {
                finish();
            });
            binding.extendedFab.setText(R.string.fab_upd);
            binding.extendedFab.setOnClickListener(v -> {
                updatePartFromBuild(db.document(partRef));
            });
        } else {
            partRef = getIntent().getStringExtra("REF");

            binding.partInfAppBar.setNavigationOnClickListener(v -> {
                Intent intent = new Intent(this, BuildInfActivity.class);
                intent.putExtra("TITLE", docId);
                startActivity(intent);
                finish();
            });
            binding.extendedFab.setOnClickListener(v -> {
                updatePartFromBuild(null);
            });
        }
        loadPartInfFromFirestore(partRef);
    }

    private void initViews(Map<String, Object> partData, String partName) {
        String[] image = partData.get("image").toString().split(",");
        Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(binding.partImage);
        binding.partName.setText(partName);
        part = partName;
        if(partRef.contains("CPU")) {
            binding.partInf1Title.setText("Ядра / Потоки");
            binding.partInf1.setText(String.valueOf(partData.get("inf2")) + " / " + String.valueOf(partData.get("inf3")));
            binding.partInf2Title.setText("Частота процессора");
            binding.partInf2.setText(String.valueOf(partData.get("inf1")));
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
        if (partRef.contains("GPU")) {
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
        if (partRef.contains("RAM")) {
            binding.partInf1Title.setText("Объем памяти");
            binding.partInf1.setText(String.valueOf(partData.get("size")));
            binding.partInf2Title.setText("Тип памяти");
            binding.partInf2.setText(String.valueOf(partData.get("type")));
            binding.partInf3Title.setText("Частота памяти");
            binding.partInf3.setText(String.valueOf(partData.get("clock")));
            binding.partInf4Title.setText("Количество плашек");
            binding.partInf4.setText(String.valueOf(partData.get("DIMMs")));
            binding.partInf5Title.setText("CAS");
            binding.partInf5.setText(String.valueOf(partData.get("CAS")));
            binding.partInf6Title.setVisibility(View.GONE);
            binding.partInf6.setVisibility(View.GONE);
            binding.partInf7Title.setVisibility(View.GONE);
            binding.partInf7.setVisibility(View.GONE);
        }
        if (partRef.contains("Storage")) {
            binding.partInf1Title.setText("Форм-фактор");
            binding.partInf1.setText(String.valueOf(partData.get("form")));
            binding.partInf2Title.setText("Объем");
            binding.partInf2.setText(String.valueOf(partData.get("size")));
            binding.partInf3Title.setText("Максимальная скорость");
            binding.partInf3.setText(String.valueOf(partData.get("speed")));
            binding.partInf4Title.setText("Тип");
            binding.partInf4.setText(String.valueOf(partData.get("type")));
            binding.partInf5Title.setVisibility(View.GONE);
            binding.partInf5.setVisibility(View.GONE);
            binding.partInf6Title.setVisibility(View.GONE);
            binding.partInf6.setVisibility(View.GONE);
            binding.partInf7Title.setVisibility(View.GONE);
            binding.partInf7.setVisibility(View.GONE);
        }
        if (partRef.contains("PSU")) {
            binding.partInf1Title.setText("Сертификат");
            binding.partInf1.setText(String.valueOf(partData.get("efficiency")));
            binding.partInf2Title.setText("Форм-фактор");
            binding.partInf2.setText(String.valueOf(partData.get("form")));
            binding.partInf3Title.setText("Модульный");
            binding.partInf3.setText(String.valueOf(partData.get("modular")));
            binding.partInf4Title.setText("Мощность");
            binding.partInf4.setText(String.valueOf(partData.get("wattage")));
            binding.partInf5Title.setVisibility(View.GONE);
            binding.partInf5.setVisibility(View.GONE);
            binding.partInf6Title.setVisibility(View.GONE);
            binding.partInf6.setVisibility(View.GONE);
            binding.partInf7Title.setVisibility(View.GONE);
            binding.partInf7.setVisibility(View.GONE);
        }
        if (partRef.contains("Cooling")) {
            binding.partInf1Title.setText("Совместимые сокеты");
            binding.partInf1.setText(String.valueOf(partData.get("socket")));
            binding.partInf2Title.setText("Обороты в минуту");
            binding.partInf2.setText(String.valueOf(partData.get("rpm")));
            binding.partInf3Title.setText("Рассеиваемая мощность");
            binding.partInf3.setText(String.valueOf(partData.get("tdp")));
            binding.partInf4Title.setText("Высота");
            binding.partInf4.setText(String.valueOf(partData.get("width")));
            binding.partInf5Title.setVisibility(View.GONE);
            binding.partInf5.setVisibility(View.GONE);
            binding.partInf6Title.setVisibility(View.GONE);
            binding.partInf6.setVisibility(View.GONE);
            binding.partInf7Title.setVisibility(View.GONE);
            binding.partInf7.setVisibility(View.GONE);
        }
        if (partRef.contains("Cases")) {
            binding.partInf1Title.setText("Форм-фактор");
            binding.partInf1.setText(String.valueOf(partData.get("form")));
            binding.partInf2Title.setText("Совместимые платы");
            binding.partInf2.setText(String.valueOf(partData.get("maxform")));
            binding.partInf3Title.setText("Максимальная ширина видеокарты");
            binding.partInf3.setText(String.valueOf(partData.get("maxw")));
            binding.partInf4Title.setText("Максимальная высота охлаждения ЦПУ");
            binding.partInf4.setText(String.valueOf(partData.get("maxh")));
            binding.partInf5Title.setText("Оконная дверца");
            binding.partInf5.setText(String.valueOf(partData.get("window")));
            binding.partInf6Title.setVisibility(View.GONE);
            binding.partInf6.setVisibility(View.GONE);
            binding.partInf7Title.setVisibility(View.GONE);
            binding.partInf7.setVisibility(View.GONE);
        }
        if (partRef.contains("Motherboard")) {
            binding.partInf1Title.setText("Чипсет");
            binding.partInf1.setText(String.valueOf(partData.get("chipset")));
            binding.partInf2Title.setText("Сокет");
            binding.partInf2.setText(String.valueOf(partData.get("socket")));
            binding.partInf3Title.setText("Тип ОЗУ");
            binding.partInf3.setText(String.valueOf(partData.get("RAMT")));
            binding.partInf4Title.setText("Максимальный объем ОЗУ");
            binding.partInf4.setText(String.valueOf(partData.get("RAMM")));
            binding.partInf5Title.setText("Количество слотов ОЗУ");
            binding.partInf5.setText(String.valueOf(partData.get("RAMS")));
            binding.partInf6Title.setText("Максимальная частота ОЗУ");
            binding.partInf6.setText(String.valueOf(partData.get("RAMC")));
            binding.partInf7Title.setText("Форм-фактор");
            binding.partInf7.setText(String.valueOf(partData.get("form")));
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
    private void updatePartFromBuild(DocumentReference part) {
        db.collection(auth.getCurrentUser().getUid())
                .document(docId)
                .update(partType, part)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, BuildInfActivity.class);
                        intent.putExtra("TITLE", docId);
                        startActivity(intent);
                        finish();
                    }
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
        MenuItem internetItem = menu.findItem(R.id.internet_item);
        internetItem.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.internet_item) {
            Uri uri = Uri.parse("https://www.google.com/search?q=" + part);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}