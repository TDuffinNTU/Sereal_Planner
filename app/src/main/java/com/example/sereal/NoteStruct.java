package com.example.sereal;

import java.util.Date;

// Class to control the creation and loading of note objects/cards in 'Notes' activity
public class NoteStruct {
    private int mID;
    private String mTitle;
    private String mContents;
    private String mDate;

    public NoteStruct() { }

    public NoteStruct(int id, String title, String date, String contents)
    {
        mID = id;
        mTitle = title;
        mContents = contents;
        mDate = date;
    }

    public NoteStruct(String title, String date, String contents)
    {
        mTitle = title;
        mContents = contents;
        mDate = date;
    }

    public void setID(int id) {mID = id;}
    public void setTitle(String s) {mTitle = s;}
    public void setContents(String s) {mContents = s;}
    public void setDate(String d) {mDate = d;}

    public int getID() {return mID;}
    public String getTitle () {return mTitle;}
    public String getContents() {return  mContents;}
    public String getDate() {return  mDate;}

}
