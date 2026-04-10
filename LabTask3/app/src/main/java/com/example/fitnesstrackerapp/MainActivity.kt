package com.example.fitnesstrackerapp  // <-- Make sure this matches your package name

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var steps = 0
    private val dailyGoal = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvStepsCount = findViewById<TextView>(R.id.tvStepsCount)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvProgressPercent = findViewById<TextView>(R.id.tvProgressPercent)
        val btnUpdateStats = findViewById<Button>(R.id.btnUpdateStats)

        btnUpdateStats.setOnClickListener {
            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_NUMBER

            AlertDialog.Builder(this)
                .setTitle("Update Steps")
                .setMessage("Enter new step count")
                .setView(input)
                .setPositiveButton("Update") { dialog, _ ->
                    val newSteps = input.text.toString().toIntOrNull() ?: 0
                    steps = newSteps
                    tvStepsCount.text = steps.toString()

                    val progress = ((steps.toFloat() / dailyGoal) * 100).toInt()
                    progressBar.progress = progress
                    tvProgressPercent.text = "$progress%"

                    if (progress >= 100) {
                        Toast.makeText(this, "🎉 Goal Achieved!", Toast.LENGTH_SHORT).show()
                    }

                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}