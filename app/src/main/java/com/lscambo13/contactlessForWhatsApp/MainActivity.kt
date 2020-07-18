package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.RequestConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Int as Int1


class MainActivity : AppCompatActivity() {

    fun start() {
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

    fun about() {
        val startAbt = Intent(this, About::class.java)
        startActivity(startAbt)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //MobileAds.initialize(this) {}

        val adView = findViewById<AdView>(R.id.adView)

        /*MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("D50F398C3BFED617020627C107525FB1"))
                .build()
        )*/

        // Create an ad request.
        val adReq = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adReq)

        val shr = findViewById<ImageView>(R.id.share)
        val telegram = findViewById<ImageView>(R.id.telegram)
        cCode.requestFocus()

        // main call function


        cCode.addTextChangedListener(object : TextWatcher {
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
                if (cCode.length() == 3) {
                    phnNum1.requestFocus()
                }
            }
        })

        phnNum1.addTextChangedListener(object : TextWatcher {
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
                if (phnNum1.length() == 12) {
                    start()
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
            val message = "Check out this amazing app that lets you chat with others on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
        }

        // call via button
        chat.setOnClickListener {
            // Start loading the ad in the background.
            adView.loadAd(adReq)
            start()
        }
    }
}







