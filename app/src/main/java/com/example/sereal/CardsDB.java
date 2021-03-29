package com.example.sereal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardsDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "CardsDatabase";

    private static final String TABLE_CARDS = "Cards";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TIME = "time";
    private static final String KEY_MON = "mondays";
    private static final String KEY_TUE = "tuedays";
    private static final String KEY_WED = "wednesdays";
    private static final String KEY_THU = "thursdays";
    private static final String KEY_FRI = "fridays";
    private static final String KEY_SAT = "saturdays";
    private static final String KEY_SUN = "sundays";
    private static final String KEY_ALARM = "alarm";
    private static final String KEY_NOTE_ID = "noteID";

    private final Context mContext;
    private final DateTimeFormatter mFormatter;

    public enum DAY
    {
        MON, TUE, WED, THU, FRI, SAT, SUN, NULL

    }

    public CardsDB(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        mFormatter = DateTimeFormatter.ofPattern(mContext.getString(R.string.time_format));


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String args = "CREATE TABLE " +
                TABLE_CARDS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_TITLE + " TEXT," +
                KEY_TIME + " TEXT," +
                KEY_MON + " TEXT," +
                KEY_TUE + " TEXT," +
                KEY_WED + " TEXT," +
                KEY_THU + " TEXT," +
                KEY_FRI + " TEXT," +
                KEY_SAT + " TEXT," +
                KEY_SUN + " TEXT," +
                KEY_NOTE_ID + " INTEGER," +
                KEY_ALARM + " TEXT)";

        db.execSQL(args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }

    public void clearRows()
    {
        String q = "DELETE FROM " + TABLE_CARDS;
        getWritableDatabase().execSQL(q);
    }

    // Note creation
    void addCard(CardStruct n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();

        PlaceValues(n, v);

        // adding a row
        db.insert(TABLE_CARDS, null, v);
        db.close();
    }



    private void PlaceValues(CardStruct n, ContentValues v) {
        // places our data values into ContentValues
        ArrayList<Boolean> days = n.getDays();

        v.put(KEY_TITLE, n.getTitle());
        v.put(KEY_TIME, mFormatter.format(n.getTime()));
        v.put(KEY_MON, days.get(0));
        v.put(KEY_TUE, days.get(1));
        v.put(KEY_WED, days.get(2));
        v.put(KEY_THU, days.get(3));
        v.put(KEY_FRI, days.get(4));
        v.put(KEY_SAT, days.get(5));
        v.put(KEY_SUN, days.get(6));
        v.put(KEY_ALARM, n.isAlarm());
        try {
            v.put(KEY_NOTE_ID, n.getNote().getID());
        } catch(NullPointerException e)
        {
            e.printStackTrace();
            v.put(KEY_NOTE_ID, "-1");
        }
    }

    private CardStruct BuildStruct(Cursor c) {
        // builds out the data structure for the card from the database
        try
        {
            ArrayList<Boolean> days = new ArrayList<>();
            boolean alarm;
            NoteStruct note = null;

            for(int i = 3; i <= 9; i++)
            {
                // load individual day cols into struct
                days.add(c.getInt(i) > 0);
            }

            alarm = c.getInt(11) > 0;

            // note id exists
            int noteid = Integer.parseInt(c.getString(10));
            if(noteid >= 0)
            {
                NotesDB ndb = new NotesDB(mContext);
                note = ndb.getNote(noteid);
            }

            return new CardStruct(
                    Integer.parseInt(c.getString(0)),
                    c.getString(1),
                    LocalTime.from(mFormatter.parse(c.getString(2))),
                    alarm,
                    note,
                    days);

        } catch(CursorIndexOutOfBoundsException e)
        {
            // catching cases where index supplied is not valid
            e.printStackTrace();
            return null;
        }
    }

    // retrieving Card
    CardStruct getCard(int id)
    {
        // query the database
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CARDS + " WHERE " + KEY_ID + " = " + id;

        Cursor c = db.rawQuery(query, null);

        //Card has been found
        if (c != null)
        {
            c.moveToFirst();
        } else
        {
            // id supplied was bogus!
            return null;
        }

        CardStruct card = BuildStruct(c);

        c.close();
        db.close();

        return card;
    }

    public List<CardStruct> getAllCards()
    {
        List<CardStruct> list = new ArrayList<>();

        // returning Cards
        String query = "SELECT * FROM " + TABLE_CARDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        // looping through query results (if any)
        if(c.moveToFirst())
        {
            do
            {
                list.add(BuildStruct(c));
            }
            while (c.moveToNext());
        }

        c.close();
        db.close();

        return list;
    }

    public List<CardStruct> getCardsOnDay(Integer day)
    {
        List<CardStruct> list = getAllCards();

        if (day.equals(DAY.NULL.ordinal()))
        {
            return list;
        }

        List<CardStruct> result = new ArrayList<>();

        for(CardStruct c : list)
        {
            if(c.getDays().get(day))
            {
                result.add(c);
            }
        }

        return result;
    }

    // Updating a single  record
    public void updateCard(CardStruct n)
    {
        if(n.getID() == null)
        {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        PlaceValues(n, v);
        db.update(TABLE_CARDS, v,KEY_ID + "=?",
                new String[]{String.valueOf(n.getID())});
        db.close();
    }

    public int countRows()
    {
        return  (int)DatabaseUtils.queryNumEntries(this.getReadableDatabase(), TABLE_CARDS);
    }

    public void removeCard(CardStruct n)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CARDS + " WHERE " + KEY_ID + " = " + n.getID();
        db.execSQL(query);

        Toast.makeText(mContext, "Card deleted", Toast.LENGTH_SHORT).show();
    }

}
