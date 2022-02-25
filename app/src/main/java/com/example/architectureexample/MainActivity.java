package com.example.architectureexample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;

    /**
     * This will add a new Note in the database after AddEditNoteActivity is returned
     */
    ActivityResultLauncher<Intent> AddNoteActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();

                        if(data != null) {

                            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC);
                            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

                            Note newNote = new Note(title, desc, priority);
                            noteViewModel.insert(newNote);

                            Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Note not saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    /**
     * This will update a Note in the database after AddEditNoteActivity is returned
     */
    ActivityResultLauncher<Intent> EditNoteActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();

                        int note_id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
                        if(note_id == -1) {
                            // Just to make sure
                            Toast.makeText(MainActivity.this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                        }

                        String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                        String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC);
                        int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

                        Note note = new Note(title, desc, priority);
                        note.setId(note_id);

                        noteViewModel.update(note);

                        Toast.makeText(MainActivity.this, "Note updated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton btnAddNote = findViewById(R.id.btn_add_note);
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);

                AddNoteActivityResultLauncher.launch(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                /**
                 * Instead of this, we will use a method of ListAdapter called submitList
                 */
//                noteAdapter.setNotes(notes);
                noteAdapter.submitList(notes);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        int position = viewHolder.getAdapterPosition();
                        Note currNote = noteAdapter.getNoteAt(position);

                        noteViewModel.delete(currNote);

                        Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        itemTouchHelper.attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {

                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESC, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());

                EditNoteActivityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_option_delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}