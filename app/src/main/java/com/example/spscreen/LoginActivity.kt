package com.example.spscreen


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Finding the views by ID
        val loginButton: Button = findViewById(R.id.loginButton)
        val emailField: EditText = findViewById(R.id.emailEditText)
        val passwordField: EditText = findViewById(R.id.passwordEditText)

        // Handling login button click
        loginButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            // Simple validation logic
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            } else if (email == "user@gmail.com" && password == "user123") {
                // Successful login
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                // Redirect to Home screen (you can modify this based on your next activity)
                val intent = Intent(this, MainActivity::class.java)  // Change 'HomeActivity' as needed
                startActivity(intent)
                finish()  // Optionally close the login activity
            } else {
                // Failed login
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
