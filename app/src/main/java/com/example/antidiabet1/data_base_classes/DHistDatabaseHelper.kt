package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import com.example.antidiabet1.item_classes.FoodItem
import java.text.SimpleDateFormat
import java.util.Date

class DHistDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "dishhistory", factory, 2) {
    public val table_name = "dishhistory"

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE dishhistory (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, dishLink INT, " +
                "datetime TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE dishhistory")
        onCreate(db)
    }

    fun getAllDishes(): ArrayList<ArrayList<String>> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM dishhistory", null)
        val result = ArrayList<ArrayList<String>>()
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                val row = ArrayList<String>()
                row.add(cursor.getString(0))
                row.add(cursor.getString(1))
                row.add(cursor.getString(2))
                row.add(cursor.getString(3))
                result.add(row)
                //cursor.getColumnIndex("name")
                cursor.moveToNext()
            }
        }
        return result
    }

    fun addDish(dish: FoodItem, foodId: Int){
        val values = ContentValues()
        values.put("name", dish.name)
        values.put("dishLink", foodId)
        values.put("datetime", getStrTime())
        val db = this.writableDatabase
        db.insert("dishhistory", null, values)
        db.close()
    }

    private fun getStrTime(): String {
        val sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        //System.out.println(" C DATE is  "+currentDate)
        return currentDate
    }

}