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
import androidx.core.view.ViewCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.lscambo13.contactlessForWhatsApp.Menu.MenuClicks.makeStatusBarTransparent
import com.lscambo13.contactlessForWhatsApp.Menu.MenuClicks.setMarginTop
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.system.exitProcess

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Experiment for transparent statusbar
        makeStatusBarTransparent()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView2)) { _, insets ->
            findViewById<TextView>(R.id.topGuide).setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }

        // This section detects if the app is running for the first time.
        // Int 0 = new user
        val isFirstRun: Int = Preferences(this).getFirstRun()
        when (isFirstRun) {
            (0) -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.welcomeTitle)
                builder.setMessage(R.string.welcomeMessage)
                builder.setPositiveButton("Continue") { _, _ ->
                    Preferences(this).setFirstRun(BuildConfig.VERSION_CODE)
                }
                builder.setNegativeButton("Source code") { _, _ ->
                    Preferences(this).setFirstRun(BuildConfig.VERSION_CODE)
                    Menu.MenuClicks.github(this)
                }
                builder.setNeutralButton("What's New") { _, _ ->
                    Preferences(this).setFirstRun(BuildConfig.VERSION_CODE)
                    Menu.MenuClicks.changelog(this)
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
                builder.setNeutralButton("More on GitHub") { _, _ ->
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



