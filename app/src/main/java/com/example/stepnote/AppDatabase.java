package com.example.stepnote;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// 1. Add AudioNote.class to the entities array.
// 2. IMPORTANT: Increment the version number to 3.
@Database(entities = {Flashcard.class, User.class, AudioNote.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    // 3. Add an abstract method for the AudioNoteDao.
    public abstract FlashcardDao flashcardDao();
    public abstract UserDao userDao();
    public abstract AudioNoteDao audioNoteDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "stepnote_database")
                            // This tells Room how to handle version upgrades.
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
    