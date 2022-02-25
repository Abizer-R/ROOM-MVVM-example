package com.example.architectureexample;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    /**
     * We create this variable because we want to turn this class into a singleton.
     * SINGLETON = A class that has only one instance and provides a global point of access to it
      */
    private static NoteDatabase instance;

    // No need to provide body, because room takes of all the code
    public abstract NoteDao noteDao();

    /**
     * Creating our database now.
     * "Synchronized" is used because it prevents multiple threads to use it
     * at the same time, which prevents accidentally creating 2 instances of this database
     */
    public static synchronized NoteDatabase getInstance(Context context) {

        // We only want to instantiate this instance, if we don't already have one
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,     /* Name of this class */
                    "note_database"    /* Name that we want this file to have */)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            this.noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 1));
            noteDao.insert(new Note("Title 2", "Description 2", 2));
            noteDao.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }
}
