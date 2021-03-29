package com.example.sereal;

// Class to control the creation and loading of note objects/cards in 'Notes' activity
public class NoteStruct {
    private Integer mID;
    private String mTitle;
    private String mContents;

    public NoteStruct() { }

    public NoteStruct(Integer id, String title, String contents)
    {
        mID = id;
        mTitle = title;
        mContents = contents;
    }

    public NoteStruct(String title, String contents)
    {
        mTitle = title;
        mContents = contents;
    }

    public void setID(Integer id) {mID = id;}
    public void setTitle(String s) {mTitle = s;}
    public void setContents(String s) {mContents = s;}


    public Integer getID() {return mID;}
    public String getTitle () {return mTitle;}
    public String getContents() {return  mContents;}


}
