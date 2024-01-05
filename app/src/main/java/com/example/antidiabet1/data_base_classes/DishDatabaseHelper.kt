package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.antidiabet1.item_classes.ChosenIngredient
import com.example.antidiabet1.item_classes.DishItem
import com.example.antidiabet1.item_classes.Ingredient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.max


class DishDatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, "dishs", factory, 7) {
    public val table_name = "dishs"

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
                    "carbons FLOAT, proteins FLOAT, fats FLOAT, calories REAL)"
        db!!.execSQL(query)
    }

    fun deleteDish(dish: DishItem) {
        val id = dish.id

        val db = this.writableDatabase
        val args = ArrayList<String>()
        args.add(id.toString())

        db.delete(table_name, "id=?", args.toTypedArray())
        db.close()

        static_dick.remove(dish)
    }

    fun updateDish(dish: DishItem): ArrayList<DishItem> {
        val id = dish.id
        val values = ContentValues()
        val gson = Gson()
        val strIngredients = gson.toJson(dish.ingredients,
            object : TypeToken<ArrayList<ChosenIngredient>>() {}.type)
        values.put("name", dish.name)
        values.put("calories", dish.calories)
        values.put("fats", dish.fats)
        values.put("carbons", dish.calories)
        values.put("ingredients", strIngredients)
        values.put("proteins", dish.name)

        val db = this.writableDatabase
        val args = ArrayList<String>()
        args.add(id.toString())

        db.update(table_name, values, "id=?", args.toTypedArray())
        db.close()

        for (item in static_dick) {
            if (item.id == id) {
                item.name = dish.name
                item.calories = dish.calories
                item.fats = dish.fats
                item.carbons = dish.carbons
                item.ingredients = dish.ingredients
                item.proteins = dish.proteins
            }
        }

        return static_dick
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE dishs")
        onCreate(db)
    }

    fun addDish(dish: DishItem): Int{
        static_id += 1
        dish.id = EventHistoryDatabaseHelper.static_id

        val values = ContentValues()
        values.put("id", static_id)
        values.put("name", dish.name)
        values.put("fats", dish.fats)
        values.put("proteins", dish.proteins)
        values.put("calories", dish.calories)
        values.put("carbons", dish.carbons)

        val gson = Gson()
        val type = object : TypeToken<ArrayList<ChosenIngredient>>() {}.type
        val str_ingredients = gson.toJson(dish.ingredients, type)
        Log.d("--> json", str_ingredients)
        values.put("ingridients", str_ingredients)
        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()

        static_dick.add(dish)
        return static_id
    }

    private fun _getAllDishes(): ArrayList<DishItem> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM dishs", null)
        val dishList = ArrayList<DishItem>()

        val gson = Gson()
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                val id = cursor.getInt(0)
                static_id = max(static_id, id)
                val name = cursor.getString(1)
                val str_ingridients = cursor.getString(2)


                val carbons = cursor.getDouble(3)
                val proteins = cursor.getDouble(4)

                val fats = cursor.getDouble(5)
                val calories = cursor.getDouble(6)

                Log.d("<-- json", str_ingridients)

                val type = object : TypeToken<ArrayList<ChosenIngredient>>() {}.type
                var ingredients = gson.fromJson<ArrayList<ChosenIngredient>>(str_ingridients, type)
                dishList.add(DishItem(id, name, carbons, proteins, fats, calories, ingredients))
                //cursor.getColumnIndex("name")
                cursor.moveToNext()
            }
        }
        static_dick = dishList
        return dishList
    }

    private fun setDefaultDishes() : ArrayList<DishItem> {
        val foodList = ArrayList<DishItem>()
        val ingrs = ArrayList<ChosenIngredient>()
        val ingr = Ingredient("Авто-сгенерированное блюдо", 1.1, 2.3, 4.5, 6.7)
        ingrs.add(ChosenIngredient(ingr, 1000.0))

        foodList.add(DishItem(10, "Каша овсянная", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(15, "Суп борщ", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(20, "Суп овощной", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(21, "Суп грибной", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(25, "Котлета куриная", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(36, "Фрикадельки", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(37, "Сосиски молочные", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(38, "Сардельки", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(41, "Паста Карбонара", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(42, "Салат Здоровье", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(43, "Салат Цезарь", 10.0, 20.0, 30.0, 112.0, ingrs))
        foodList.add(DishItem(44, "Салат крабовый", 10.0, 20.0, 30.0, 112.0, ingrs))

        return foodList
    }

    fun getAllDishes():ArrayList<DishItem>{
        var dishes = _getAllDishes()

        if (dishes.size == 0){
            dishes = setDefaultDishes()
            for (food in dishes){
                addDish(food)
            }
        }
        dishes.reverse()
        return dishes
    }
}