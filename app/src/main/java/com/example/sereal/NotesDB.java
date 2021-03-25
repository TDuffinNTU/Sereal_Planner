package com.example.sereal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotesDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "notesDatabase";
    private static final String TABLE_NOTES = "notes";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENTS = "contents";
    private static final String KEY_DATE = "date";
    Context mContext;

    public NotesDB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String args = "CREATE TABLE " +
                TABLE_NOTES + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_DATE + " TEXT," +
                KEY_CONTENTS + " TEXT)";

        db.execSQL(args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }

    public void clearRows()
    {
        String q = "DELETE FROM " + TABLE_NOTES;
        getWritableDatabase().execSQL(q);
    }

    // Note creation
    void addNote(NoteStruct n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE, n.getTitle());
        v.put(KEY_CONTENTS, n.getContents());
        v.put(KEY_DATE, n.getDate());

        // adding a row
        db.insert(TABLE_NOTES, null, v);
        Toast.makeText(mContext, "Note added!", Toast.LENGTH_SHORT).show();
        db.close();
    }

    // retrieving Note
    NoteStruct getNote(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_NOTES, new String[] {KEY_ID, KEY_TITLE, KEY_CONTENTS, KEY_DATE},
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

        NoteStruct n = new NoteStruct(
                Integer.parseInt(c.getString(0)),
                c.getString(1),
                c.getString(2),
                c.getString(3));

        c.close();
        db.close();

        return n;
    }

    // TODO if this becomes a problem it might be best to split out the query/looping code...
    // Query the database
    public List<NoteStruct> getNotesOnDate(String d)
    {
        List<NoteStruct> nlist = new ArrayList<>();

        // returning notes made on 'd' date
        String query = "SELECT * FROM " + TABLE_NOTES + " WHERE " + KEY_DATE + " = " + d;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through query results (if any)
        if(c.moveToFirst())
        {
            do
            {
                nlist.add(new NoteStruct(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)));

            }
            while (c.moveToNext());
        }

        c.close();
        db.close();

        return nlist;
    }

    public List<NoteStruct> getAllNotes()
    {
        List<NoteStruct> nlist = new ArrayList<>();

        // returning notes
        String query = "SELECT * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through query results (if any)
        if(c.moveToFirst())
        {
            do
            {
                nlist.add(new NoteStruct(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3)));

            }
            while (c.moveToNext());
        }

        c.close();
        db.close();

        return nlist;
    }

    // Updating a single  record
    public int updateNote(NoteStruct n)
    {
        if(n.getID() == null)
        {
            // avoiding case of new notes being updated vs added
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE, n.getTitle());
        v.put(KEY_CONTENTS, n.getContents());
        v.put(KEY_DATE, n.getDate());

        int r = db.update(TABLE_NOTES, v,KEY_ID + "=?",
                new String[]{String.valueOf(n.getID())});

        db.close();
        Toast.makeText(mContext, "Note updated!", Toast.LENGTH_SHORT).show();

        return r;
    }

    public int removeNote(NoteStruct n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NOTES + " WHERE " + KEY_ID + " = " + String.valueOf(n.getID());
        db.execSQL(query);

        Toast.makeText(mContext, "Note deleted!", Toast.LENGTH_SHORT).show();
        return 1;
    }

}
