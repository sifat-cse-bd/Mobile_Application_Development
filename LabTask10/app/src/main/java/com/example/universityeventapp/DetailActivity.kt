package com.example.universityeventapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.jvm.java

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val event = intent.getSerializableExtra("event") as Event

        findViewById<TextView>(R.id.title).text = event.title

        findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val intent = Intent(this, SeatActivity::class.java)
            intent.putExtra("event", event)
            startActivity(intent)
        }
    }
}