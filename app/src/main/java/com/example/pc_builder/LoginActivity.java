package com.example.pc_builder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pc_builder.databinding.ActivityLoginBinding;
import com.example.pc_builder.databinding.ActivitySplashScreenBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();

        binding.googleButton.setOnClickListener(v -> {
            signInWithGoogle();
        });
        binding.signinButton.setOnClickListener(v -> {
            firebaseAuthWithEmail();
        });
        binding.regButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("SignInActivity", "Google sign in failed", e);
                Toast.makeText(this, getString(R.string.google_signin_fail), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("Uid", uid);
                            editor.apply();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_succes), Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("SignInActivity", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void firebaseAuthWithEmail(){
        String email = binding.emailTextInput.getText().toString();
        String password = binding.passTextInput.getText().toString();
        
        if (email.isEmpty() && password.isEmpty()){
            binding.emailLayout.setError(getString(R.string.label_empty_all));
            binding.passLayout.setError(" ");
        } else if (email.isEmpty() || password.isEmpty()){
        if (email.isEmpty()){
            binding.emailLayout.setError(getString(R.string.label_empty));
            binding.passLayout.setError(null);
        } else {
            binding.passLayout.setError(getString(R.string.label_empty));
            binding.emailLayout.setError(null);
        }
        } else {
            binding.emailLayout.setError(null);
            binding.passLayout.setError(null);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("SignInActivity", "signInWithEmail:success");
                                String uid = mAuth.getCurrentUser().getUid();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Uid", uid);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                Log.w("SignInActivity", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                binding.emailLayout.setError(" ");
                                binding.passLayout.setError(getString(R.string.label_invalid));
                            }
                        }
                    });
        }
    }
}