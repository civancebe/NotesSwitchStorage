package com.example.notesswitchstorage.storage;

import android.content.Context;

import com.example.notesswitchstorage.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Stores notes in internal storage as a single JSON file: notes.json
public class FileNoteStorage implements NoteStorage {
    private static final String FILE_NAME = "notes.json";

    @Override
    public List<Note> listNotes(Context ctx) {
        String json = readAll(ctx);
        if (json == null || json.isEmpty()) json = "{\"notes\":[]}";
        List<Note> out = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json);
            JSONArray arr = root.optJSONArray("notes");
            if (arr != null) {
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    out.add(new Note(o.getString("name"), o.optString("content", "")));
                }
            }
        } catch (JSONException ignored) {}
        return out;
    }

    @Override
    public boolean saveNote(Context ctx, Note note) {
        List<Note> existing = listNotes(ctx);
        boolean found = false;
        for (int i = 0; i < existing.size(); i++) {
            if (existing.get(i).getName().equals(note.getName())) {
                existing.set(i, note);
                found = true;
                break;
            }
        }
        if (!found) existing.add(note);
        return writeAll(ctx, existing);
    }

    @Override
    public boolean deleteNote(Context ctx, String name) {
        List<Note> existing = listNotes(ctx);
        existing.removeIf(n -> n.getName().equals(name));
        return writeAll(ctx, existing);
    }

    private boolean writeAll(Context ctx, List<Note> notes) {
        JSONArray arr = new JSONArray();
        try {
            for (Note n : notes) {
                JSONObject o = new JSONObject();
                o.put("name", n.getName());
                o.put("content", n.getContent());
                arr.put(o);
            }
            JSONObject root = new JSONObject();
            root.put("notes", arr);
            try (FileOutputStream fos = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)) {
                fos.write(root.toString().getBytes());
            }
            return true;
        } catch (JSONException | IOException e) {
            return false;
        }
    }

    private String readAll(Context ctx) {
        try (FileInputStream fis = ctx.openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    @Override public String getDisplayName() { return "Files"; }
}
