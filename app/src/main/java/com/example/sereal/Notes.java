package com.example.sereal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class Notes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;
    NotesDB mNotesDatabase;
    List<NoteStruct> mAllNotes;
    List<TextView> mAllNotesTxt;
    ConstraintLayout mConstraint;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
        mConstraint = findViewById(R.id.NotesConstraint);
        mNavView = findViewById(R.id.NavMenu);
        mToolbar = findViewById(R.id.Toolbar);

        // Setting up navigation bar
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navOpen, R.string.navClose);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.bringToFront();
        mNavView.setNavigationItemSelectedListener(this);

        // Intent init
        mIntent = new Intent(this,getClass());


        // Test loading from DB
        mNotesDatabase = new NotesDB(this);
        mAllNotes = mNotesDatabase.getAllNotes();
        mAllNotesTxt = new ArrayList<>();

        for (NoteStruct n : mAllNotes)
        {
            TextView tv = new TextView(this);
            tv.setText(n.getContents());
            tv.setLayoutParams(new ConstraintLayout.LayoutParams
                    (ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
            tv.setPadding(20,20,20, 20);
            mAllNotesTxt.add(tv);
            mConstraint.addView(tv);
        }

        // TODO Recycler view for notes
        // TODO fix datetime formatting and reset db




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
        switch (item.getItemId())
        {
            case R.id.nav_notes:
                mDrawer.closeDrawers();
                break;
            case R.id.nav_calendar:
                mIntent.setClass(Notes.this, CalendarActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_cards:
                mIntent.setClass(Notes.this, Cards.class);
                startActivity(mIntent);
                break;
            case R.id.nav_today:
                mIntent.setClass(Notes.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_settings:
                mIntent.setClass(Notes.this, Settings.class);
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