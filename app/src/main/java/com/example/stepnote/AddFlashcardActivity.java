package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddFlashcardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        ImageView backArrow = findViewById(R.id.backArrow);
        EditText editTextQuestion = findViewById(R.id.editTextQuestion);
        EditText editTextAnswer = findViewById(R.id.editTextAnswer);
        Button saveFlashcardButton = findViewById(R.id.saveFlashcardButton);

        backArrow.setOnClickListener(v -> finish());

        saveFlashcardButton.setOnClickListener(v -> {
            String question = editTextQuestion.getText().toString();
            String answer = editTextAnswer.getText().toString();

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                saveFlashcard(question, answer);
            }
        });
    }

    private void saveFlashcard(String question, String answer) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        Flashcard flashcard = new Flashcard();
        flashcard.question = question;
        flashcard.answer = answer;

        new Thread(() -> {
            db.flashcardDao().insert(flashcard);
            runOnUiThread(() -> {
                Toast.makeText(this, "Flashcard saved!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }
}
    