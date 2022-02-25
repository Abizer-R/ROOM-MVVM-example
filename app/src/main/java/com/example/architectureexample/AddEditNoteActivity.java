package com.example.architectureexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.net.IDN;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = AddEditNoteActivity.class.getSimpleName() + ".EXTRA_ID";
    public static final String EXTRA_TITLE = AddEditNoteActivity.class.getSimpleName() + ".EXTRA_TITLE";
    public static final String EXTRA_DESC = AddEditNoteActivity.class.getSimpleName() + ".EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = AddEditNoteActivity.class.getSimpleName() + ".EXTRA_PRIORITY";

    private EditText editTextTitle;
    private EditText editTextDesc;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        editTextTitle = findViewById(R.id.add_note_title);
        editTextDesc = findViewById(R.id.add_note_desc);
        numberPicker = findViewById(R.id.number_picker_priority);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        if(getIntent().hasExtra(EXTRA_ID)) {

            getSupportActionBar().setTitle("Edit Note");

            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDesc.setText(getIntent().getStringExtra(EXTRA_DESC));
            numberPicker.setValue(getIntent().getIntExtra(EXTRA_PRIORITY, 1));

        } else {
            getSupportActionBar().setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote() {

        String title = editTextTitle.getText().toString();
        String desc = editTextDesc.getText().toString();
        int priority = numberPicker.getValue();

        if(title.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        } else if (desc.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, desc);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}