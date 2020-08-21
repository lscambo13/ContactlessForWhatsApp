package com.lscambo13.contactlessForWhatsApp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class Preferences(context: Context) {
    val preferenceName = "ThemePrefs"
    val preferenceValue = "CurrentTheme"

    val preference: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)

    fun getCurrentTheme(): Int{
        return preference.getInt(preferenceValue, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun setCurrentTheme(theme: Int){
        val editor = preference.edit()
        editor.putInt(preferenceValue, theme)
        editor.apply()
    }
}