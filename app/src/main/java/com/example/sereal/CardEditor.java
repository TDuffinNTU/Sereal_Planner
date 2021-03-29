package com.example.sereal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CardEditor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;
    FloatingActionButton mSaveData;
    TimePicker mTimePicker;
    EditText mTitle;
    SwitchCompat mAlarm;
    CheckBox mMon,mTue,mWed,mThur,mFri,mSat,mSun;

    // Record stuff
    LinearLayout mNoteSelector;
    CardStruct mCard;
    List<NoteStruct> mAllNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSetter.GetTheme(this); // Set's context theme to light/dark
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_editor);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
        mNavView = findViewById(R.id.NavMenu);
        mToolbar = findViewById(R.id.Toolbar);
        mNoteSelector = findViewById(R.id.NoteSelector);
        mSaveData = findViewById(R.id.SaveCard);
        mTitle = findViewById(R.id.cardTitle);
        mTimePicker = findViewById(R.id.TimePicker);
        mAlarm = findViewById(R.id.AlarmSwitch);

        //days
        mMon = findViewById(R.id.monday);
        mTue = findViewById(R.id.tuesday);
        mWed = findViewById(R.id.wednesday);
        mThur = findViewById(R.id.thursday);
        mFri = findViewById(R.id.friday);
        mSat = findViewById(R.id.saturday);
        mSun = findViewById(R.id.sunday);

        // Setting up navigation bar
        mToolbar.setTitle(getString(R.string.card_editor_title));
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navOpen, R.string.navClose);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.bringToFront();
        mNavView.setNavigationItemSelectedListener(this);

        // Intent init
        mIntent = getIntent();
        try {
            int intID = mIntent.getIntExtra(getString(R.string.card_id), -1);

            CardsDB cdb = new CardsDB(this);
            if (cdb.getCard(intID) != null)
            {
                mCard = cdb.getCard(intID);
                mTitle.setText(mCard.getTitle());

                ArrayList<Boolean> days = mCard.getDays();

                mMon.setChecked(days.get(0));
                mTue.setChecked(days.get(1));
                mWed.setChecked(days.get(2));
                mThur.setChecked(days.get(3));
                mFri.setChecked(days.get(4));
                mSat.setChecked(days.get(5));
                mSun.setChecked(days.get(6));

                mTimePicker.setHour((mCard.getTime().getHour()));
                mTimePicker.setMinute((mCard.getTime().getMinute()));

                mAlarm.setChecked(mCard.isAlarm());
            }
            else {
                mCard = new CardStruct();
            }
        } catch (Exception e)
        {
            mCard = new CardStruct();
        }

        // Setting up Note selection
        NotesDB ndb = new NotesDB(this);
        mAllNotes = ndb.getAllNotes();
        int index = 0;

        // NoteSelector init --> Selected note stored here!
        for(NoteStruct n : mAllNotes)
        {
            // Textview generation
            TextView tv = new TextView(getApplicationContext());
            tv.setId(index);
            tv.setTextSize(18);
            tv.setText(n.getTitle());
            try {
                if (n.getID().equals(mCard.getNote().getID())) {
                    tv.setTextColor(getColor(R.color.red));
                } else {
                    tv.setTextColor(ThemeSetter.GetDarkBool(getApplicationContext()) ?
                    getColor(R.color.white) : getColor(R.color.black));
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                tv.setTextColor(ThemeSetter.GetDarkBool(getApplicationContext()) ?
                getColor(R.color.white) : getColor(R.color.black));
            }

            tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // get selected note's data
            tv.setOnClickListener(v -> {
                TextView castTV = (TextView)v;
                // toggling colour red/normal cols
                if(castTV.getCurrentTextColor() != getColor(R.color.red))
                {
                    mCard.setNote(mAllNotes.get(v.getId()));
                    int childCount = mNoteSelector.getChildCount();
                    for(int i = 0; i < childCount; i++)
                    {
                        TextView txt = (TextView)mNoteSelector.getChildAt(i);
                       txt.setTextColor(ThemeSetter.GetDarkBool(getApplicationContext()) ?
                                getColor(R.color.white) : getColor(R.color.black));
                    }

                    castTV.setTextColor(getColor(R.color.red));
                } else {
                    mCard.setNote(null);
                    castTV.setTextColor(ThemeSetter.GetDarkBool(getApplicationContext()) ?
                            getColor(R.color.white) : getColor(R.color.black));
                }

            });
            mNoteSelector.addView(tv);
            index++;
        }


        // FAB setup
        mSaveData.setOnClickListener(v -> {
            CardsDB cdb = new CardsDB(getApplicationContext());

            // load data and push to db
            ArrayList<Boolean> days = new ArrayList<>();
            days.add(mMon.isChecked());
            days.add(mTue.isChecked());
            days.add(mWed.isChecked());
            days.add(mThur.isChecked());
            days.add(mFri.isChecked());
            days.add(mSat.isChecked());
            days.add(mSun.isChecked());
            mCard.setDays(days);

            mCard.setTitle(mTitle.getText().toString());
            mCard.setTime(LocalTime.of(mTimePicker.getHour(), mTimePicker.getMinute()));
            mCard.setAlarm(mAlarm.isChecked());

            if(mCard.getID() == null)
            {
                cdb.addCard(mCard);
                Toast.makeText(getApplicationContext(), "Saved new Card", Toast.LENGTH_SHORT).show();
            } else {
                cdb.updateCard(mCard);
                Toast.makeText(getApplicationContext(), "Updated Card", Toast.LENGTH_SHORT).show();
            }

            finish();

        });


    }


    @Override
    public void onBackPressed()
    {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
        {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        // Switching to different layouts when done
        switch (item.getItemId())
        {
            case R.id.nav_today:
                mIntent.setClass(CardEditor.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_cards:
                finish();
                break;
            case R.id.nav_notes:
                mIntent.setClass(CardEditor.this, Notes.class);
                startActivity(mIntent);
                break;
            case R.id.nav_settings:
                mIntent.setClass(CardEditor.this, Settings.class);
                startActivity(mIntent);
                break;
        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDrawer.closeDrawers();
    }

}