package com.lscambo13.contactlessForWhatsApp

import android.content.Context


class Menu(context: Context) {
    val C = context
/*
    fun supportClick() {
        val supportLink = "https://t.me/lscambo13_projects"
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(supportLink)
        C.startActivity(openURL)
    }

    // TODO - - MENU CLASS


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

   fun shareClick(){
       val intent = Intent(Intent.ACTION_SEND)
       intent.type = "text/plain"
       val message =
           "Check out this amazing app that lets you chat with others on WhatsApp without needing to save their phone number\nbit.ly/3gWV4rL"
       intent.putExtra(Intent.EXTRA_TEXT, message)
       startActivity(Intent.createChooser(intent, "Show Love by Sharing"))
   }
   */




}