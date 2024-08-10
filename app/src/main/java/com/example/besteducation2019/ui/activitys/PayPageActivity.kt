package com.example.besteducation2019.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.besteducation2019.R

class PayPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_page)

        val webView = findViewById<WebView>(R.id.webView)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable dark mode

        val url = intent.getStringExtra("URL")

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        if (url != null) {
            webView.loadUrl(url)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
        finish()


    }

}