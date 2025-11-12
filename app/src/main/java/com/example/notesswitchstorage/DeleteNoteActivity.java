package com.example.notesswitchstorage;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesswitchstorage.model.Note;
import com.example.notesswitchstorage.storage.NoteStorage;
import com.example.notesswitchstorage.storage.StorageProvider;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoteActivity extends AppCompatActivity {
    private ListView listView;
    private Button btnDelete;
    private ArrayAdapter<String> adapter;
    private List<String> names = new ArrayList<>();
    private int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);
        listView = findViewById(R.id.listDelete);
        btnDelete = findViewById(R.id.btnDelete);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, names);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener((parent, view, position, id) -> selected = position);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (selected < 0 || selected >= names.size()) {
                    Toast.makeText(DeleteNoteActivity.this, getString(R.string.validation_select_note), Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = names.get(selected);
                NoteStorage storage = StorageProvider.get(DeleteNoteActivity.this);
                boolean ok = storage.deleteNote(DeleteNoteActivity.this, name);
                if (ok) {
                    Toast.makeText(DeleteNoteActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    refresh();
                } else {
                    Toast.makeText(DeleteNoteActivity.this, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override protected void onResume() { super.onResume(); refresh(); }

    private void refresh() {
        NoteStorage storage = StorageProvider.get(this);
        List<Note> notes = storage.listNotes(this);
        names.clear();
        for (Note n : notes) names.add(n.getName());
        selected = -1;
        adapter.notifyDataSetChanged();
        listView.clearChoices();
    }
}
