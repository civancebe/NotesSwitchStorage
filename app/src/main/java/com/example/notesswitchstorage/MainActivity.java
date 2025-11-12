package com.example.notesswitchstorage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.notesswitchstorage.model.Note;
import com.example.notesswitchstorage.storage.NoteStorage;
import com.example.notesswitchstorage.storage.StorageProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private final List<String> noteNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listNotes);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noteNames);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
        setTitle(getString(R.string.app_name) + " (" + StorageProvider.currentName(this) + ")");
    }

    private void refreshList() {
        NoteStorage storage = StorageProvider.get(this);
        List<Note> notes = storage.listNotes(this);
        noteNames.clear();
        for (Note n : notes) noteNames.add(n.getName());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            startActivity(new Intent(this, AddNoteActivity.class));
            return true;
        } else if (id == R.id.action_delete) {
            startActivity(new Intent(this, DeleteNoteActivity.class));
            return true;
        } else if (id == R.id.action_use_prefs) {
            StorageProvider.setUseSharedPrefs(this);
            Toast.makeText(this, getString(R.string.switched_to_prefs), Toast.LENGTH_SHORT).show();
            setTitle(getString(R.string.app_name) + " (" + StorageProvider.currentName(this) + ")");
            return true;
        } else if (id == R.id.action_use_files) {
            StorageProvider.setUseFiles(this);
            Toast.makeText(this, getString(R.string.switched_to_files), Toast.LENGTH_SHORT).show();
            setTitle(getString(R.string.app_name) + " (" + StorageProvider.currentName(this) + ")");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
