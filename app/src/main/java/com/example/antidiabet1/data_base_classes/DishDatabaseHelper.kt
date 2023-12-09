package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.antidiabet1.item_classes.FoodItem
import java.text.SimpleDateFormat
import java.util.Date

class DishDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "dishs", factory, 1) {


    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE dishs (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, ingridients TEXT," +
                "carbons FLOAT, proteins FLOAT, fats FLOAT, calories REAL, weight FLOAT, pub_time TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE dishs")
        onCreate(db)
    }

    fun addDish(dish: FoodItem){
        val values = ContentValues()
        values.put("name", dish.name)
        values.put("fats", dish.fats)
        values.put("proteins", dish.proteins)
        values.put("calories", dish.calories)
        values.put("carbons", dish.carbons)
        values.put("weight", 100.0)
        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()
    }

    fun getAllDishes(): ArrayList<ArrayList<String>> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM dishs", null)
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


    private fun getStrTime(): String {
        val sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        //System.out.println(" C DATE is  "+currentDate)
        return currentDate
    }

}