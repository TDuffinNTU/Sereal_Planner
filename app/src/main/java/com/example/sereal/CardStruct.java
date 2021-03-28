package com.example.sereal;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class CardStruct {

    private Integer mID;
    private LocalTime mTime;
    private String mTitle;
    private Boolean mIsAlarm;
    private NoteStruct mNote;
    private ArrayList<Boolean> mDays;

    public CardStruct(){}
    public CardStruct(Integer id, String title, LocalTime time, boolean alarm, NoteStruct note, ArrayList<Boolean> days)
    {
        mID = id;
        mTime = time;
        mTitle = title;
        mIsAlarm = alarm;
        mNote = note;
        mDays = days;
    }

    // used for ordering cards
    public boolean isAfterCard(Context context, CardStruct card)
    {
        try
        {
            return this.getTime().isAfter(card.getTime());

        } catch (Exception e) {
            // return true if we encounter an error
            e.printStackTrace();
            return true;
        }
    }

    public Integer getID(){
        return mID;
    }

    public void setID(Integer id){
        mID = id;
    }


    public NoteStruct getNote() {
        return mNote;
    }

    public void setNote(NoteStruct mNote) {
        this.mNote = mNote;
    }

    public boolean isAlarm() {
        return mIsAlarm;
    }

    public void setAlarm(boolean mIsAlarm) {
        this.mIsAlarm = mIsAlarm;
    }

    public LocalTime getTime() {
        return mTime;
    }

    public void setTime(LocalTime mTime) {
        this.mTime = mTime;
    }

    public ArrayList<Boolean> getDays() {
        return mDays;
    }

    public void setDays(ArrayList<Boolean> mDays) {
        this.mDays = mDays;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    @Override
    public String toString()
    {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss a");
        StringBuilder data = null;

        data = new StringBuilder("TITLE: ").append(getTitle());

        data.append("\nCARD ID: ").append(getID());

        data.append("\nTIME: ");

        data.append(formatter.format(getTime())).append("\n");

        for(boolean b : getDays())
        {
            data.append("DAY: ").append(b).append("\n");
        }

        data.append("ALARM: ").append(isAlarm());

        if(getNote() != null)
        {
            data.append("\nNOTE ID: ").append(getNote().getID());
            data.append("\nNOTE TITLE: ").append(getNote().getTitle());
        } else {
            data.append("\nNO NOTE ATTACHED");
        }

        return data.toString();

    }
}

