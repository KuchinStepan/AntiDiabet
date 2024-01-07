package com.example.antidiabet1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date

class DateHistoryActivity : AppCompatActivity()
{
    lateinit var listView: RecyclerView
    lateinit var adapter: EventAdapter
    lateinit var dbHelper: EventHistoryDatabaseHelper
    lateinit var chosenDate: Date

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_date_history)
        dbHelper = EventHistoryDatabaseHelper(this, null)
        adapter = EventAdapter(ArrayList(), this)
        listView = findViewById(R.id.events_list_history)
        setSelectDateButton()
        chosenDate = Date()

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

    override fun onBackPressed()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setSelectDateButton()
    {
        val dateButton: Button = findViewById(R.id.select_date)

        dateButton.setOnClickListener() {
            showCalendarDialog(this)
        }
    }

    private fun updateList(chosenDate: Date)
    {
//        val events = dbHelper.getEventsByGivenDate(chosedDate)
        //   val events = dbHelper.getAllEvents()
        val events = dbHelper.getEventsByDate(chosenDate)
        events.reverse()
        Log.d("Bebr", dbHelper.getCount().toString())
        Log.d("Bebr in", events.count().toString())
        Log.d("Bebr date", chosenDate.toString())
        adapter.changeList(events)
    }

    private fun showCalendarDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_calendar_choice)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val dateSelectButton: Button = dialog.findViewById(R.id.selectDate)
        dateSelectButton.setOnClickListener() {
            updateList(chosenDate)
            dialog.cancel()
        }
        val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            chosenDate = Date(year - 1900, month, dayOfMonth)
        }

        dialog.show()
    }
}