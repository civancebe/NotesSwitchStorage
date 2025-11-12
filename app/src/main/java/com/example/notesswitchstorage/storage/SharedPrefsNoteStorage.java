package com.example.notesswitchstorage.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.notesswitchstorage.model.Note;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// Stores everything in one JSON string inside SharedPreferences.
public class SharedPrefsNoteStorage implements NoteStorage {
    private static final String PREFS = "notes_prefs";
    private static final String KEY_JSON = "notes_json";

    @Override
    public List<Note> listNotes(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_JSON, "{\"notes\":[]}");
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
        // replace if same name
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
            SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            sp.edit().putString(KEY_JSON, root.toString()).apply();
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Override public String getDisplayName() { return "SharedPreferences"; }
}
