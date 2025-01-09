package com.example.mmorycardgame

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Find views
        val etName: EditText = findViewById(R.id.etName)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etGameId: EditText = findViewById(R.id.etGameId)
        val etPhoneNumber: EditText = findViewById(R.id.etPhoneNumber)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        // Get SharedPreferences instance to store data
        val sharedPreferences: SharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE)

        // Handle Sign Up button click
        btnSignUp.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val gameId = etGameId.text.toString()
            val phoneNumber = etPhoneNumber.text.toString()

            // Check if all fields are filled
            if (name.isNotEmpty() && email.isNotEmpty() && gameId.isNotEmpty() && phoneNumber.isNotEmpty()) {
                // Save details to SharedPreferences
                val editor = sharedPreferences.edit()
                editor.putString("Name", name)
                editor.putString("Email", email)
                editor.putString("GameId", gameId)
                editor.putString("PhoneNumber", phoneNumber)
                editor.apply()

                // Show pop-up message
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Sign Up Successful!")
                    .setPositiveButton("OK") { dialog, _ ->
                        // Navigate to Login page after clicking OK
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                builder.create().show()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}