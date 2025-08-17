package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FlashcardsActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards);

        findViews();
        loadAnimations();
        changeCameraDistance();

        Button addFlashcardButton = findViewById(R.id.addFlashcardButton);
        FrameLayout cardContainer = findViewById(R.id.card_container);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_flashcards);

        bottomNavigationView.setSelectedItemId(R.id.navigation_flashcards);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_flashcards) {
                return true; // Already here
            } else if (itemId == R.id.navigation_audio) {
                // This is the single, correct block for the audio button
                startActivity(new Intent(getApplicationContext(), AudioNotesActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                return true;
            }else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.navigation_audio) {
            startActivity(new Intent(getApplicationContext(), AudioNotesActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        } else if (itemId == R.id.navigation_profile) {
            // Add this logic
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return true;
        }
            return false;
        });

        addFlashcardButton.setOnClickListener(v -> {
            startActivity(new Intent(FlashcardsActivity.this, AddFlashcardActivity.class));
        });

        cardContainer.setOnClickListener(v -> flipCard());
    }

    private void findViews() {
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    public void flipCard() {
        if (!mIsBackVisible) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mCardBackLayout.setVisibility(View.VISIBLE);
            mCardFrontLayout.setVisibility(View.GONE);
            mIsBackVisible = true;

        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);
            mSetRightOut.start();
            mSetLeftIn.start();
            mCardFrontLayout.setVisibility(View.VISIBLE);
            mCardBackLayout.setVisibility(View.GONE);
            mIsBackVisible = false;
        }
    }
}
