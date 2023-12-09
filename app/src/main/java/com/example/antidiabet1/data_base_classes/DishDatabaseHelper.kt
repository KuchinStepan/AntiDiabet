package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.antidiabet1.item_classes.DishItem
import com.example.antidiabet1.item_classes.FoodItem
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.max

class DishDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "dishs", factory, 1) {
    companion object {
        var static_dick = ArrayList<DishItem>()
        var static_id = -1;
        // в этом говне нет статических полей, поэтому
        // я буду писать как хочу, и пусть с матом и в стиле пиоона,
        // хуже этих недоразвитых обезьян я уже не сделаю
    }

    fun get_free_id() :Int{
        return static_id
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE dishs (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, ingridients TEXT," +
                "carbons FLOAT, proteins FLOAT, fats FLOAT, calories REAL, weight FLOAT, pub_time TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE dishs")
        onCreate(db)
    }

    fun addFood(food: FoodItem){
        val values = ContentValues()
        values.put("name", food.name)
        values.put("fats", food.fats)
        values.put("proteins", food.proteins)
        values.put("calories", food.calories)
        values.put("carbons", food.carbons)
        values.put("ingridients", " ")
        values.put("weight", 100.0)
        values.put("pub_time", getStrTime())
        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()
    }

    fun addDish(dish: DishItem): Int{
        val values = ContentValues()
        values.put("name", dish.name)
        values.put("fats", dish.fats)
        values.put("proteins", dish.proteins)
        values.put("calories", dish.calories)
        values.put("carbons", dish.carbons)
        values.put("ingridients", dish.ingridients)
        values.put("weight", dish.weight)
        values.put("pub_time", dish.pub_time)
        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()
        static_id += 1
        return static_id
    }

    fun getAllDishes(): ArrayList<DishItem> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM dishs", null)
        val dishList = ArrayList<DishItem>()
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                val row = ArrayList<String>()
                val id = cursor.getInt(0)
                static_id = max(static_id, id)
                val name = cursor.getString(1)
                val ingridients = cursor.getString(2)
                val carbons = cursor.getDouble(3)
                val proteins = cursor.getDouble(4)

                val fats = cursor.getDouble(5)
                val calories = cursor.getDouble(6)
                val weight = cursor.getDouble(7)
                val pub_time = cursor.getString(8)
                dishList.add(DishItem(id, name, carbons, proteins, fats, calories, weight, ingridients, pub_time))
                //cursor.getColumnIndex("name")
                cursor.moveToNext()
            }
        }
        static_dick = dishList
        return dishList
    }

    fun getAllFoods():ArrayList<FoodItem>{
        val dishs=getAllDishes()
        val foods=ArrayList<FoodItem>()
        for (dish in dishs){
            foods.add(dish.TranslateToFood())
        }
        return foods
    }


    private fun getStrTime(): String {
        val sdf = SimpleDateFormat("dd:MM:yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        //System.out.println(" C DATE is  "+currentDate)
        return currentDate
    }

}