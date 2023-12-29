package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DateHistoryActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_date_history)

        setBackToMenu()
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_dateHistory)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}