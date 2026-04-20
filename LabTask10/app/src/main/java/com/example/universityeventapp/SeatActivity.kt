package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SeatActivity : AppCompatActivity() {

    private val seats = mutableListOf<Seat>()
    private var selected = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seat)

        val event = intent.getSerializableExtra("event") as Event

        val grid = findViewById<GridView>(R.id.gridView)

        repeat(48) {
            val booked = (0..100).random() < 30
            seats.add(Seat(if (booked) 1 else 0))
        }

        val adapter = SeatAdapter(this, seats) {
            if (it.status == 0) {
                it.status = 2
                selected++
            } else if (it.status == 2) {
                it.status = 0
                selected--
            }
        }

        grid.adapter = adapter

        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            if (selected > 0) {
                val intent = Intent(this, SummaryActivity::class.java)
                intent.putExtra("event", event)
                intent.putExtra("seatCount", selected)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select at least one seat", Toast.LENGTH_SHORT).show()
            }
        }
    }
}