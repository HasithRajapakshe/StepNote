package com.example.stepnote;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcards")
public class Flashcard {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "question")
    public String question;

    @ColumnInfo(name = "answer")
    public String answer;
}
    