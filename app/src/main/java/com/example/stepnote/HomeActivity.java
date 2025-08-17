package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(getApplicationContext());

        // --- Find all the views ---
        TextView welcomeTextView = findViewById(R.id.welcomeTextView);
        RelativeLayout flashcardsOption = findViewById(R.id.flashcardsOption);
        RelativeLayout audioNotesOption = findViewById(R.id.audioNotesOption);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        TextView dailyStepsCount = findViewById(R.id.dailyStepsCount);
        ProgressBar stepsProgressBar = findViewById(R.id.stepsProgressBar);

        // --- Set up the UI ---
        String userName = sessionManager.getUserName();
        welcomeTextView.setText("Welcome back, " + userName);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        // --- Load and display the step count ---
        SharedPreferences prefs = getSharedPreferences(AudioPlayerActivity.STEP_PREFS, MODE_PRIVATE);
        int steps = prefs.getInt(AudioPlayerActivity.STEP_COUNT_KEY, 0);
        dailyStepsCount.setText(String.valueOf(steps));
        // Update progress bar (assuming a goal of 10,000 steps)
        int progress = (int) ((steps / 10000.0) * 100);
        stepsProgressBar.setProgress(progress);


        // --- Set up Click Listeners ---
        flashcardsOption.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, FlashcardsActivity.class));
            finish();
        });

        audioNotesOption.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AudioNotesActivity.class));
            finish();
        });

        // --- Set up the Bottom Navigation Logic ---
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
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
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }
}
