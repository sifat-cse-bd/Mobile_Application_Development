package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView

class MainActivity : AppCompatActivity() {

    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val scrollView = findViewById<NestedScrollView>(R.id.scrollView)

        val intro = findViewById<TextView>(R.id.sectionIntro)
        val key = findViewById<TextView>(R.id.sectionKey)
        val analysis = findViewById<TextView>(R.id.sectionAnalysis)
        val conclusion = findViewById<TextView>(R.id.sectionConclusion)

        val btnIntro = findViewById<Button>(R.id.btnIntro)
        val btnKey = findViewById<Button>(R.id.btnKey)
        val btnAnalysis = findViewById<Button>(R.id.btnAnalysis)
        val btnConclusion = findViewById<Button>(R.id.btnConclusion)
        val btnTop = findViewById<Button>(R.id.btnTop)

        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)


        btnIntro.setOnClickListener {
            scrollView.smoothScrollTo(0, intro.top)
        }

        btnKey.setOnClickListener {
            scrollView.smoothScrollTo(0, key.top)
        }

        btnAnalysis.setOnClickListener {
            scrollView.smoothScrollTo(0, analysis.top)
        }

        btnConclusion.setOnClickListener {
            scrollView.smoothScrollTo(0, conclusion.top)
        }

        // BACK TO TOP
        btnTop.setOnClickListener {
            scrollView.smoothScrollTo(0, 0)
        }


        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked

            if (isBookmarked) {
                btnBookmark.setImageResource(android.R.drawable.btn_star_big_on)
                Toast.makeText(this, "Article Bookmarked", Toast.LENGTH_SHORT).show()
            } else {
                btnBookmark.setImageResource(android.R.drawable.btn_star_big_off)
                Toast.makeText(this, "Bookmark Removed", Toast.LENGTH_SHORT).show()
            }
        }

        
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this article: AI is Changing the Future"
            )
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }
}