package com.example.stepnote;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "audio_notes")
public class AudioNote {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    // We will store the location of the audio file as a String
    @ColumnInfo(name = "file_path")
    public String filePath;
}
    