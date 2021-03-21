package com.example.sereal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CardsDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CardsDatabase";
    private static final String TABLE_Cards = "Cards";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENTS = "contents";
    private static final String KEY_DATE = "date";

    public CardsDB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String args = "CREATE TABLE " +
                TABLE_Cards + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_CONTENTS + " TEXT)";

        db.execSQL(args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Cards);
        onCreate(db);
    }

    // Note creation
    void addNote(CardStruct n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        // TODO v.put(KEY_x, n.getX());

        // adding a row
        db.insert(TABLE_Cards, null, v);
        db.close();
    }

    // retrieving Note
    CardStruct getNote(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_Cards, new String[] {KEY_ID, KEY_TITLE, KEY_CONTENTS, KEY_DATE},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        // Note has been found
        if (c != null)
        {
            c.moveToFirst();
        } else
        {
            return null;
        }

        c.close();
        db.close();

        return new CardStruct();
    }

    // TODO if this becomes a problem it might be best to split out the query/looping code...
    // Query the database
    public List<CardStruct> getCardsOnDate(String d)
    {
        List<CardStruct> list = new ArrayList<>();

        // returning Cards made on 'd' date
        String query = "SELECT * FROM " + TABLE_Cards + " WHERE " + KEY_DATE + " = " + d;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through query results (if any)
        if(c.moveToFirst())
        {
            do
            {
                list.add(new CardStruct());

            }
            while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    public List<CardStruct> getAllCards()
    {
        List<CardStruct> list = new ArrayList<>();

        // returning Cards
        String query = "SELECT * FROM " + TABLE_Cards;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through query results (if any)
        if(c.moveToFirst())
        {
            do
            {
                // TODO fix all new CardStruct() instances!
                list.add(new CardStruct());

            }
            while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    // Updating a single  record
    public int updateNote(CardStruct n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        // TODO v.put() etc etc

        int r = db.update(TABLE_Cards, v,KEY_ID + "=?",
                new String[]{String.valueOf(n.getID())});

        db.close();

        return r;
    }

}
