package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.antidiabet1.item_classes.FoodItem

class DishDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "dishs", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE dishs (id INT PRIMARY KEY, name STRING NOT NULL, ingridients STRING," +
                "carbons FLOAT, proteins FLOAT, fats FLOAT, calories FLOAT, weight FLOAT)"
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
        values.put("weight", 100)
        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()
    }

}