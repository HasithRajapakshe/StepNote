package com.example.stepnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class AudioNotesActivity extends AppCompatActivity {

    private RecyclerView audioNotesRecyclerView;
    private AudioNotesAdapter adapter;
    private List<AudioNote> audioNoteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_notes);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_audio);
        Button addAudioNoteButton = findViewById(R.id.addAudioNoteButton);
        audioNotesRecyclerView = findViewById(R.id.audioNotesRecyclerView);

        bottomNavigationView.setSelectedItemId(R.id.navigation_audio);

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
                return true; // Already here
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });

        addAudioNoteButton.setOnClickListener(v -> {
            startActivity(new Intent(AudioNotesActivity.this, AddAudioNoteActivity.class));
        });

        audioNotesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AudioNotesAdapter(audioNoteList);
        audioNotesRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAudioNotes();
    }

    private void loadAudioNotes() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        new Thread(() -> {
            List<AudioNote> notes = db.audioNoteDao().getAll();
            runOnUiThread(() -> {
                adapter.updateData(notes);
            });
        }).start();
    }

    // --- RecyclerView Adapter ---
    public class AudioNotesAdapter extends RecyclerView.Adapter<AudioNotesAdapter.ViewHolder> {
        private List<AudioNote> localDataSet;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView titleTextView;
            public final TextView durationTextView;

            public ViewHolder(View view) {
                super(view);
                titleTextView = view.findViewById(R.id.audioTitle);
                durationTextView = view.findViewById(R.id.audioDuration);
                view.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        AudioNote note = localDataSet.get(position);
                        Intent intent = new Intent(AudioNotesActivity.this, AudioPlayerActivity.class);
                        intent.putExtra("AUDIO_FILE_PATH", note.filePath);
                        intent.putExtra("AUDIO_FILE_TITLE", note.title);
                        startActivity(intent);
                    }
                });
            }
        }

        public AudioNotesAdapter(List<AudioNote> dataSet) {
            localDataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_item_audio_note, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            AudioNote note = localDataSet.get(position);
            viewHolder.titleTextView.setText(note.title);
            viewHolder.durationTextView.setText("Audio Note"); // Placeholder
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }

        public void updateData(List<AudioNote> newNotes) {
            localDataSet.clear();
            localDataSet.addAll(newNotes);
            notifyDataSetChanged();
        }
    }
}
