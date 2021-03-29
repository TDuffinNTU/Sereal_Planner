package com.example.sereal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;
    FloatingActionButton mPrevDay, mNextDay;

    RecyclerView mRecycler;
    CardsRecyclerAdapter mAdapter;
    NotesDB mNotesDB;
    CardsDB mCardsDB;
    List<String> mDayStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSetter.GetTheme(this); // Set's context theme to light/dark
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
        mNavView = findViewById(R.id.NavMenu);
        mToolbar = findViewById(R.id.Toolbar);
        mRecycler = findViewById(R.id.CardRecycler);

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

        // Recycler setup
        mNotesDB = new NotesDB(this);
        mCardsDB = new CardsDB(this);

        mAdapter = new CardsRecyclerAdapter(this, mCardsDB, mNotesDB, AlarmsHandler.getTodayOfWeek());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDayStrings = new ArrayList<>();
        mDayStrings.add("Monday");
        mDayStrings.add("Tuesday");
        mDayStrings.add("Wednesday");
        mDayStrings.add("Thursday");
        mDayStrings.add("Friday");
        mDayStrings.add("Saturday");
        mDayStrings.add("Sunday");

        mToolbar.setTitle(mDayStrings.get(AlarmsHandler.getTodayOfWeek()));

        mNextDay = findViewById(R.id.NextDay);
        mPrevDay = findViewById(R.id.PrevDay);

        // increment/decrement days so user can see all day routines
        mNextDay.setOnClickListener(v -> {
            mAdapter.NextDay();
            mToolbar.setTitle(mDayStrings.get(mAdapter.GetDay()));
        });

        mPrevDay.setOnClickListener(v -> {
            mAdapter.PrevDay();
            mToolbar.setTitle(mDayStrings.get(mAdapter.GetDay()));
        });

        AlarmsHandler.SetAllAlarms(this);
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
                mDrawer.closeDrawers();
                break;
            case R.id.nav_cards:
                mIntent.setClass(MainActivity.this, Cards.class);
                startActivity(mIntent);
                break;
            case R.id.nav_notes:
                mIntent.setClass(MainActivity.this, Notes.class);
                startActivity(mIntent);
                break;
            case R.id.nav_settings:
                mIntent.setClass(MainActivity.this, Settings.class);
                startActivity(mIntent);
                break;
        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mDrawer.closeDrawers();
        mAdapter = new CardsRecyclerAdapter(this, mCardsDB, mNotesDB, AlarmsHandler.getTodayOfWeek());
        mRecycler.setAdapter(mAdapter);
        mToolbar.setTitle(mDayStrings.get(AlarmsHandler.getTodayOfWeek()));
    }



}