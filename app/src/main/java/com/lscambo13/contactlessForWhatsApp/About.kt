package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_about.*

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Load ads.
        val adView = findViewById<AdView>(R.id.adView)
        val adReq = AdRequest.Builder().build()
        adView.loadAd(adReq)

        val summary: TextView = findViewById(R.id.summary)

        // This enables the back button.
        val close: Button = findViewById(R.id.closeAbt)
        close.setOnClickListener {
            onBackPressed()
        }
        // This enables the check for updates button.
        val newVer: Button = findViewById(R.id.DwnNewVer)
        newVer.setOnClickListener {
            val updateLink = "https://bit.ly/3gWV4rL"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(updateLink)
            adView.loadAd(adReq)
            startActivity(openURL)
        }

        // This enables the telegram button to open my support group.
        val telegram: ImageView = findViewById(R.id.telegram)
        telegram.setOnClickListener {
            Menu.MenuClicks.supportClick(this)
        }

        // This enables the share button with a cool message.
        val shr: ImageView = findViewById(R.id.share)
        shr.setOnClickListener {
            Menu.MenuClicks.shareClick(this)
        }

        // These are the animations users see after tapping on description text.
        var animRun = 0
        val animateSummaryIn: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.summary_anim_in)
        val animateSummaryOut: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.summary_anim_out)
        summary2.visibility = View.INVISIBLE
        summary.setOnClickListener {
            when (animRun) {
                0 -> {
                    summary2.visibility = View.VISIBLE
                    summary.startAnimation(animateSummaryOut)
                    summary2.startAnimation(animateSummaryIn)
                    animRun = 1
                }
                1 -> {
                    summary.startAnimation(animateSummaryIn)
                    summary2.startAnimation(animateSummaryOut)
                    animRun = 0
                }
            }
            adView.loadAd(adReq)
        }

        // This enables the three-dot menu on top-right corner, Part 1.
        menu_btn.setOnClickListener {
            threeDot()
        }
    }

    // This enables the three-dot menu on top-right corner, Part 2.
    // Here options are different from home screen.
    private fun threeDot() {
        val menuPopup = PopupMenu(this, menu_btn)
        menuPopup.inflate(R.menu.menu)
        menuPopup.menu.removeItem(R.id.about)
        menuPopup.show()
        val preferences = Preferences(this)
        menuPopup.setOnMenuItemClickListener {
            when (it!!.itemId) {
                R.id.sysDef -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    menuPopup.menu.setGroupCheckable(1, true, true)
                    findViewById<RadioButton>(R.id.sysDef).isChecked = true
                    true
                }
                R.id.day -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    true
                }
                R.id.night -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    true
                }
                R.id.exit -> {
                    finish()
                    true
                }
                R.id.home -> {
                    Menu.MenuClicks.home(this)
                    true
                }
                R.id.changelog -> {
                    Menu.MenuClicks.changelog(this)
                    true
                }
                else -> false
            }
        }
    }
}

