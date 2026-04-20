package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Find the main layout after setContentView
        val mainView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.btnBrowse).setOnClickListener {
            startActivity(Intent(this, EventsActivity::class.java))
        }

        findViewById<Button>(R.id.btnBookings).setOnClickListener {
            Toast.makeText(this, "My Bookings coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnNotifications).setOnClickListener {
            Toast.makeText(this, "Notifications coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnProfile).setOnClickListener {
            Toast.makeText(this, "Profile coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btnRegisterFeatured).setOnClickListener {
            // Find the featured event from EventData and pass it
            val featuredEvent = EventData.events.firstOrNull { it.id == 1 } // Assuming ID 1 is the workshop
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("event", featuredEvent)
            startActivity(intent)
        }
    }
}