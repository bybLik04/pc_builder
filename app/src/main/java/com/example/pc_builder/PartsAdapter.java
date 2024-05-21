package com.example.pc_builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class PartsAdapter extends RecyclerView.Adapter<PartsAdapter.ViewHolder> {
    private Context mContext;
    private List<String> parts;
    private String docId;
    private String partPath;
    private FirebaseFirestore db;
    private String partType;
    public PartsAdapter(String docId, String partPath, Context context, FirebaseFirestore db, String partType) {
        this.docId = docId;
        this.partPath = partPath;
        this.mContext = context;
        this.db = db;
        this.partType = partType;
    }
    public List<String> getParts() {
        return parts;
    }
    public void setParts(List<String> parts) {
        this.parts = parts;
    }
    @NonNull
    @Override
    public PartsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.builds_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartsAdapter.ViewHolder holder, int position) {
        String part = getParts().get(position);
        holder.title.setText(part);

        loadPartFromFirestore(part, new OnPartLoadedListener() {
            @Override
            public void onPartLoaded(Map<String, Object> partInfo, String partRef) {

                String price = partInfo.get("price").toString() + " " + mContext.getString(R.string.ruble);
                holder.cost.setText(price);
                String[] image = partInfo.get("image").toString().split(",");
                Picasso.get().load(image[0].replace("[", "").replace("]", "")).into(holder.image);
                if (partRef.contains("CPU")) {
                    holder.inf1.setText(mContext.getString(R.string.cores_threads) + partInfo.get("inf2").toString() + " / " + partInfo.get("inf3").toString());
                    holder.inf2.setText(mContext.getString(R.string.clock) + partInfo.get("inf1").toString());
                } else if (partRef.contains("GPU")) {
                    holder.inf1.setText(mContext.getString(R.string.vram_size) + partInfo.get("VRAMS").toString() + " " + partInfo.get("VRAMT").toString());
                    holder.inf2.setText(mContext.getString(R.string.clock) + partInfo.get("Clock").toString());
                } else if (partRef.contains("RAM")) {
                    holder.inf1.setText(mContext.getString(R.string.clock) + partInfo.get("clock").toString());
                    holder.inf2.setText(mContext.getString(R.string.total_size) + partInfo.get("size").toString() + " " + partInfo.get("type").toString());
                } else if (partRef.contains("Motherboard")) {
                    holder.inf1.setText(mContext.getString(R.string.chipset) + partInfo.get("chipset").toString());
                    holder.inf2.setText(mContext.getString(R.string.form_factor) + partInfo.get("form").toString());
                } else if (partRef.contains("PSU")) {
                    holder.inf1.setText("Сертификат: " + String.valueOf(partInfo.get("efficiency")));
                    holder.inf2.setText("Мощность: " + String.valueOf(partInfo.get("wattage")));
                } else if (partRef.contains("Storage")) {
                    holder.inf1.setText("Объем: " + String.valueOf(partInfo.get("size")));
                    holder.inf2.setText("Тип: " + String.valueOf(partInfo.get("type")));
                } else if (partRef.contains("Cases")) {
                    holder.inf1.setText("Форм-фактор: " + String.valueOf(partInfo.get("form")));
                    holder.inf2.setText("Совместимость: " + String.valueOf(partInfo.get("maxform")));
                } else if (partRef.contains("Cooling")) {
                    holder.inf1.setText("Рассеиваемая мощность: " + String.valueOf(partInfo.get("tdp")));
                    holder.inf2.setText("Высота: " + String.valueOf(partInfo.get("width")));
                }

            }
        });
        holder.card.setOnClickListener(view -> {
            if (position != RecyclerView.NO_POSITION) {
                String clickedPart = getParts().get(position);
                Intent intent = new Intent(mContext, PartInfActivity.class);
                intent.putExtra("INTENT", "add");
                intent.putExtra("TITLE", clickedPart);
                intent.putExtra("DOC", docId);
                intent.putExtra("REF", partPath);
                intent.putExtra("PART", partType);

                if (mContext instanceof Activity) {
                    mContext.startActivity(intent);
                }
            }
        });
        /*loadPartsFromFirestore(part, new OnPartLoadedListener() {
            @Override
            public void onPartLoaded(String partName) {
                holder.inf1.setText(partName);
            }
        });
        loadPartsFromFirestore(part, new OnPartLoadedListener() {
            @Override
            public void onPartLoaded(String partName) {
                holder.inf2.setText(partName);
            }
        });*/
    }
    @Override
    public int getItemCount() {
        if (getParts() == null) {
            return 0;
        } else {
            return getParts().size();
        }
    }
    private void loadPartFromFirestore(String part, OnPartLoadedListener listener) {
        if (part != null) {
            DocumentReference partRef = db.collection(partPath).document(part);
            partRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Map<String, Object> partInfo = document.getData();
                        listener.onPartLoaded(partInfo, partRef.getPath());
                        Log.d("PARTINFO", document.getId() + " => " + document.getData());
                    }
                }
            });
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView cost;
        ImageView image;
        TextView inf1;
        TextView inf2;
        MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            cost = itemView.findViewById(R.id.costTxt);
            image = itemView.findViewById(R.id.image);
            inf1 = itemView.findViewById(R.id.inf1Txt);
            inf2 = itemView.findViewById(R.id.inf2Txt);
            card = itemView.findViewById(R.id.build_item);
        }
    }
    public interface OnPartLoadedListener {
        void onPartLoaded(Map<String, Object> partInfo, String partRef);
    }
}
