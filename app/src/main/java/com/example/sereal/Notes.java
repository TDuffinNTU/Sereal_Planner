package com.example.sereal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.format.DateTimeFormatter;


public class Notes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;
    NotesDB mNotesDatabase;
    ConstraintLayout mConstraint;
    RecyclerView mRecycler;
    FloatingActionButton mNewNoteFAB;
    NotesRecyclerAdapter mAdapter;
    DateTimeFormatter mDTFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSetter.GetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
        mConstraint = findViewById(R.id.NotesConstraint);
        mNavView = findViewById(R.id.NavMenu);
        mToolbar = findViewById(R.id.Toolbar);
        mRecycler = findViewById(R.id.NotesRecycler);
        mNewNoteFAB = findViewById(R.id.NotesFAB);

        // Setting up navigation bar
        mToolbar.setTitle(getString(R.string.notes_title));
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navOpen, R.string.navClose);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.bringToFront();
        mNavView.setNavigationItemSelectedListener(this);

        // Intent init
        mIntent = new Intent(this,getClass());

        // loading from DB
        mNotesDatabase = new NotesDB(this);


        // Recycler view for notes
        mRecycler = findViewById(R.id.NotesRecycler);
        mAdapter = new NotesRecyclerAdapter(this, mNotesDatabase);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        // TODO fix datetime formatting
        mDTFormat = DateTimeFormatter.ofPattern(getString(R.string.date_format));
        mNewNoteFAB.setOnClickListener(v -> {
           mAdapter.AddNewNote();
           mRecycler.scrollToPosition(0);
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