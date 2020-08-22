package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loadTheme = Preferences(this).getCurrentTheme()
        AppCompatDelegate.setDefaultNightMode(loadTheme)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}