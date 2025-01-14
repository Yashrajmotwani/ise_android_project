package com.example.spscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the splash screen layout
        setContentView(R.layout.activity_splash)

        // Enable edge-to-edge support
        enableEdgeToEdge()

        // Apply window insets for edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Delay transition to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Start MainActivity after 3 seconds
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Finish SplashActivity so the user can't go back to it
        }, 500)
    }}
