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
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.Int as Int1


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        phnNum1.requestFocus()

        val shr = findViewById<ImageView>(R.id.share)
        val telgrm = findViewById<ImageView>(R.id.telegram)
        var currS = 0

        // main call function
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
                    phnNum1.text.replace("\\s".toRegex(), "")
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

        phnNum1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int1,
                before: Int1,
                count: Int1
            ) {
                val prevS = currS
                currS = phnNum1.length()

                if (phnNum1.length() == 5)
                    if (phnNum1.text[4] != ' ')
                        phnNum1.text.replace("\\s".toRegex(), "")
                if (phnNum1.length() == 5 && currS > prevS) {
                    phnNum1.text = phnNum1.text.append(' ')
                    phnNum1.setSelection(phnNum1.length())
                }
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int1, count: Int1,
                after: Int1
            ) {
            }
            override fun afterTextChanged(s: Editable) {
                if (phnNum1.length() == 11) {
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

        telgrm.setOnClickListener {
            val supportLink = "https://bit.ly/2ZlCNOT"
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

        // call via button
        chat.setOnClickListener {
            start()
        }
    }
}







