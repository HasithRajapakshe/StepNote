package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextEmail = findViewById(R.id.editTextEmailSignUp);
        EditText editTextPassword = findViewById(R.id.editTextPasswordSignUp);
        Button signUpButton = findViewById(R.id.signUpButton);
        ImageView backArrow = findViewById(R.id.backArrow);

        backArrow.setOnClickListener(v -> finish());

        signUpButton.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                registerUser(name, email, password);
            }
        });
    }

    private void registerUser(String name, String email, String password) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        // Database operations MUST be done on a background thread.
        new Thread(() -> {
            if (db.userDao().findByEmail(email) != null) {
                runOnUiThread(() -> Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show());
                return;
            }
            User user = new User();
            user.name = name;
            user.email = email;
            user.password = password; // NOTE: In a real app, always hash passwords!
            db.userDao().insert(user);

            runOnUiThread(() -> {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            });
        }).start();
    }
}
    