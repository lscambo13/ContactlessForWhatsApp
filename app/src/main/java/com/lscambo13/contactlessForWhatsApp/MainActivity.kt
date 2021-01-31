package com.lscambo13.contactlessForWhatsApp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.TelephonyManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This section detects if the app is running for the first time.
        val isFirstRun: Int = Preferences(this).getFirstRun()
        when (isFirstRun) {
            (0) -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.welcomeTitle)
                builder.setMessage(R.string.welcomeMessage)
                builder.setPositiveButton("Continue") { _, _ ->
                    Preferences(this).setFirstRun(BuildConfig.VERSION_CODE)
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
            // This section shows the update dialog in case it has been updated.
            in 1 until BuildConfig.VERSION_CODE -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.updateTitle)
                builder.setMessage(R.string.updateMessage)
                builder.setPositiveButton("Continue") { _, _ ->
                    Preferences(this).setFirstRun(BuildConfig.VERSION_CODE)
                }
                builder.setNeutralButton("Open Github") { _, _ ->
                    Preferences(this).setFirstRun(BuildConfig.VERSION_CODE)
                    Menu.MenuClicks.github(this)
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }

        // These are the animations users see post-splash screen.
        val animateHeading: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.heading_anim)
        val animateButtonChat: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.button_chat_anim)
        val animateButtonAbout: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.button_about_anim)
        val animateLogo: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.logo_anim)
        textView2.startAnimation(animateHeading)
        menu_btn.startAnimation(animateHeading)
        imageView.startAnimation(animateLogo)
        chat.startAnimation(animateButtonChat)
        about.startAnimation(animateButtonAbout)

        // Test device for testing purposes.
        // Not intended for production builds.
        val testDeviceIds =
            listOf("5D4AF9D840DDE3EAE66D464C754BF20D", "83CF9B4C6D12079FEB5BA7155E48C9E6")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        // Load ads.
        val adView = findViewById<AdView>(R.id.adView)
        val adReq = AdRequest.Builder().build()
        adView.loadAd(adReq)

        // This opens the virtual keyboard automatically on app start.
        phnNum1.requestFocus()

        // This automatically pulls and writes the country code in input box.
        val countryCodes: Array<String> = resources.getStringArray(R.array.DialingCountryCode)
        val tm =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue = tm.networkCountryIso.toUpperCase(Locale.ROOT)
        var countryDialCode = ""
        for (i in countryCodes.indices) {
            val arrDial: List<String> = countryCodes[i].split(",")
            if (arrDial[1].trim { it <= ' ' } == countryCodeValue.trim()) {
                countryDialCode = arrDial[0]
                break
            }
        }
        val setCode = "+$countryDialCode"
        phnNum1.setText(setCode)
        val phnPos = phnNum1.length()
        phnNum1.setSelection(phnPos)

        // This enables the formatting of the phone number on-the-fly.
        phnNum1.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {})

        // This changes the virtual keyboard's "return key" to "send",
        // and leads the user to WhatsApp.
        findViewById<EditText>(R.id.phnNum1).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    start()
                    true
                }
                else -> false
            }
        }

        // This enables the about button on main screen, Part 1.
        about.setOnClickListener { Menu.MenuClicks.about(this) }

        // This enables the telegram button to open my support group.
        val telegram = findViewById<ImageView>(R.id.telegram)
        telegram.setOnClickListener {
            Menu.MenuClicks.supportClick(this)
        }

        // This enables the share button with a cool message.
        val shr = findViewById<ImageView>(R.id.share)
        shr.setOnClickListener {
            Menu.MenuClicks.shareClick(this)
        }

        // This enables the three-dot menu on top-right corner, Part 1.
        menu_btn.setOnClickListener {
            threeDot()
        }

        // This leads the user to WhatsApp, Part 1.
        chat.setOnClickListener {
            adView.loadAd(adReq)
            start()
        }
    }

    // This leads the user to WhatsApp, Part 2.
    private fun start() {
        // The following checks if validity of the input number,
        // and prevents the further action if needed.
        when {
            !(phnNum1.text.startsWith("+")) -> {
                Toast.makeText(applicationContext, "Check Country Code", Toast.LENGTH_LONG)
                    .show()
                phnNum1.requestFocus()
            }
            (phnNum1.length() < 6) -> {
                Toast.makeText(applicationContext, "Check Phone Number!", Toast.LENGTH_LONG)
                    .show()
                phnNum1.requestFocus()
            }
            else -> {
                val urlPhn1 = phnNum1.text.toString()
                val go = "https://api.whatsapp.com/send?phone=$urlPhn1"
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(go)
                startActivity(openURL)
            }
        }
    }

    // This enables the three-dot menu on top-right corner, Part 2.
    // Here options are different from about screen.
    private fun threeDot() {
        val menuPopup = PopupMenu(this, menu_btn)
        menuPopup.inflate(R.menu.menu)
        menuPopup.menu.removeItem(R.id.home)
        menuPopup.show()
        val preferences = Preferences(this)
        menuPopup.setOnMenuItemClickListener {
            when (it!!.itemId) {
                R.id.sysDef -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    //findViewById<RadioButton>(R.id.sysDef).setChecked(true)
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
                R.id.about -> {
                    Menu.MenuClicks.about(this)
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



