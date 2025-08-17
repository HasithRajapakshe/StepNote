package com.example.stepnote;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AudioNoteDao {

    @Insert
    void insert(AudioNote audioNote);

    @Query("SELECT * FROM audio_notes ORDER BY id DESC")
    List<AudioNote> getAll();
}
    