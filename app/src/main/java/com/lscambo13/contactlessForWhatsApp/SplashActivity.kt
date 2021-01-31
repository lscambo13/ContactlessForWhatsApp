package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This loads the last used theme, and applies it.
        val loadTheme = Preferences(this).getCurrentTheme()
        AppCompatDelegate.setDefaultNightMode(loadTheme)

        // This proceeds to the home screen of the app.
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}