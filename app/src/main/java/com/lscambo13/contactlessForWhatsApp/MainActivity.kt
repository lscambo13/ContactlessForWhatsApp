package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
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
import kotlin.Int as Int1

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


        val testDeviceIds = listOf("5D4AF9D840DDE3EAE66D464C754BF20D")
        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
        MobileAds.setRequestConfiguration(configuration)

        val adView = findViewById<AdView>(R.id.adView)
        val adReq = AdRequest.Builder().build()
        adView.loadAd(adReq)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }
            override fun onAdFailedToLoad(errorCode: Int1) {
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
        cCode.requestFocus()
        val countryCodes = resources.getStringArray(R.array.country_code)

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
            ) {
            }
            override fun afterTextChanged(s: Editable) {
                /*if (cCode.length() == 3) {
                  *  phnNum1.requestFocus()
                }*/
            }
        })

        phnNum1.addTextChangedListener(object : TextWatcher {

            //TODO - - IMPLEMENT PHONENUMBER UTILS
            //TODO - - INCREASE WORD LIMIT

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

        findViewById<EditText>(R.id.cCode).setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    phnNum1.requestFocus()
                    true
                }
                else -> false
            }
        }

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
            TextUtils.isEmpty(cCode.text) -> {
                Toast.makeText(applicationContext, "Country Code missing!", Toast.LENGTH_LONG)
                    .show()
                cCode.requestFocus()
            }
            TextUtils.isEmpty(phnNum1.text) -> {
                Toast.makeText(
                    applicationContext,
                    "Enter a valid Phone number.",
                    Toast.LENGTH_SHORT
                ).show()
                phnNum1.requestFocus()
            }
            else -> {
                phnNum1.text?.replace("\\s".toRegex(), "")
                val cCode = cCode.text.toString()
                val urlPhn1 = phnNum1.text.toString()
                val go = "https://api.whatsapp.com/send?phone=$cCode$urlPhn1"
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








