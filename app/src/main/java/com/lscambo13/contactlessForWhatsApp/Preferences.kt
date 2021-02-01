package com.lscambo13.contactlessForWhatsApp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class Preferences(context: Context) {

//    Theme settings begin. --------------------------------

    // This is the save file for theme settings.
    val preferenceThemeFile = "ConfigTheme"

    // This is the integer entry for theme style.
    val preferenceIDTheme = "CurrentTheme"

    // This reads the theme file, ConfigTheme,
    // and reads the presently set theme style from CurrentTheme. (default = set by system)
    //
    // Integer values are as follows
    // MODE_NIGHT_FOLLOW_SYSTEM = -1
    // MODE_NIGHT_NO = 1
    // MODE_NIGHT_YES = 2
    val preferenceTheme: SharedPreferences =
        context.getSharedPreferences(preferenceThemeFile, Context.MODE_PRIVATE)

    fun getCurrentTheme(): Int {
        return preferenceTheme.getInt(preferenceIDTheme, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    // This saves the set theme as -1, 1 or 2.
    fun setCurrentTheme(theme: Int) {
        val editor = preferenceTheme.edit()
        editor.putInt(preferenceIDTheme, theme)
        editor.apply()
    }

//  Theme settings end. --------------------------------


//  First run settings begin. --------------------------

    // This is the save file for detecting first run.
    val preferenceFirstRunFile = "ConfigFirstRun"

    // This is the integer entry for first run.
    val preferenceIDFirstRun = "FirstRun"

    // This reads the first run detection file, ConfigFirstRun,
    // and detects if the app is installed freshly (Int 0) or updated (Int !0) from entry FirstRun.
    val preferenceFirstRun: SharedPreferences =
        context.getSharedPreferences(preferenceFirstRunFile, Context.MODE_PRIVATE)

    fun getFirstRun(): Int {
        return preferenceFirstRun.getInt(preferenceIDFirstRun, 0)
    }

    // This saves the first run property as 0 or VersionCode.
    fun setFirstRun(run: Int) {
        val editor = preferenceFirstRun.edit()
        editor.putInt(preferenceIDFirstRun, run)
        editor.apply()
    }

//  First run settings end. --------------------------


//  Easter egg settings begin. -----------------------

    // This is the save file for collecting easter egg.
    val preferenceEasterEggFile = "ConfigEasterEgg"

    // This is the integer entry for easter egg.
    val preferenceIDEasterEgg = "EasterEgg"

    // This reads the easter egg collection file, ConfigEasterEgg,
    // and detects if the egg is collected (true) or not (false) from entry EasterEgg.
    val preferenceEasterEgg: SharedPreferences =
        context.getSharedPreferences(preferenceEasterEggFile, Context.MODE_PRIVATE)

    fun getEasterEgg(): Boolean {
        return preferenceEasterEgg.getBoolean(preferenceIDEasterEgg, false)
    }

    // This saves the easter egg property as true or false.
    fun setEasterEgg(collected: Boolean) {
        val editor = preferenceEasterEgg.edit()
        editor.putBoolean(preferenceIDEasterEgg, collected)
        editor.apply()
    }

//  Easter egg settings end. -----------------------

}