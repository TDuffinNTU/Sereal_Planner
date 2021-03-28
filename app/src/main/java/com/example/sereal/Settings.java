package com.example.sereal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Map;


public class Settings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout mDrawer;
    NavigationView mNavView;
    Toolbar mToolbar;
    Intent mIntent;
    Button mDelNotes, mDelCards;

    // Settings items
    SwitchCompat mDarkMode, mDyslexicMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeSetter.GetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //  Hooks
        mDrawer = findViewById(R.id.DrawerLayout);
        mNavView = findViewById(R.id.NavMenu);
        mToolbar = findViewById(R.id.Toolbar);
        mDarkMode = findViewById(R.id.DarkModeSwitch);
        mDyslexicMode = findViewById(R.id.DyslexicModeSwitch);
        mDelNotes = findViewById(R.id.deleteNotesBtn);
        mDelCards = findViewById(R.id.deleteCardsBtn);

        // Setting up navigation bar
        mToolbar.setTitle(getString(R.string.settings_title));
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navOpen, R.string.navClose);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavView.bringToFront();
        mNavView.setNavigationItemSelectedListener(this);

        // Intent init
        mIntent = new Intent(this,getClass());

        // Dark mode switch action
        mDarkMode.setChecked(ThemeSetter.GetDarkBool(this));
        mDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeSetter.SetDarkMode(this, isChecked);
            finish();
            startActivity(getIntent());
        });

        // Dyslexic mode switch action
        mDyslexicMode.setChecked(ThemeSetter.GetDyslexicBool(this));
        mDyslexicMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ThemeSetter.SetDyslexicMode(this, isChecked);
            finish();
            startActivity(getIntent());
        });


        // Deleting records on user request
        mDelNotes.setOnClickListener(v -> {
            DeleteDBDialog(v, "N");
        });

        mDelCards.setOnClickListener(v -> {
            DeleteDBDialog(v, "C");
        });
    }

    private void DeleteDBDialog(View v, String dbname)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msg = "Notes" + getString(R.string.dialog_action);

        // dialog box
        builder.setMessage(msg)
                .setCancelable(true)
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete the contents of the DB
                    switch(dbname)
                    {
                        // Lazy solution because I didn't polymorphize my database classes...
                        case "N":
                            NotesDB n = new NotesDB(this);
                            n.clearRows();
                            break;
                        case "R":
                            break;
                        case"C":
                            CardsDB c = new CardsDB(this);
                            c.clearRows();
                            break;
                        default:
                            break;
                    }

                    // confirm to user
                    Toast.makeText(this, getString(R.string.delete_confirm),Toast.LENGTH_SHORT)
                            .show();
                })      .setNegativeButton("Cancel", (dialog, which) -> {
            // Cancelled
            Toast.makeText(this, getString(R.string.delete_cancel),Toast.LENGTH_SHORT)
                    .show();
            dialog.cancel();
        });

        builder.create().show();

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
                mIntent.setClass(Settings.this, CalendarActivity.class);
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