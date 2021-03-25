package com.example.sereal;

import android.content.Context;

import java.text.SimpleDateFormat;

public class CardStruct {

    private Integer mID;
    private String mTime;
    private boolean mIsAlarm;
    private NoteStruct mNote;
    private boolean[] mDays;

    public CardStruct(){}
    public CardStruct(Integer id, String time, boolean alarm, NoteStruct note, boolean[] days)
    {
        mID = id;
        mTime = time;
        mIsAlarm = alarm;
        mNote = note;
        mDays = days;
    }

    // used for ordering cards
    public boolean isAfterCard(Context context, CardStruct card)
    {
        SimpleDateFormat format = new SimpleDateFormat(context.getString(R.string.time_format));
        try
        {
            return format.parse(card.getmTime()).after((format.parse(this.mTime)));

        } catch (Exception e) {
            // Just return true if we encounter an error
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


    public NoteStruct getmNote() {
        return mNote;
    }

    public void setmNote(NoteStruct mNote) {
        this.mNote = mNote;
    }

    public boolean ismIsAlarm() {
        return mIsAlarm;
    }

    public void setmIsAlarm(boolean mIsAlarm) {
        this.mIsAlarm = mIsAlarm;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public boolean[] getmDays() {
        return mDays;
    }

    public void setmDays(boolean[] mDays) {
        this.mDays = mDays;
    }
}

