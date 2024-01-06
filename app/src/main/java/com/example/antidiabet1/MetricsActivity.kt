package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.dialog_helpers.MainInsulinDialogs
import com.example.antidiabet1.dialog_helpers.MainSugarDialogs
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date

class MetricsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metrics)
        setBackToMenu()
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_metrics)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}