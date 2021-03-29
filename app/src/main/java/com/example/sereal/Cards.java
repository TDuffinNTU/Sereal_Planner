package com.example.sereal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class Cards extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;
    FloatingActionButton mCreateCard;
    RecyclerView mRecycler;
    CardsRecyclerAdapter mAdapter;
    NotesDB mNotesDB;
    CardsDB mCardsDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSetter.GetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
        mNavView = findViewById(R.id.NavMenu);
        mToolbar = findViewById(R.id.Toolbar);
        mCreateCard = findViewById(R.id.CreateCardFAB);
        mRecycler = findViewById(R.id.CardRecycler);

        // Setting up navigation bar
        mToolbar.setTitle(getString(R.string.cards_title));
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navOpen, R.string.navClose);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.bringToFront();
        mNavView.setNavigationItemSelectedListener(this);

        // Intent init
        mIntent = new Intent(this,getClass());

        // creating new card
        mCreateCard.setOnClickListener(v -> {
            mIntent.setClass(Cards.this, CardEditor.class);
            startActivity(mIntent);
        });

        mNotesDB = new NotesDB(this);
        mCardsDB = new CardsDB(this);

        mAdapter = new CardsRecyclerAdapter(this, mCardsDB, mNotesDB, CardsDB.DAY.NULL.ordinal());
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));

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
        switch (item.getItemId())
        {
            case R.id.nav_cards:
                mDrawer.closeDrawers();
                break;
            case R.id.nav_today:
                mIntent.setClass(Cards.this, MainActivity.class);
                startActivity(mIntent);
                break;
            case R.id.nav_notes:
                mIntent.setClass(Cards.this, Notes.class);
                startActivity(mIntent);
                break;
            case R.id.nav_settings:
                mIntent.setClass(Cards.this, Settings.class);
                startActivity(mIntent);
                break;
        }

        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDrawer.closeDrawers();
        mAdapter = new CardsRecyclerAdapter(this, mCardsDB, mNotesDB, CardsDB.DAY.NULL.ordinal());
        mRecycler.setAdapter(mAdapter);
    }
}