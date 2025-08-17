package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private TextView profileName, profileUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(getApplicationContext());

        profileName = findViewById(R.id.profile_name);
        profileUsername = findViewById(R.id.profile_username);

        // Load user data from the session
        profileName.setText(sessionManager.getUserName());
        // Create a simple username from the email
        profileUsername.setText("@" + sessionManager.getUserEmail().split("@")[0]);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profile);
        Button signOutButton = findViewById(R.id.signOutButton);

        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);

        // This is the full navigation logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_flashcards) {
                startActivity(new Intent(getApplicationContext(), FlashcardsActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_audio) {
                startActivity(new Intent(getApplicationContext(), AudioNotesActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                return true; // Already here
            }
            return false;
        });

        signOutButton.setOnClickListener(v -> {
            // Clear the session data
            sessionManager.logoutUser();

            // Navigate back to the SignInActivity
            Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
