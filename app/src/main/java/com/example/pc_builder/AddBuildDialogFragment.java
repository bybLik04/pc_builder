package com.example.pc_builder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddBuildDialogFragment extends DialogFragment {
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    public interface AddBuildDialogListener {
        void onBuildCreated(String buildName, String notificationTitle);
    }
    public AddBuildDialogFragment(FirebaseAuth auth, FirebaseFirestore db) {
        this.auth = auth;
        this.db = db;
    }
    private AddBuildDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_App_MaterialAlertDialog);
        dialogBuilder.setTitle(R.string.dialog_title);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_build, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton(R.string.dialog_add, (dialog, which) -> {
            EditText buildNameEditText = dialogView.findViewById(R.id.newBuildTxt);
            String title = buildNameEditText.getText().toString();
            Log.d("NAMETITLE", title);
            if (mListener != null) {
                if (title.isEmpty()) {
                    mListener.onBuildCreated(title,"Введите название!");
                } else {
                    Map<String, Object> newBuild = new HashMap<>();
                    newBuild.put("CPU", null);
                    newBuild.put("GPU", null);
                    newBuild.put("Cost", 0);
                    newBuild.put("Cases", null);
                    newBuild.put("Motherboard", null);
                    newBuild.put("RAM", null);
                    newBuild.put("Storage", null);
                    newBuild.put("PSU", null);
                    newBuild.put("Cooling", null);

                    db.collection(auth.getCurrentUser().getUid())
                            .document(title)
                            .set(newBuild).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    mListener.onBuildCreated(title,"Сборка создана успешно!");
                                }
                            })
                            .addOnFailureListener(e -> {
                                mListener.onBuildCreated(title,"Проблема при создании сборки");
                            });
                }
            }
        });
        dialogBuilder.setNegativeButton(R.string.dialog_cancel, null);

        return dialogBuilder.create();
    }

    public void setListener(AddBuildDialogListener listener) {
        mListener = listener;
    }
}
