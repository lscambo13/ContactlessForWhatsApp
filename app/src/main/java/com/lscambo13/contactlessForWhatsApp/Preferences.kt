package com.lscambo13.contactlessForWhatsApp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class Preferences(context: Context) {
    val preferenceThemeFile = "ConfigTheme"
    val preferenceFirstRunFile = "ConfigFirstRun"
    val preferenceIDTheme = "CurrentTheme"
    val preferenceIDFirstRun = "FirstRun"

    val preferenceTheme: SharedPreferences =
        context.getSharedPreferences(preferenceThemeFile, Context.MODE_PRIVATE)

    val preferenceFirstRun: SharedPreferences =
        context.getSharedPreferences(preferenceFirstRunFile, Context.MODE_PRIVATE)

    fun getCurrentTheme(): Int {
        return preferenceTheme.getInt(preferenceIDTheme, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun getFirstRun(): Boolean {
        return preferenceFirstRun.getBoolean(preferenceIDFirstRun, true)
    }

    fun setCurrentTheme(theme: Int) {
        val editor = preferenceTheme.edit()
        editor.putInt(preferenceIDTheme, theme)
        editor.apply()
    }

    fun setFirstRun(run: Boolean) {
        val editor = preferenceFirstRun.edit()
        editor.putBoolean(preferenceIDFirstRun, run)
        editor.apply()
    }
}