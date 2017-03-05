package com.example.root.cloudnote;

/**
 * Created by root on 6/3/17.
 */

public class Note {
    private String Title;
    private String Notes;

    public Note(){}

    public Note(String title,String notes){
        this.Title=title;
        this.Notes=notes;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
