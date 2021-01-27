package com.lscambo13.contactlessForWhatsApp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlinx.android.synthetic.main.activity_about.*

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val adView = findViewById<AdView>(R.id.adView)
        val adReq = AdRequest.Builder().build()
        adView.loadAd(adReq)

        val close: Button = findViewById(R.id.closeAbt)
        val newVer: Button = findViewById(R.id.DwnNewVer)
        val shr: ImageView = findViewById(R.id.share)
        val telegram: ImageView = findViewById(R.id.telegram)
        val summary: TextView = findViewById(R.id.summary)

        close.setOnClickListener {
            onBackPressed()
        }

        newVer.setOnClickListener {
            val updateLink = "https://bit.ly/3gWV4rL"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(updateLink)

            adView.loadAd(adReq)
            startActivity(openURL)
        }

        telegram.setOnClickListener {
            Menu.MenuClicks.supportClick(this)
        }

        shr.setOnClickListener {
            Menu.MenuClicks.shareClick(this)
        }

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

        menu_btn.setOnClickListener {
            //menu_btn.visibility = View.GONE
            //mainActivity.menuClick()
        }
    }
}

