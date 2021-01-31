package com.lscambo13.contactlessForWhatsApp

import android.content.Context
import android.content.Intent
import android.net.Uri
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
            val changelogLink = "https://github.com/lscambo13/ContactlessForWhatsApp/commits/master"
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

    }
}