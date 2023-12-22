package com.example.antidiabet1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.DishDatabaseHelper
import com.example.antidiabet1.item_classes.DishItem
import com.example.antidiabet1.item_classes.Ingredient
import com.example.antidiabet1.item_classes.FoodItemAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

var Ingredients = ArrayList<Ingredient>()
private var dishName = ""
class CreationFoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_food)
        val foodNameEnter = setNameEnterLine()
        foodNameEnter.setText(dishName)
        setBackToMenu()
        setCreateButton(foodNameEnter)
        setAddIngredientButton(foodNameEnter)
        setIngredientsScrollList()
    }

    private fun setAddIngredientButton(foodNameEnter: EditText) {
        val addButton: Button = findViewById(R.id.add_ingredient_button_from_creation)

        addButton.setOnClickListener() {
            val intent = Intent(this, AddIngridientActivity::class.java)
            dishName = foodNameEnter.text.toString()
            startActivity(intent)
        }
    }

    private fun setCreateButton(foodNameEnter: EditText) {
        val createButton: Button = findViewById(R.id.create_food_button)

        createButton.setOnClickListener() {
            if(dishName == "")
                dishName = foodNameEnter.text.toString()
            if (dishName == "")
                Toast.makeText(this, "Введите свое название еды", Toast.LENGTH_SHORT).show()
            else {
                val db = DishDatabaseHelper(this, null)
                val dish = CreateDishFromIngredients(db.get_free_id(), dishName, Ingredients)
                db.addDish(dish)
                dishName = ""
                Ingredients.clear()
                val intent = Intent(this, SelectionFoodActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setNameEnterLine(): EditText {
        val foodNameEnter: EditText = findViewById(R.id.food_enter)


        foodNameEnter.setOnFocusChangeListener() { view, b ->
            foodNameEnter.isCursorVisible = b
        }

        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)
        constraintLayout.setOnClickListener {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(foodNameEnter.getWindowToken(), 0)

            foodNameEnter.clearFocus()
        }

        return foodNameEnter
    }


    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_foodList)

        exitButton.setOnClickListener() {
            val intent = Intent(this, SelectionFoodActivity::class.java)

            startActivity(intent)
        }
    }

    private fun setIngredientsScrollList()
    {
        val listView: RecyclerView = findViewById(R.id.InredientList)
        listView.layoutManager = LinearLayoutManager(this)
        val adapter = FoodItemAdapter(Ingredients, this)
        listView.adapter = adapter

        adapter.onLongClick = { food, view ->
            Ingredients.remove(food)
            adapter.changeList(Ingredients)
        }
    }
}

@SuppressLint("SimpleDateFormat")
private fun CreateDishFromIngredients(id:Int, name: String, ings : ArrayList<Ingredient>): DishItem {
    var totalCarbons = 0.0
    var totalProteins = 0.0
    var totalFats = 0.0
    var totalCalories = 0.0
    val ingsNames = ArrayList<String>()
    for(ing in ings)
    {
        totalCarbons += ing.carbons
        totalProteins += ing.proteins
        totalFats += ing.fats
        totalCalories += ing.calories
        ingsNames.add(ing.name)
    }
    totalCarbons = (totalCarbons * 100).roundToInt() / 100.0
    totalProteins = (totalProteins * 100).roundToInt() / 100.0
    totalFats = (totalFats * 100).roundToInt() / 100.0
    totalCalories = (totalCalories * 100).roundToInt() / 100.0
    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val current = formatter.format(time)
    return DishItem(id, name, totalCarbons,
        totalProteins, totalFats, totalCalories, 0.0,
        ingsNames.joinToString(", "), current)
}