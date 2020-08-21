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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    //TODO - -MAKE LOAD METHOD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        val animateHeading: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.heading_anim)
        val animateButtonChat: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.button_chat_anim)
        val animateButtonAbout: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.button_about_anim)
        val animateLogo: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.logo_anim)

        textView2.startAnimation(animateHeading)
        menu.startAnimation(animateHeading)
        imageView.startAnimation(animateLogo)
        chat.startAnimation(animateButtonChat)
        about.startAnimation(animateButtonAbout)


        val testDeviceIds = listOf("5D4AF9D840DDE3EAE66D464C754BF20D", "83CF9B4C6D12079FEB5BA7155E48C9E6")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        val adView = findViewById<AdView>(R.id.adView)
        val adReq = AdRequest.Builder().build()
        adView.loadAd(adReq)

        adView.adListener = object : AdListener() {
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
        }

        val shr = findViewById<ImageView>(R.id.share)
        val telegram = findViewById<ImageView>(R.id.telegram)
        phnNum1.requestFocus()
        val countryCodes:Array<String> = resources.getStringArray(R.array.DialingCountryCode)

        /*
        cCode.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(
                s: CharSequence,
                start: Int1,
                before: Int1,
                count: Int1
            ) {
                if(cCode.text.isNotEmpty())
                    if(cCode.text.toString() in countryCodes)
                       phnNum1.requestFocus()
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int1, count: Int1,
                after: Int1
            ) {}
            override fun afterTextChanged(s: Editable) {}
        })
         */


        val tm =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCodeValue = tm.networkCountryIso.toUpperCase(Locale.ROOT)
        var countryDialCode = ""
        for (i in countryCodes.indices) {
            val arrDial: List<String> = countryCodes.get(i).split(",")
            if (arrDial[1].trim { it <= ' ' } == countryCodeValue.trim()) {
                countryDialCode = arrDial[0]
                break
            }
        }

        val setCode = "+$countryDialCode"
        phnNum1.setText(setCode)
        val phnPos = phnNum1.length()
        phnNum1.setSelection(phnPos)

        phnNum1.addTextChangedListener(object : PhoneNumberFormattingTextWatcher(){

            /*
            override fun onTextChanged(
                s: CharSequence,
                start: Int1,
                before: Int1,
                count: Int1
            ) {
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int1, count: Int1,
                after: Int1
            ) {
            }
            override fun afterTextChanged(s: Editable) {
                if(phnNum1.text.toString().isEmpty()) {
                    cCode.requestFocus()
                }
            }

             */
        })


        findViewById<EditText>(R.id.phnNum1).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    start()
                    true
                }
                else -> false
            }
        }

        /*
        findViewById<EditText>(R.id.cCode).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    phnNum1.requestFocus()
                    true
                }
                else -> false
            }
        }

         */

        about.setOnClickListener { about() }

        telegram.setOnClickListener {
            val supportLink = "https://t.me/lscambo13_projects"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(supportLink)
            startActivity(openURL)
        }

        shr.setOnClickListener {
            intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val message =
                "Check out this amazing app that lets you chat with others on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
        }

        menu.setOnClickListener {
            menuClick()
        }

        // call via button
        chat.setOnClickListener {
            // Start loading the ad in the background.
            adView.loadAd(adReq)
            start()
        }
    }


    private fun start() {
        when {
            !(phnNum1.text.startsWith("+") || phnNum1.text.startsWith("00")) -> {
                Toast.makeText(applicationContext, "Country Code missing!", Toast.LENGTH_LONG)
                    .show()
                phnNum1.requestFocus()
            }
            /* TextUtils.isEmpty(phnNum1.text) -> {
                Toast.makeText(
                    applicationContext,
                    "Enter a valid Phone number.",
                    Toast.LENGTH_SHORT
                ).show()
                phnNum1.requestFocus()
            }*/
            else -> {
                //phnNum1.text?.replace("\\s".toRegex(), "")
                //val cCode = cCode.text.toString()
                val urlPhn1 = phnNum1.text.toString()
                val go = "https://api.whatsapp.com/send?phone=$urlPhn1"
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(go)
                startActivity(openURL)
            }
        }
    }

    private fun about() {
        startActivity(Intent(this, About::class.java))
    }

    private fun menuClick() {

        val menuPopup = PopupMenu(this, menu)
        menuPopup.inflate(R.menu.menu)
        menuPopup.show()

        menuPopup.setOnMenuItemClickListener {
            when(it!!.itemId){
                R.id.sysDef -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    save()
                }
                R.id.day -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    save()
                }
                R.id.night ->
                {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    save()}
            }
            true
        }
    }

    private fun save() {
        //TODO - - MAKE SAVE METHOD
    }
}








