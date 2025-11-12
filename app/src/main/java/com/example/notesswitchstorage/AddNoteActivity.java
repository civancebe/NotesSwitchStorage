package com.example.notesswitchstorage;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesswitchstorage.model.Note;
import com.example.notesswitchstorage.storage.NoteStorage;
import com.example.notesswitchstorage.storage.StorageProvider;

public class AddNoteActivity extends AppCompatActivity {
    private EditText etName, etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        etName = findViewById(R.id.etNoteName);
        etContent = findViewById(R.id.etNoteContent);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String content = etContent.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(content)) {
                    Toast.makeText(AddNoteActivity.this, getString(R.string.validation_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                NoteStorage storage = StorageProvider.get(AddNoteActivity.this);
                boolean ok = storage.saveNote(AddNoteActivity.this, new Note(name, content));
                if (ok) {
                    Toast.makeText(AddNoteActivity.this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddNoteActivity.this, getString(R.string.save_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
