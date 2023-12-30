package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date

var chosenDate: Date = Date()
class DateHistoryActivity : AppCompatActivity()
{
//    val chosedDate = Date(intent.getStringExtra("chosedData"))

    lateinit var listView: RecyclerView
    lateinit var adapter: EventAdapter
    lateinit var dbHelper: EventHistoryDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_date_history)

        dbHelper = EventHistoryDatabaseHelper(this, null)
//        val events = dbHelper.getEventsByGivenDate(chosedDate)
     //   val events = dbHelper.getAllEvents()
        val events = dbHelper.getEventsByDate(chosenDate)
        events.reverse()
        Log.d("Bebr", dbHelper.getCount().toString())
        Log.d("Bebr in", events.count().toString())
        Log.d("Bebr date", chosenDate.toString())
        adapter = EventAdapter(events, this)
        listView = findViewById(R.id.events_list_history)

        setBackToMenu()
        setEventsList(adapter)
    }

    private fun setEventsList(adapter: EventAdapter) {
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter

        adapter.onClick = { event, view ->
            null
        }
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_dateHistory)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}