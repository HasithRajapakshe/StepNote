package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AudioPlayerActivity extends AppCompatActivity implements SensorEventListener {

    // Sensor variables
    private SensorManager sensorManager;
    private Sensor stepDetectorSensor;
    private boolean isSensorActive = false;

    // MediaPlayer variables
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    // UI variables
    private TextView audioTitleTextView;
    private ImageView playPauseButton; // You'll need to add an ID for this

    // Handler to detect when the user stops walking
    private Handler stopHandler = new Handler();
    private Runnable stopRunnable;
    private static final long STOP_DELAY_MS = 3000; // 3 seconds of no steps = stopped

    // SharedPreferences for saving steps
    public static final String STEP_PREFS = "StepPrefs";
    public static final String STEP_COUNT_KEY = "DailyStepCount";
    private int sessionStepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        ImageView closeButton = findViewById(R.id.closeButton);
        audioTitleTextView = findViewById(R.id.audioTitleText); // Add this ID to your XML
        playPauseButton = findViewById(R.id.playPauseButton); // Add this ID to your XML

        // Get the audio file path from the intent
        String filePath = getIntent().getStringExtra("AUDIO_FILE_PATH");
        String fileTitle = getIntent().getStringExtra("AUDIO_FILE_TITLE");
        audioTitleTextView.setText(fileTitle);

        // Initialize the SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null) {
            stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            isSensorActive = true;
        } else {
            Toast.makeText(this, "Step detector sensor not available!", Toast.LENGTH_SHORT).show();
            isSensorActive = false;
        }

        // Initialize the MediaPlayer
        setupMediaPlayer(filePath);

        closeButton.setOnClickListener(v -> finish());

        playPauseButton.setOnClickListener(v -> {
            if (isPlaying) {
                pauseAudio();
            } else {
                // Manually start playing if user presses play
                playAudio();
            }
        });
    }

    private void setupMediaPlayer(String filePath) {
        if (filePath == null) {
            Toast.makeText(this, "Invalid audio file", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, Uri.parse(filePath));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not play audio file", Toast.LENGTH_SHORT).show();
        }
    }

    private void playAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.ic_pause_circle); // You need this icon
            startStepDetection();
        }
    }

    private void pauseAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            playPauseButton.setImageResource(R.drawable.ic_play_circle);
            stopStepDetection();
        }
    }

    private void startStepDetection() {
        if (isSensorActive) {
            sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            // Start the timer to check if the user has stopped
            resetStopTimer();
        }
    }

    private void stopStepDetection() {
        if (isSensorActive) {
            sensorManager.unregisterListener(this);
            // Stop the timer
            stopHandler.removeCallbacks(stopRunnable);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // A step was detected!
            sessionStepCount++;
            // If audio was paused because they stopped, resume it
            if (!isPlaying) {
                playAudio();
            }
            // Reset the timer since a step was just taken
            resetStopTimer();
        }
    }

    private void resetStopTimer() {
        // Remove any old timer callbacks
        if (stopRunnable != null) {
            stopHandler.removeCallbacks(stopRunnable);
        }
        // Create a new timer
        stopRunnable = () -> {
            // This code runs after 3 seconds of no steps
            if (isPlaying) {
                pauseAudio();
                Toast.makeText(this, "Paused - Start walking to resume!", Toast.LENGTH_SHORT).show();
            }
        };
        // Start the timer
        stopHandler.postDelayed(stopRunnable, STOP_DELAY_MS);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed for this implementation
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause audio and stop listening for steps when the app is paused
        pauseAudio();
        saveSteps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        saveSteps();
    }

    private void saveSteps() {
        SharedPreferences prefs = getSharedPreferences(STEP_PREFS, MODE_PRIVATE);
        int currentSteps = prefs.getInt(STEP_COUNT_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(STEP_COUNT_KEY, currentSteps + sessionStepCount);
        editor.apply();
        sessionStepCount = 0; // Reset for next session
    }
}
