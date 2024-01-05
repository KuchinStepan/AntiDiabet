package com.example.antidiabet1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.example.antidiabet1.item_classes.ChosenIngredientAdapter
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date


class MainActivity : AppCompatActivity() {
    lateinit var chosedDate: Date
    lateinit var listView: RecyclerView
    lateinit var adapter: EventAdapter
    lateinit var dbHelper: EventHistoryDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setEventButton()
        setHistoryButton()

        chosenDate = Date()
        dbHelper = EventHistoryDatabaseHelper(this, null)
        val events = dbHelper.getAllEvents()
        events.reverse()
        adapter = EventAdapter(events, this)
        listView = findViewById(R.id.events_list)

        setEventsList(adapter)
    }

    private fun setEventsList(adapter: EventAdapter) {
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter

        adapter.onClick = { event, view ->
            null
        }

        adapter.onLongClick = { event, view ->
            val dialog = Dialog(this)
            if (event.type == EventType.Eating)
                showIngredientDialog(dialog, event)
            else showBasicEditOrDeleteDialog(dialog, event)
        }
    }

    private fun showIngredientDialog(dialog: Dialog, event: Event) {
        val dish = event.dishItem!!
        dialog.setContentView(R.layout.dialog_food_ingredients)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val view: RecyclerView = dialog.findViewById(R.id.ingredients)

        view.layoutManager = LinearLayoutManager(this)
        view.adapter = ChosenIngredientAdapter(dish.ingredients, this)

        val name: TextView = dialog.findViewById(R.id.name)
        name.text = dish.name

        dialog.show()
    }

    private fun showBasicEditOrDeleteDialog(dialog: Dialog, event: Event) {
        dialog.setContentView(R.layout.dialog_edit_or_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val db = EventHistoryDatabaseHelper(this, null)

        val deleteButton: Button = dialog.findViewById(R.id.delete_button)

        deleteButton.setOnClickListener() {
            showDeleteConfirmationDialog(dialog, event)
        }

        val editButton: Button = dialog.findViewById(R.id.edit_button)

        editButton.setOnClickListener() {
            dialog.cancel()
            setSugarChangeFun(this, db, event)
        }

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(dialog: Dialog, event: Event) {
        dialog.setContentView(R.layout.dialog_delete_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        dialog.show()
    }

    private fun updateEventsList(){
        val events: ArrayList<Event> = dbHelper.getEventsByLastThreeDays()
        events.reverse()
        adapter.changeList(events)
    }

    private fun setEventButton() {
        val button: Button = findViewById(R.id.event_button)

        button.setOnClickListener() {
            showEventChoiceDialog(this)
        }
    }

    private fun setHistoryButton() {
        val button: Button = findViewById(R.id.history_button)

        button.setOnClickListener() {
            showCalendarDialog(this)
        }
    }

    private fun showCalendarDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_calendar_choice)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val dateSelectButton: Button = dialog.findViewById(R.id.selectDate)
        dateSelectButton.setOnClickListener() {
            val intent = Intent(this, DateHistoryActivity::class.java)
            startActivity(intent)
        }
        val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            chosenDate = Date(year - 1900, month, dayOfMonth)
        }

        dialog.show()
    }

    private fun showEventChoiceDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_event_choice)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val foodBtt: Button = dialog.findViewById(R.id.get_food)

        foodBtt.setOnClickListener() {
            val intent = Intent(context, SelectionFoodActivity::class.java)
            startActivity(intent)
        }

        val db = EventHistoryDatabaseHelper(context, null)

        val insulinBtt: Button = dialog.findViewById(R.id.get_insulin)
        insulinBtt.setOnClickListener() {
            dialog.cancel()
            setInsulinFun(context, db)
        }

        val sugarBtt: Button = dialog.findViewById(R.id.measure_sugar)
        sugarBtt.setOnClickListener() {
            dialog.cancel()
            setSugarFun(context, db)
        }

        dialog.show()
    }

    private fun setInsulinFun(context: Context, db: EventHistoryDatabaseHelper) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choice_insulin)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()

                val event = Event(Date(), EventType.InsulinInjection, null, grams, 0.0)
                db.addEvent(event)
                updateEventsList()
                dialog.cancel()
            } else {
                Toast.makeText(context, "Введите количество ЕД", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun setSugarChangeFun(context: Context, db: EventHistoryDatabaseHelper, event: Event) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choice_sugar)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()
                event.sugar = grams
                val evs = db.updateEvent(event)

                adapter.changeList(evs)
                dialog.cancel()
            } else {
                Toast.makeText(context, "Введите количество ммоль/л", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun setSugarFun(context: Context, db: EventHistoryDatabaseHelper ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choice_sugar)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()

                val event = Event(Date(), EventType.SugarMeasure, null, 0.0, grams)
                db.addEvent(event)
                updateEventsList()
                dialog.cancel()
            } else {
                Toast.makeText(context, "Введите количество ммоль/л", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}