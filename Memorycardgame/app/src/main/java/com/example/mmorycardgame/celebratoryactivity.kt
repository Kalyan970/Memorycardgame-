package com.example.mmorycardgame

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mcg.NormalLevelActivity

class CelebratoryActivity : AppCompatActivity() {

    private lateinit var replayButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_celebratory)

        replayButton = findViewById(R.id.replayButton)
        exitButton = findViewById(R.id.exitButton)

        replayButton.setOnClickListener {
            // Replay the game (this could be the first level or last level)
            val intent = Intent(this, NormalLevelActivity::class.java)
            startActivity(intent)
            finish()
        }

        exitButton.setOnClickListener {
            // Exit the app
            finishAffinity()  // Close all activities
        }
    }
}