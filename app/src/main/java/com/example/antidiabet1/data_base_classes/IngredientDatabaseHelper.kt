package com.example.antidiabet1.data_base_classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.antidiabet1.item_classes.Ingredient
import kotlin.math.max

class IngredientDatabaseHelper (val context: Context, val factory: SQLiteDatabase.CursorFactory?):
        SQLiteOpenHelper(context, "ingredients", factory, 1) {
    val table_name = "ingredients"

    companion object {
        var static_dick = ArrayList<Ingredient>()
        var static_id = -1;
        // в этом говне нет статических полей, поэтому
        // я буду писать как хочу, и пусть с матом и в стиле пиоона,
        // хуже этих недоразвитых обезьян я уже не сделаю
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE ingredients (id INTEGER PRIMARY KEY autoincrement, name TEXT NOT NULL, " +
                "carbons FLOAT, proteins FLOAT, fats FLOAT, calories FLOAT)"
        db!!.execSQL(query)
    }

    fun deleteIngredient(ingredient: Ingredient) {
        val id = ingredient.id

        val db = this.writableDatabase
        val args = ArrayList<String>()
        args.add(id.toString())

        db.delete(table_name, "id=?", args.toTypedArray())
        db.close()

        static_dick.remove(ingredient)
    }

    fun getIngredientById(id: Int): Ingredient? {
        for (dish in static_dick){
            if (dish.id == id)
                return dish
        }
        return null
    }

   /* fun updateDish(ingredient: Ingredient): ArrayList<Ingredient> {
        val id = ingredient.id
        val values = ContentValues()
        values.put("name", ingredient.name)
        values.put("calories", ingredient.calories)
        values.put("fats", ingredient.fats)
        values.put("carbons", ingredient.calories)
        values.put("proteins", ingredient.name)

        val db = this.writableDatabase
        val args = ArrayList<String>()
        args.add(id.toString())

        db.update(table_name, values, "id=?", args.toTypedArray())
        db.close()

        for (item in static_dick) {
            if (item.id == id) {
                item.name = ingredient.name
                item.calories = ingredient.calories
                item.fats = ingredient.fats
                item.carbons = ingredient.carbons
                item.ingredients = ingredient.ingredients
                item.proteins = ingredient.proteins
            }
        }

        return static_dick
    }*/

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE ingredients")
        onCreate(db)
    }

    fun addIngredient(ingredient: Ingredient) {
        /*if (ingredient.id != -1)
        {
            updateDish(ingredient)
            return
        }*/
        static_id += 1
        ingredient.id = static_id

        val values = ContentValues()
        values.put("id", static_id)
        values.put("name", ingredient.name)
        values.put("fats", ingredient.fats)
        values.put("proteins", ingredient.proteins)
        values.put("calories", ingredient.calories)
        values.put("carbons", ingredient.carbons)

        val db = this.writableDatabase
        db.insert("dishs", null, values)
        db.close()

        static_dick.add(ingredient)
        return
    }

    private fun _getAllIngredients(): ArrayList<Ingredient> {
        val db=this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ingredients", null)
        val ingredientList = ArrayList<Ingredient>()


        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                val id = cursor.getInt(0)
                static_id = max(static_id, id)
                val name = cursor.getString(1)

                val carbons = cursor.getDouble(2)
                val proteins = cursor.getDouble(3)

                val fats = cursor.getDouble(4)
                val calories = cursor.getDouble(5)

                ingredientList.add(Ingredient(name, carbons, proteins, fats, calories, id))

                cursor.moveToNext()
            }
        }
        static_dick = ingredientList
        return ingredientList
    }

    fun getAllCachedIngredients() : ArrayList<Ingredient> {
        return static_dick
    }

    fun getAllIngredients():ArrayList<Ingredient>{
        var dishes = _getAllIngredients()
        dishes.reverse()

        return dishes
    }
}