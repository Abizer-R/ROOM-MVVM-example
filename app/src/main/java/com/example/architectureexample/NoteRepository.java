package com.example.architectureexample;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    /**
     * We pass an "Application" instead of context
     * ecause, in our viewModel will be passed an "Application" and not context.
     * Also, application is a subclass of context, we can use it to create our Database instance
      */
    public NoteRepository(Application application) {

        NoteDatabase database = NoteDatabase.getInstance(application);

        // These ate the abstract methods whose body is autogenerated by ROOM
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note) {
        new InsertDbAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateDbAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteDbAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNodes() {
        new DeleteAllDbAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    /**
     * These AsyncTask classes have to be static so that they doesn't have the
     * reference to the repository itself....
     * Otherwise, it would cause memory leak
     */
    public static class InsertDbAsyncTask extends AsyncTask<Note, Void, Void> {

        // Since the class is "static".
        // We won't be able to use noteDao of this repository directly
        // So, we pass it over a contructor
        private NoteDao noteDao;

        private InsertDbAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public static class DeleteDbAsyncTask extends AsyncTask<Note, Void, Void> {

        // Since the class is "static".
        // We won't be able to use noteDao of this repository directly
        // So, we pass it over a contructor
        private NoteDao noteDao;

        private DeleteDbAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public static class UpdateDbAsyncTask extends AsyncTask<Note, Void, Void> {

        // Since the class is "static".
        // We won't be able to use noteDao of this repository directly
        // So, we pass it over a contructor
        private NoteDao noteDao;

        private UpdateDbAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public static class DeleteAllDbAsyncTask extends AsyncTask<Void, Void, Void> {

        // Since the class is "static".
        // We won't be able to use noteDao of this repository directly
        // So, we pass it over a contructor
        private NoteDao noteDao;

        private DeleteAllDbAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
}
