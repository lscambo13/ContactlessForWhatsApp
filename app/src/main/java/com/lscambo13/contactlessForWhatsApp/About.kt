package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.lscambo13.contactlessForWhatsApp.Menu.MenuClicks.makeStatusBarTransparent
import com.lscambo13.contactlessForWhatsApp.Menu.MenuClicks.setMarginTop
import kotlinx.android.synthetic.main.activity_about.*
import kotlin.system.exitProcess

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Experiment for transparent statusbar
        makeStatusBarTransparent()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView3)) { _, insets ->
            findViewById<TextView>(R.id.topGuide).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

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

        // Easter egg
        val logo: ImageView = imageView
        logo.setOnLongClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.easterEggTitle)
            builder.setMessage(R.string.easterEggMessage)
            builder.setPositiveButton("Cheers") { _, _ ->
            }
            when (Preferences(this).getEasterEgg()) {
                false -> {
                    builder.setNeutralButton("") { _, _ ->
                        Preferences(this).setEasterEgg(true)
                    }
                    builder.setNeutralButtonIcon(ContextCompat.getDrawable(this, R.drawable.egg))
                }
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            true
        }
    }

    // This enables the three-dot menu on top-right corner, Part 2.
    // Here options are different from home screen.
    private fun threeDot() {
        val menuPopup = PopupMenu(this, menu_btn)
        menuPopup.inflate(R.menu.menu)
        menuPopup.menu.removeItem(R.id.about)
        // This locates the previously used theme option, and checks the radio option.
        when (Preferences(this).getCurrentTheme()) {
            -1 -> {
                menuPopup.menu.findItem(R.id.sysDef).isChecked = true
            }
            1 -> {
                menuPopup.menu.findItem(R.id.day).isChecked = true
            }
            2 -> {
                menuPopup.menu.findItem(R.id.night).isChecked = true
            }
        }
        menuPopup.show()
        val preferences = Preferences(this)
        menuPopup.setOnMenuItemClickListener {
            when (it!!.itemId) {
                R.id.sysDef -> {
                    val themeItem = menuPopup.menu.findItem(it.itemId)
                    themeItem.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    true
                }
                R.id.day -> {
                    val themeItem = menuPopup.menu.findItem(it.itemId)
                    themeItem.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    true
                }
                R.id.night -> {
                    val themeItem = menuPopup.menu.findItem(it.itemId)
                    themeItem.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    true
                }
                R.id.exit -> {
                    this.finish()
                    exitProcess(0)
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

