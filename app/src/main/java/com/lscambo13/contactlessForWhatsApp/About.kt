package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.RequestConfiguration

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

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

        val close: Button = findViewById(R.id.closeAbt)
        val newVer: Button = findViewById(R.id.DwnNewVer)
        val shr: ImageView = findViewById(R.id.share)
        val telegram: ImageView = findViewById(R.id.telegram)
        val summary: TextView = findViewById(R.id.summary)
        val aboutText = getText(R.string.about_text)

        close.setOnClickListener {
            finish()
        }

        newVer.setOnClickListener {
            val updateLink = "https://bit.ly/3gWV4rL"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(updateLink)

            // Start loading the ad in the background.
            adView.loadAd(adReq)
            startActivity(openURL)
        }

        telegram.setOnClickListener {
            val supportLink = "https://t.me/lscambo13_projects"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(supportLink)
            startActivity(openURL)
        }

        shr.setOnClickListener {
            intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val message = "Check out this amazing app that lets you chat with someone on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
        }

        summary.setOnClickListener {

            // Start loading the ad in the background.
            adView.loadAd(adReq)

            if(summary.text != getString(R.string.easter_egg))
                summary.text = getString(R.string.easter_egg)
            else
                summary.text = aboutText.toString()

        }
    }
}

