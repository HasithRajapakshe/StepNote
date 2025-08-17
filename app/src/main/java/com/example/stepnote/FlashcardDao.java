package com.example.stepnote;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface FlashcardDao {

    @Insert
    void insert(Flashcard flashcard);

    @Query("SELECT * FROM flashcards")
    List<Flashcard> getAll();
}
    