package com.example.sereal;

import android.content.SharedPreferences;
import android.content.Context;

class ThemeSetter {
    // Globally get and set the theme (light vs dark)
    public static void GetTheme(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.PREFS_LOCATION), Context.MODE_PRIVATE);
        boolean dark = sp.getBoolean(context.getString(R.string.PREF_DARK_THEME), false);
        boolean dyslexic = sp.getBoolean(context.getString(R.string.PREFS_DYSLEXIC), false);
        if(dark)
        {
            if (dyslexic) context.setTheme(R.style.DyslexicDark);
            context.setTheme(R.style.Theme_DarkTheme);
        }
        else {
            if (dyslexic)
                context.setTheme(R.style.DyslexicDark);
            context.setTheme(R.style.Theme_LightTheme);
        }
    }

    public static void SetDarkMode(Context context, boolean dark)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.PREFS_LOCATION), context.MODE_PRIVATE).edit();
        editor.putBoolean(context.getString(R.string.PREF_DARK_THEME), dark);
        editor.apply();
    }

    public static void SetDyslexicMode(Context context, boolean dyslexic)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getString(R.string.PREFS_LOCATION), context.MODE_PRIVATE).edit();
        editor.putBoolean(context.getString(R.string.PREFS_DYSLEXIC), dyslexic);
        editor.apply();
    }
    public static boolean GetDarkBool(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.PREFS_LOCATION), Context.MODE_PRIVATE);
        return sp.getBoolean(context.getString(R.string.PREF_DARK_THEME), false);
    }

    public static boolean GetDyslexicBool(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.PREFS_LOCATION), Context.MODE_PRIVATE);
        return sp.getBoolean(context.getString(R.string.PREFS_DYSLEXIC), false);
    }
}
