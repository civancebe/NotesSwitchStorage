package com.example.notesswitchstorage.storage;

import android.content.Context;
import com.example.notesswitchstorage.model.Note;
import java.util.List;

public interface NoteStorage {
    List<Note> listNotes(Context ctx);
    boolean saveNote(Context ctx, Note note);       // create or overwrite by name
    boolean deleteNote(Context ctx, String name);   // delete by name
    String getDisplayName();                        // "SharedPreferences" or "Files"
}
