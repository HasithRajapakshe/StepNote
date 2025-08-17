package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sessionManager = new SessionManager(getApplicationContext());

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);
        Button signInButton = findViewById(R.id.signInButton);
        TextView signUpText = findViewById(R.id.signUpText);

        signInButton.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        signUpText.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });
    }

    private void loginUser(String email, String password) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            User user = db.userDao().findByEmailAndPassword(email, password);
            if (user != null) {
                // Login successful, save the session
                sessionManager.createLoginSession(user.name, user.email);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
    