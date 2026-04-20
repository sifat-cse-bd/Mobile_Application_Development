package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EventsActivity : AppCompatActivity() {

    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = EventAdapter(EventData.events) {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("event", it)
            startActivity(intent)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}