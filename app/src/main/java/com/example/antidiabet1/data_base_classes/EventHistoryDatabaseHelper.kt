package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.antidiabet1.item_classes.DishItem
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.max
//цоашцоащшуазозцащоцщуазщцшоуащцушоацщшоащшыыдвлфываолдфжыоалдфлыалфывджлаофыдважофдываолдфо
class EventHistoryDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "eventhistory", factory, 8) {
    public val table_name = "eventhistory"
    companion object {
        var static_dick = ArrayList<Event>()
        var static_id = -1;
    }

    fun get_free_id() :Int{
        return DishDatabaseHelper.static_id
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $table_name (id INTEGER PRIMARY KEY autoincrement, date TEXT NOT NULL, eventType INT, " +
                "dishItem TEXT, insulin DOUBLE, sugar DOUBLE)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE $table_name")
        onCreate(db)
    }

    fun getAllEvents(): ArrayList<Event> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $table_name", null)
        val events = ArrayList<Event>()
        val gson = Gson()
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                val id = cursor.getInt(0)
                static_id = max(static_id, id)
                val date = Date(cursor.getString(1))
                val eventType = EventType.valueOf(cursor.getString(2))
                val dishItem = gson.fromJson(cursor.getString(3), DishItem::class.java)
                val insulin = cursor.getDouble(4)
                val sugar = cursor.getDouble(5)
                events.add(Event(date, eventType, dishItem, insulin, sugar))
                //cursor.getColumnIndex("name")
                cursor.moveToNext()
            }
        }
        static_dick = events
        return events
    }

    fun addEvent(event: Event): Int {
        val values = ContentValues()
        values.put("date", event.date.toString())
        values.put("eventType", event.type.toString())
        val gson = Gson()
        val strDishItem = gson.toJson(event.dishItem)
        Log.d("--> json", strDishItem)
        values.put("dishItem", strDishItem)
        values.put("insulin", event.insulin)
        values.put("sugar", event.sugar)
        val db = this.writableDatabase
        db.insert(table_name, null, values)
        db.close()
        static_id += 1
        return static_id
    }

    private fun getStrTime(date: Date): String {
        val sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss")
        val currentDate = sdf.format(date)
        return currentDate
    }

    fun isMoreThanNDaysPassed(date: Date, n: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, n) // Adds 3 days to the original date

        val currentDate = Calendar.getInstance()

        return currentDate.after(calendar)
    }


    fun getEventsByLastThreeDays(): ArrayList<Event> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $table_name", null)
        val events = ArrayList<Event>()
        val gson = Gson()
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(0)
                static_id = max(static_id, id)
                val date = Date(cursor.getString(1))
                if (isMoreThanNDaysPassed(date, 3))
                    break
                val eventType = EventType.valueOf(cursor.getString(2))
                val dishItem = gson.fromJson(cursor.getString(3), DishItem::class.java)
                val insulin = cursor.getDouble(4)
                val sugar = cursor.getDouble(5)
                events.add(Event(date, eventType, dishItem, insulin, sugar))
                cursor.moveToNext()
            }
        }
        static_dick = events
        return events
    }
}