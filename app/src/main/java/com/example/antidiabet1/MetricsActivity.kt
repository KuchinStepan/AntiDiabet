package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.example.antidiabet1.dialog_helpers.MainInsulinDialogs
import com.example.antidiabet1.dialog_helpers.MainSugarDialogs
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date

class MetricsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metrics)
        setBackToMenu()

        val opensText: TextView = findViewById<TextView?>(R.id.login_metric)
        opensText.text = getSharedPreferences(packageName, MODE_PRIVATE)
            .getInt("app_opened_count", 1).toString()

        var foodCount = 0
        var insulinCount = 0
        var sugarCount = 0

        val dbHelper = EventHistoryDatabaseHelper(this, null)
        for (event in dbHelper.getAllEvents()){
            if (event.type == EventType.Eating)
                foodCount += 1
            else if (event.type == EventType.InsulinInjection)
                insulinCount += 1
            else if (event.type == EventType.SugarMeasure) sugarCount += 1
        }

        val foodText: TextView = findViewById<TextView?>(R.id.food_metric)
        foodText.text = foodCount.toString()

        val insulinText: TextView = findViewById<TextView?>(R.id.insulin_metric)
        insulinText.text = insulinCount.toString()

        val sugarText: TextView = findViewById<TextView?>(R.id.sugar_metric)
        sugarText.text = sugarCount.toString()
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_metrics)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}