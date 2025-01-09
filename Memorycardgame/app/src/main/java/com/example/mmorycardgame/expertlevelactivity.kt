package com.example.mmorycardgame

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ExpertLevelActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var scoreTextView: TextView
    private lateinit var timerTextView: TextView // Added for the timer
    private var cardValues = mutableListOf<String>()
    private var cardButtons = mutableListOf<Button>()
    private var flippedCards = mutableListOf<Button>()
    private var matchedCards = mutableListOf<String>()
    private var score = 0

    private lateinit var correctSound: MediaPlayer
    private lateinit var failSound: MediaPlayer
    private lateinit var successSound: MediaPlayer

    private var startTime: Long = 0L
    private val handler = Handler()
    private lateinit var timerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expertlevel)

        gridLayout = findViewById(R.id.gridLayout)
        scoreTextView = findViewById(R.id.score)
        timerTextView = findViewById(R.id.timer) // Add a TextView in the XML for the timer

        // Initialize sounds
        correctSound = MediaPlayer.create(this, R.raw.correct)
        failSound = MediaPlayer.create(this, R.raw.fail) // Add fail sound here
        successSound = MediaPlayer.create(this, R.raw.success)

        // Set GridLayout row and column count
        gridLayout.rowCount = 6
        gridLayout.columnCount = 5

        // Center the GridLayout
        val layoutParams = gridLayout.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
        layoutParams.topToTop = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.startToStart = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        layoutParams.endToEnd = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
        gridLayout.layoutParams = layoutParams

        // Initialize card values (pairs of emojis)
        val emojis = listOf("🍎", "🥝", "😸", "🎁", "🧸", "🥥", "🎉", "🍇", "🦋", "🍔", "🍰", "😂", "🍓", "😺", "🥪")
        cardValues = (emojis + emojis).shuffled().toMutableList()

        // Create buttons for cards
        for (i in 0 until 30) {
            val button = Button(this)
            button.text = ""
            button.setBackgroundResource(R.drawable.card_front)

            // Adjust button size and margins
            val cardParams = GridLayout.LayoutParams()
            cardParams.width = 0
            cardParams.height = 0
            cardParams.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            cardParams.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            cardParams.setMargins(8, 8, 8, 8) // Add spacing between cards
            button.layoutParams = cardParams

            button.setOnClickListener { onCardClicked(button, i) }
            cardButtons.add(button)
            gridLayout.addView(button)
        }

        // Start the timer
        startTimer()
        updateScore()
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        timerRunnable = object : Runnable {
            override fun run() {
                val elapsedTime = SystemClock.elapsedRealtime() - startTime
                val seconds = (elapsedTime / 1000).toInt() % 60
                val minutes = (elapsedTime / 1000 / 60).toInt()
                timerTextView.text = String.format("Time: %02d:%02d", minutes, seconds)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(timerRunnable)
    }

    private fun stopTimer(): String {
        handler.removeCallbacks(timerRunnable)
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val seconds = (elapsedTime / 1000).toInt() % 60
        val minutes = (elapsedTime / 1000 / 60).toInt()
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun onCardClicked(button: Button, index: Int) {
        if (flippedCards.size < 2 && !flippedCards.contains(button)) {
            button.text = cardValues[index]
            button.setBackgroundResource(R.drawable.card_back)
            flippedCards.add(button)

            if (flippedCards.size == 2) {
                checkForMatch()
            }
        }
    }

    private fun checkForMatch() {
        val firstCard = flippedCards[0]
        val secondCard = flippedCards[1]

        val firstCardValue = cardValues[cardButtons.indexOf(firstCard)]
        val secondCardValue = cardValues[cardButtons.indexOf(secondCard)]

        if (firstCardValue == secondCardValue) {
            // Match found
            correctSound.start()
            score += 4
            matchedCards.add(firstCardValue)
            updateScore()
            flippedCards.clear()

            if (matchedCards.size == cardValues.size / 2) {
                showEndGameDialog()
            }
        } else {
            // No match, flip back after a delay
            failSound.start() // Play the fail sound on mismatch
            Handler().postDelayed({
                firstCard.text = ""
                secondCard.text = ""
                firstCard.setBackgroundResource(R.drawable.card_front)
                secondCard.setBackgroundResource(R.drawable.card_front)
                flippedCards.clear()
            }, 1000)
        }
    }

    private fun updateScore() {
        scoreTextView.text = "Score: $score"
    }

    private fun showEndGameDialog() {
        // Stop the timer and get the elapsed time
        val elapsedTime = stopTimer()

        // Play success sound
        successSound.start()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")
            .setMessage("Successfully completed Expert Level!\nScore: $score\nTime: $elapsedTime")
            .setCancelable(false)
            .setPositiveButton("Celebrate") { _, _ ->
                goToCelebratory()
            }
            .setNegativeButton("Quit") { _, _ ->
                finish()
            }
        val dialog = builder.create()
        dialog.show()
    }

    private fun goToCelebratory() {
        val intent = Intent(this, CelebratoryActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timerRunnable)
        correctSound.release()
        failSound.release() // Release the fail sound
        successSound.release()
    }
}