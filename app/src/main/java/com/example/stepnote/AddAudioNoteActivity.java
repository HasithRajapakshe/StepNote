package com.example.stepnote;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddAudioNoteActivity extends AppCompatActivity {

    private Uri selectedFileUri;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audio_note);

        // This method sets up the modern way of handling permissions and file picking.
        registerLaunchers();

        // Standard view setup
        ImageView backArrow = findViewById(R.id.backArrow);
        Button saveButton = findViewById(R.id.saveAudioNoteButton);
        ImageView recordButton = findViewById(R.id.recordButton);
        EditText noteTitle = findViewById(R.id.editTextNoteTitle);

        backArrow.setOnClickListener(v -> finish());

        recordButton.setOnClickListener(v -> {
            checkPermissionAndOpenFilePicker();
        });

        saveButton.setOnClickListener(v -> {
            String title = noteTitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            } else if (selectedFileUri == null) {
                Toast.makeText(this, "Please select an audio file", Toast.LENGTH_SHORT).show();
            } else {
                saveAudioNote(title, selectedFileUri.toString());
            }
        });
    }

    /**
     * Initializes the ActivityResultLaunchers for handling permissions and file selection.
     */
    private void registerLaunchers() {
        // Launcher for handling the result of the file picker
        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedFileUri = result.getData().getData();
                        if (selectedFileUri != null) {
                            Toast.makeText(this, "File selected: " + selectedFileUri.getPath(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

        // Launcher for handling the result of the permission request
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
                        openFilePicker();
                    } else {
                        Toast.makeText(this, "Permission is required to select audio files", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Checks for the appropriate storage permission based on the Android version
     * and either opens the file picker or requests permission.
     */
    private void checkPermissionAndOpenFilePicker() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openFilePicker();
        } else {
            requestPermissionLauncher.launch(permission);
        }
    }

    /**
     * Launches an intent to open the system's file browser, filtered for audio files.
     */
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        filePickerLauncher.launch(Intent.createChooser(intent, "Select Audio File"));
    }

    /**
     * Saves the new audio note's title and file path to the Room database.
     * @param title The title entered by the user.
     * @param filePath The string representation of the selected file's URI.
     */
    private void saveAudioNote(String title, String filePath) {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        AudioNote audioNote = new AudioNote();
        audioNote.title = title;
        audioNote.filePath = filePath;

        // Database operations must run on a background thread.
        new Thread(() -> {
            db.audioNoteDao().insert(audioNote);
            runOnUiThread(() -> {
                Toast.makeText(this, "Audio note saved!", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity and return to the list
            });
        }).start();
    }
}
