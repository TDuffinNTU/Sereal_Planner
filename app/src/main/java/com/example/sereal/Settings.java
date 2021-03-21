package com.example.sereal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;


public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;

    //TODO Boilerplate code into other classes. That or research ViewGroups

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_settings:
                mDrawer.closeDrawers();
                break;
            case R.id.nav_calendar:
                mIntent.setClass(Settings.this, Calendar.class);
                startActivity(mIntent);
                break;
            case R.id.nav_cards:
                mIntent.setClass(Settings.this, Cards.class);
                startActivity(mIntent);
                break;
            case R.id.nav_notes:
                mIntent.setClass(Settings.this, Notes.class);
                startActivity(mIntent);
                break;
            case R.id.nav_today:
                mIntent.setClass(Settings.this, MainActivity.class);
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