package com.example.sereal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;

    Map<Integer, Integer> NavActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
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
            case R.id.nav_today:
                finish();
                overridePendingTransition(0,0);
                startActivity(getIntent());
                overridePendingTransition(0,0);
                break;
            case R.id.nav_calendar:
                mIntent.setClass(MainActivity.this, Calendar.class);
                startActivity(mIntent);
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


}