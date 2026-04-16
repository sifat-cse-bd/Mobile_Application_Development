package com.example.learningportalapp

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var etUrl: EditText

    private lateinit var btnBack: Button
    private lateinit var btnForward: Button
    private lateinit var btnRefresh: Button
    private lateinit var btnHome: Button
    private lateinit var btnGo: Button

    private lateinit var btnGoogle: Button
    private lateinit var btnYouTube: Button
    private lateinit var btnWiki: Button
    private lateinit var btnKhan: Button
    private lateinit var btnUniversity: Button

    private val homeUrl = "https://www.google.com"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        etUrl = findViewById(R.id.etUrl)

        btnBack = findViewById(R.id.btnBack)
        btnForward = findViewById(R.id.btnForward)
        btnRefresh = findViewById(R.id.btnRefresh)
        btnHome = findViewById(R.id.btnHome)
        btnGo = findViewById(R.id.btnGo)

        btnGoogle = findViewById(R.id.btnGoogle)
        btnYouTube = findViewById(R.id.btnYouTube)
        btnWiki = findViewById(R.id.btnWiki)
        btnKhan = findViewById(R.id.btnKhan)
        btnUniversity = findViewById(R.id.btnUniversity)


        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true


        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                progressBar.visibility = View.GONE
                etUrl.setText(url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                view.loadUrl("file:///android_asset/offline.html")
            }
        }


        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar.progress = newProgress
            }
        }


        loadUrl(homeUrl)


        btnBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                Toast.makeText(this, "No more history", Toast.LENGTH_SHORT).show()
            }
        }

        btnForward.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            }
        }

        btnRefresh.setOnClickListener {
            webView.reload()
        }

        btnHome.setOnClickListener {
            loadUrl(homeUrl)
        }

        btnGo.setOnClickListener {
            loadUrl(etUrl.text.toString())
        }


        etUrl.setOnEditorActionListener { _, _, _ ->
            loadUrl(etUrl.text.toString())
            true
        }


        btnGoogle.setOnClickListener { loadUrl("https://www.google.com") }
        btnYouTube.setOnClickListener { loadUrl("https://www.youtube.com") }
        btnWiki.setOnClickListener { loadUrl("https://www.wikipedia.org") }
        btnKhan.setOnClickListener { loadUrl("https://www.khanacademy.org") }
        btnUniversity.setOnClickListener { loadUrl("https://www.aiub.edu") }
    }


    fun loadUrl(url: String) {
        if (!isOnline()) {
            webView.loadUrl("file:///android_asset/offline.html")
            return
        }

        var fixedUrl = url.trim()
        if (!fixedUrl.startsWith("http://") && !fixedUrl.startsWith("https://")) {
            fixedUrl = "https://$fixedUrl"
        }
        webView.loadUrl(fixedUrl)
    }


    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isOnline(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetworkInfo
        return network != null && network.isConnected
    }


    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}



