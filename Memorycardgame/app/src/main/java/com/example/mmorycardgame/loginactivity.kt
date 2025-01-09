package com.example.mmorycardgame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find views
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etGameId: EditText = findViewById(R.id.etGameId)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        // Get SharedPreferences instance to retrieve data
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE)

        // Handle Login button click
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val gameId = etGameId.text.toString()

            // Retrieve saved details from SharedPreferences
            val savedEmail = sharedPreferences.getString("Email", "")
            val savedGameId = sharedPreferences.getString("GameId", "")

            // Check if the entered email and game ID match the saved details
            if (email == savedEmail && gameId == savedGameId) {
                // Show Login Successful message
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Login Successful!")
                    .setPositiveButton("OK") { dialog, _ ->
                        // Navigate to the Levels page after clicking OK
                        val intent = Intent(this, LevelsActivity::class.java)
                        startActivity(intent)
                    }
                builder.create().show()
            } else {
                // Show Invalid Credentials message
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Invalid Credentials!")
                    .setPositiveButton("OK", null)
                builder.create().show()
            }
        }

        // Handle Sign Up button click
        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}