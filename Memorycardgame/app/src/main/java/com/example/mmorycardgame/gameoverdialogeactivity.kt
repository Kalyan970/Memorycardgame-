package com.example.mmorycardgame

import android.app.Dialog
import android.content.Intent
import android.widget.Button
import com.example.mcg.EasyLevelActivity
import com.example.mcg.NormalLevelActivity

class GameOverDialog(context: EasyLevelActivity) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_game_over)

        val nextLevelButton: Button = findViewById(R.id.btnNextLevel)
        val quitButton: Button = findViewById(R.id.btnQuit)

        nextLevelButton.setOnClickListener {
            val intent = Intent(context, NormalLevelActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }

        quitButton.setOnClickListener {
            context.finish() // Close the app
            dismiss()
        }
    }
}