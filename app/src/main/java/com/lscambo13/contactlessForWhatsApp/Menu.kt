package com.lscambo13.contactlessForWhatsApp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog

class Menu {
    object MenuClicks {

        // This opens the share dialog.
        fun shareClick(context: Context?) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val message =
                "Check out this amazing app that lets you chat with others on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            context?.startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
        }

        // This opens the Telegram group.
        fun supportClick(context: Context) {
            val supportLink = "https://t.me/lscambo13_projects"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(supportLink)
            context.startActivity(openURL)
        }

        // This opens the latest changelog dialog.
        fun changelog(context: Context) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.updateTitle)
            builder.setMessage(R.string.updateMessage)
            builder.setPositiveButton("Open Github") { _, _ ->
                // This opens GitHub commits page, Part 1.
                github(context)
            }
            builder.setNegativeButton("Close") { _, _ ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(true)
            alertDialog.show()
        }

        // This opens GitHub commits page, Part 2.
        fun github(context: Context) {
            val changelogLink = "https://github.com/lscambo13/ContactlessForWhatsApp"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(changelogLink)
            context.startActivity(openURL)
        }

        // This opens the about screen.
        fun about(context: Context) {
            context.startActivity(Intent(context, About::class.java))
        }

        // This opens the home screen.
        fun home(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }

        // Experiment for transparent statusbar
        fun Activity.makeStatusBarTransparent() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.apply {
                    clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val mode =
                            context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)
                        when (mode) {
                            Configuration.UI_MODE_NIGHT_YES -> {
                                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            }
                            Configuration.UI_MODE_NIGHT_NO -> {
                                decorView.systemUiVisibility =
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                            }
                            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                                decorView.systemUiVisibility =
                                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                            }
                        }
                    } else {
                        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    }
                    statusBarColor = Color.TRANSPARENT
                }
            }
        }

        fun View.setMarginTop(marginTop: Int) {
            val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
            menuLayoutParams.setMargins(0, marginTop, 0, 0)
            this.layoutParams = menuLayoutParams
        }

    }
}