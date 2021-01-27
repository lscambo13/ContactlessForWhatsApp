package com.lscambo13.contactlessForWhatsApp

import android.content.Context
import android.content.Intent
import android.net.Uri


class Menu {

    object MenuClicks {
        fun shareClick(context: Context? ) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val message =
                "Check out this amazing app that lets you chat with others on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            //context?.startActivity(intent)
            context?.startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
            //context?.startActivity(intent)
        }

        fun supportClick(context : Context) {
            val supportLink = "https://t.me/lscambo13_projects"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(supportLink)
            //openURL.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(openURL)
        }
    }

/*
   fun about() {
       C.startActivity(Intent(C, About::class.java))
   }


 un menuClick() {
       val menuPopup = PopupMenu(C, menu)
       menuPopup.inflate(R.menu.menu)
       menuPopup.show()
       val preferences = Preferences(C)
       menuPopup.setOnMenuItemClickListener {
           when(it!!.itemId){
               R.id.sysDef -> {
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                   preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
               }
               R.id.day -> {
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                   preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_NO)
               }
               R.id.night -> {
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                   preferences.setCurrentTheme(AppCompatDelegate.MODE_NIGHT_YES)
               }
               R.id.exit -> {
                   finish()
               }
               R.id.about -> {
                   about()
               }
           }
           true
       }
   }

   */




}