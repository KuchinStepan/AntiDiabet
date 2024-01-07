package com.example.antidiabet1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.example.antidiabet1.dialog_helpers.MainInsulinDialogs
import com.example.antidiabet1.dialog_helpers.MainSugarDialogs
import com.example.antidiabet1.item_classes.ChosenIngredientAdapter
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date


class MainActivity : AppCompatActivity() {
    lateinit var chosedDate: Date
    lateinit var listView: RecyclerView
    lateinit var adapter: EventAdapter
    lateinit var dbHelper: EventHistoryDatabaseHelper
    lateinit var sugarDialogs: MainSugarDialogs
    lateinit var insulinDialogs: MainInsulinDialogs
    var isFromCringeActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getStringExtra("startPosition").toString() == "fromCringeActivity")
            isFromCringeActivity = true
        setContentView(R.layout.activity_main)

        setEventButton()
        setHistoryButton()

        chosenDate = Date()
        dbHelper = EventHistoryDatabaseHelper(this, null)
        val events = dbHelper.getAllEvents()
        events.reverse()
        adapter = EventAdapter(events, this)
        listView = findViewById(R.id.events_list)
        sugarDialogs = MainSugarDialogs(this, dbHelper)
        insulinDialogs = MainInsulinDialogs(this, dbHelper)

        setEventsList(adapter)
    }

    override fun onBackPressed() { }

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

        val deleteButton: Button = dialog.findViewById(R.id.dialog_food_ingredients_delete)

        deleteButton.setOnClickListener() {
            dialog.cancel()
            showDeleteConfirmationDialog(dialog, event)
        }

        val editButton: Button = dialog.findViewById(R.id.dialog_food_ingredients_edit)
        editButton.visibility = View.INVISIBLE
        //maybe another time
/*        editButton.setOnClickListener() {
            dialog.cancel()
            val intent = Intent(this, SelectionFoodActivity::class.java)
            intent.putExtra("event_id", event.id)
            startActivity(intent)
        }*/

        dialog.show()
    }

    private fun showBasicEditOrDeleteDialog(dialog: Dialog, event: Event) {
        dialog.setContentView(R.layout.dialog_edit_or_delete)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val deleteButton: Button = dialog.findViewById(R.id.delete_button)

        deleteButton.setOnClickListener() {
            dialog.cancel()
            showDeleteConfirmationDialog(dialog, event)
        }

        val editButton: Button = dialog.findViewById(R.id.edit_button)
        editButton.setOnClickListener() {
            dialog.cancel()
            if (event.type == EventType.SugarMeasure)
                sugarDialogs.changingEventFun(event, adapter)
            else if (event.type == EventType.InsulinInjection)
                insulinDialogs.changingEventFun(event, adapter)
        }

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(dialog: Dialog, event: Event) {
        dialog.setContentView(R.layout.dialog_delete_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val cancelBtt: Button = dialog.findViewById(R.id.delete_cancel_button)
        cancelBtt.setOnClickListener() {
            dialog.cancel()
        }

        val deleteBtt: Button = dialog.findViewById(R.id.delete_confirm_button)
        deleteBtt.setOnClickListener() {
            dbHelper.deleteEvent(event)
            updateEventsList()
            dialog.cancel()
        }

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

        button.setOnLongClickListener() {
            val intent = Intent(this, MetricsActivity::class.java)
            startActivity(intent)
            true
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
            insulinDialogs.creationEventFun { updateEventsList() }
        }

        val sugarBtt: Button = dialog.findViewById(R.id.measure_sugar)
        sugarBtt.setOnClickListener() {
            dialog.cancel()
            sugarDialogs.creationEventFun { updateEventsList() }
        }

        dialog.show()
    }
}