package com.example.pc_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pc_builder.databinding.ActivityRegistrationBinding;
import com.example.pc_builder.databinding.ActivitySplashScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private ActivityRegistrationBinding binding;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        binding.signupButton.setOnClickListener(v -> {
            firebaseRegWithEmail();
            Log.w("SignInActivity", "signInWithCredential:failure");
        });
    }
    private void firebaseRegWithEmail() {
        String email = binding.emailTextInput.getText().toString();
        String password = binding.passTextInput.getText().toString();
        String password2 = binding.pass2TextInput.getText().toString();

        if (email.isEmpty() || password.isEmpty() || password2.isEmpty()){
            if (email.isEmpty()){
                binding.emailInputLayout.setError(getString(R.string.label_empty));
                binding.passInputLayout.setError(null);
                binding.pass2InputLayout.setError(null);
            } else if (password.isEmpty()) {
                binding.passInputLayout.setError(getString(R.string.label_empty));
                binding.emailInputLayout.setError(null);
                binding.pass2InputLayout.setError(null);
            } else {
                binding.pass2InputLayout.setError(getString(R.string.label_empty));
                binding.emailInputLayout.setError(null);
                binding.passInputLayout.setError(null);
            }
        } else if (!isValidEmail(email)) {
            binding.emailInputLayout.setError(getString(R.string.label_invalid_email));
            binding.passInputLayout.setError(null);
            binding.pass2InputLayout.setError(null);
        } else if (password.length() < 6) {
            binding.passInputLayout.setError(getString(R.string.min_sybols) + "!");
            binding.emailInputLayout.setError(null);
            binding.pass2InputLayout.setError(null);
        } else if (!password2.equals(password)) {
            binding.pass2InputLayout.setError(getString(R.string.label_notmatch));
            binding.emailInputLayout.setError(null);
            binding.passInputLayout.setError(null);
        } else {
            binding.emailInputLayout.setError(null);
            binding.pass2InputLayout.setError(null);
            binding.passInputLayout.setError(null);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("RegActivity", "createUserWithEmail:success");
                                String uid = mAuth.getCurrentUser().getUid();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Uid", uid);
                                editor.apply();
                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                                Toast.makeText(RegistrationActivity.this, getString(R.string.auth_succes), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w("RegActivity", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegistrationActivity.this, getString(R.string.auth_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public void back(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}