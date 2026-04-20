package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val event = intent.getSerializableExtra("event") as Event

        findViewById<ImageView>(R.id.eventImage).setImageResource(event.imageRes)
        findViewById<TextView>(R.id.title).text = event.title
        findViewById<TextView>(R.id.description).text = event.description
        findViewById<TextView>(R.id.eventInfo).text = "Date: ${event.date} | Venue: ${event.venue}"

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val intent = Intent(this, SeatActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }
}