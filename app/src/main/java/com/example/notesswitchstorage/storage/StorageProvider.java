package com.example.notesswitchstorage.storage;

import android.content.Context;
import android.content.SharedPreferences;

// Keeps the user's choice for storage and returns the correct implementation.
public class StorageProvider {
    private static final String PREFS = "storage_choice";
    private static final String KEY = "which"; // "prefs" or "files"

    public static void setUseSharedPrefs(Context ctx) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, "prefs").apply();
    }
    public static void setUseFiles(Context ctx) {
        ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(KEY, "files").apply();
    }
    public static NoteStorage get(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String which = sp.getString(KEY, "prefs");
        return "files".equals(which) ? new FileNoteStorage() : new SharedPrefsNoteStorage();
    }
    public static String currentName(Context ctx) { return get(ctx).getDisplayName(); }
}
