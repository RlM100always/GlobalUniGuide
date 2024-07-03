package com.techtravelcoder.alluniversityinformations.notes;

public class NotesModel {
    private String title;
    private String notes,date,key;
    private boolean isExpanded;

    // Constructor, getters, and setters

  public NotesModel(){

  }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public NotesModel(String title, String notes) {
        this.title = title;
        this.notes = notes;
        this.isExpanded = false; // Default is not expanded
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
