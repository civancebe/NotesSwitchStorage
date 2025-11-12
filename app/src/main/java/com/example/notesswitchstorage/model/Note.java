package com.example.notesswitchstorage.model;

public class Note {
    private String name;
    private String content;

    public Note(String name, String content) {
        this.name = name;
        this.content = content;
    }
    public String getName() { return name; }
    public String getContent() { return content; }
}
