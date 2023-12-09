package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.antidiabet1.item_classes.FoodItem
import java.text.SimpleDateFormat
import java.util.Date

class DHistDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "dishhistory", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE dishhistory (id INT PRIMARY KEY, name STRING NOT NULL, dishLink INT, " +
                "datetime TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE dishhistory")
        onCreate(db)
    }

    fun getAllDishes(): String {
        val db=this.readableDatabase
        val result = db.rawQuery("SELECT * FROM dishhistory", null)
        return result.toString()
    }

    fun addDish(dish: FoodItem){
        val values = ContentValues()
        values.put("name", dish.name)
        values.put("dishlink", 1)
        values.put("datetime", getStrTime())
        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()
    }

    private fun getStrTime(): String {
        val sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        //System.out.println(" C DATE is  "+currentDate)
        return currentDate
    }

}