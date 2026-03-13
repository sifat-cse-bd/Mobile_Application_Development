package com.example.loginprofileapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find Views
        val loginForm = findViewById<RelativeLayout>(R.id.loginForm)
        val profileCard = findViewById<RelativeLayout>(R.id.profileCard)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val etUser = findViewById<EditText>(R.id.txtUsername)
        val etPass = findViewById<EditText>(R.id.txtPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val tvForgot = findViewById<TextView>(R.id.tvForgotPass)

        // Login Logic
        btnLogin.setOnClickListener {
            val user = etUser.text.toString()
            val pass = etPass.text.toString()

            if (user == "admin" && pass == "1234") {
                loginForm.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                // Simulate loading for 1.5 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    profileCard.visibility = View.VISIBLE
                }, 1500)
            } else {
                Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show()
            }
        }

        // Logout Logic
        btnLogout.setOnClickListener {
            profileCard.visibility = View.GONE
            loginForm.visibility = View.VISIBLE
            etUser.text.clear()
            etPass.text.clear()
        }

        // Forgot Password Logic
        tvForgot.setOnClickListener {
            Toast.makeText(this, "Password reset link sent to your email", Toast.LENGTH_LONG).show()
        }
    }
}