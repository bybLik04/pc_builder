package com.example.pc_builder;



import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BuildsAdapter extends RecyclerView.Adapter<BuildsAdapter.ViewHolder> {

    private List<Builds> builds;
    private Context mContext;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private OnPartLoadedListener onPartLoadedListener;
    public BuildsAdapter(List<Builds> builds, Context Context, FirebaseFirestore db, FirebaseAuth auth) {
        this.db = db;
        this.auth = auth;
        this.builds = builds;
        this.mContext = Context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.builds_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildsAdapter.ViewHolder holder, int position) {
        Builds build = builds.get(position);

        holder.title.setText(build.getTitle());

        String totalCost = String.valueOf(build.getCost()) + mContext.getString(R.string.ruble);
        holder.cost.setText(totalCost);
        Log.d("TAG", "TITLE: " + build.getTitle() + " Cpu: " + build.getCpu() + " Gpu: " + build.getGpu() + " Cost: " + build.getCost() + totalCost);
        if (build.getCpu() == null) {
            holder.cpu.setText(R.string.empty_cpu);
        } else {
            loadPartsFromFirestore(build.getCpu(), new OnPartLoadedListener() {
                @Override
                public void onPartLoaded(String partName) {
                    holder.cpu.setText(partName);
                }
            });
        }
        if (build.getGpu() == null) {
            holder.cpu.setText(R.string.empty_cpu);
        } else {
            loadPartsFromFirestore(build.getGpu(), new OnPartLoadedListener() {
                @Override
                public void onPartLoaded(String partName) {
                    holder.gpu.setText(partName);
                }
            });
        }

        holder.card.setOnClickListener(view -> {
            if (position != RecyclerView.NO_POSITION) {
                Builds clickedBuilds = builds.get(position);
                Intent intent = new Intent(mContext, BuildInfActivity.class);
                intent.putExtra("TITLE", clickedBuilds.getTitle());
                //intent.putExtra("LESSON_LIST", )
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return builds.size();
    }

    private void loadPartsFromFirestore(DocumentReference part, OnPartLoadedListener listener) {
        if (part != null) {
            part.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String partName = null;
                    if (document != null) {
                        partName = document.getId();
                        Log.d("TAG", document.getId() + " => " + document.getData());
                    }
                    listener.onPartLoaded(partName);
                }
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView cost;
        TextView cpu;
        TextView gpu;
        MaterialCardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            cost = itemView.findViewById(R.id.costTxt);
            cpu = itemView.findViewById(R.id.inf1Txt);
            gpu = itemView.findViewById(R.id.inf2Txt);
            card = itemView.findViewById(R.id.build_item);
        }
    }
    public interface OnPartLoadedListener {
        void onPartLoaded(String partName);
    }
}
