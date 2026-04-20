package com.example.universityeventapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val event = intent.getSerializableExtra("event") as Event
        val seatCount = intent.getIntExtra("seatCount", 0)
        val totalPrice = seatCount * event.price

        findViewById<TextView>(R.id.txtEventTitle).text = event.title
        findViewById<TextView>(R.id.txtSeatCount).text = "Seats Booked: $seatCount"
        findViewById<TextView>(R.id.txtTotalPrice).text = "Total Price: $$totalPrice"

        findViewById<Button>(R.id.btnPay).setOnClickListener {
            Toast.makeText(this, "Booking confirmed!", Toast.LENGTH_LONG).show()
            finishAffinity() // For simple flow, reset to start
        }
    }
}