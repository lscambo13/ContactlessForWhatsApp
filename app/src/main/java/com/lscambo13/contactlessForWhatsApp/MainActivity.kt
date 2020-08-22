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
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val isFirstRun: Boolean = Preferences(this).getFirstRun()
        when {
            (isFirstRun) -> {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle(R.string.dialogTitle)
                //set message for alert dialog
                builder.setMessage(R.string.dialogMessage)
                //builder.setIcon(android.R.drawable.ic_dialog_info)
                //performing positive action
                builder.setPositiveButton("Continue") { _, _ ->
                    Preferences(this).setFirstRun(false)}
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }


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


        /* val testDeviceIds = listOf("5D4AF9D840DDE3EAE66D464C754BF20D", "83CF9B4C6D12079FEB5BA7155E48C9E6")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)*/

        val adView = findViewById<AdView>(R.id.adView)
        val adReq = AdRequest.Builder().build()
        adView.loadAd(adReq)

        /*adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }
            override fun onAdFailedToLoad(errorCode: Int) {
                adView.loadAd(adReq)
            }
            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }
            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }
            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }*/

        val shr = findViewById<ImageView>(R.id.share)
        val telegram = findViewById<ImageView>(R.id.telegram)
        phnNum1.requestFocus()
        val countryCodes:Array<String> = resources.getStringArray(R.array.DialingCountryCode)

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

        phnNum1.addTextChangedListener(object : PhoneNumberFormattingTextWatcher(){})

        findViewById<EditText>(R.id.phnNum1).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    start()
                    true
                }
                else -> false
            }
        }

        about.setOnClickListener { about() }

        telegram.setOnClickListener {
            supportClick()
        }

        shr.setOnClickListener {
            shareClick()
        }

        menu_btn.setOnClickListener {
            menuClick()
        }

        chat.setOnClickListener {

            adView.loadAd(adReq)
            start()
        }
    }


    fun start() {
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

    fun about() {
        startActivity(Intent(this, About::class.java))
    }

    fun menuClick() {
        val menuPopup = PopupMenu(this, menu_btn)
        menuPopup.inflate(R.menu.menu)
        menuPopup.show()
        val preferences = Preferences(this)
        menuPopup.setOnMenuItemClickListener {
            when(it!!.itemId){
                R.id.sysDef -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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
                    about()
                    true
                }
                R.id.changelog -> {
                    changelog()
                    true
                }
                else -> false
            }

        }
    }

    fun shareClick(){
        intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val message =
            "Check out this amazing app that lets you chat with others on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
    }

    fun supportClick() {
        val supportLink = "https://t.me/lscambo13_projects"
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(supportLink)
        startActivity(openURL)
    }

    fun changelog() {
        val changelogLink = "https://github.com/lscambo13/ContactlessForWhatsApp/commits/master"
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(changelogLink)
        startActivity(openURL)
    }

}








