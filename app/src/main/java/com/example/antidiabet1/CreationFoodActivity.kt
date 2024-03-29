package com.example.antidiabet1

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.DishDatabaseHelper
import com.example.antidiabet1.item_classes.ChosenIngredient
import com.example.antidiabet1.item_classes.ChosenIngredientAdapter
import com.example.antidiabet1.item_classes.DishItem
import kotlin.math.roundToInt

var Ingredients = ArrayList<ChosenIngredient>()
var dishName = ""
var dishId = -1
class CreationFoodActivity : AppCompatActivity() {

    var toast: Toast ?= null
    lateinit var adapter: ChosenIngredientAdapter
    lateinit var foodNameEnter: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_food)
        val foodNameEnter = setNameEnterLine()
        foodNameEnter.setText(dishName)
        setBackToMenu()
        setCreateButton(foodNameEnter)
        setAddIngredientButton(foodNameEnter)
        setIngredientsScrollList()

        val _id = intent.getStringExtra("dish_id").toString()
        Log.d("--> meow?", _id)
        if (_id == "fromAddIngredient")
            Log.d("--> muhahaha", "dont delete this or it wouldnt work")
        else if (_id != "null")
            setAlreadyCreatedDish(DishDatabaseHelper(this, null)
                .getDishById(_id.toInt())!!)
        else {
            val dish = CreateDishFromIngredients(-1, "", ArrayList())
            setAlreadyCreatedDish(dish)
        }

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT)
    }

    fun setAlreadyCreatedDish(dish: DishItem){
        Ingredients = dish.ingredients
        dishId = dish.id
        dishName = dish.name
        adapter.changeList(Ingredients)
        foodNameEnter.setText(dishName)
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
            dishName = foodNameEnter.text.toString()
            if (dishName == ""){
                toast?.setText("Введите свое название еды")
                toast?.show()
            }
            else if (Ingredients.count() == 0){
                toast?.setText("Добавьте ингредиенты")
                toast?.show()
            }
            else {
                val db = DishDatabaseHelper(this, null)
                val dish = CreateDishFromIngredients(dishId, dishName, Ingredients)
                db.addDish(dish)
                dishName = ""
                Ingredients.clear()
                val intent = Intent(this, SelectionFoodActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setNameEnterLine(): EditText {
        foodNameEnter = findViewById(R.id.food_enter)


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
        val exitButton: TextView = findViewById(R.id.exit_dateHistory)

        exitButton.setOnClickListener() {
            onBackPressed()
        }
    }

    override fun onBackPressed()
    {
        if (foodNameEnter.text.toString() != "" || Ingredients.isNotEmpty())
            showCancelConfirmationDialog(Dialog(this))
        else {
            val intent = Intent(this, SelectionFoodActivity::class.java)
            Ingredients.clear()
            startActivity(intent)
        }
    }

    private fun showCancelConfirmationDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_cancel_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val cancelBtt: Button = dialog.findViewById(R.id.cancel_cancel_button)
        cancelBtt.setOnClickListener() {
            dialog.cancel()
        }

        val cancelButton: Button = dialog.findViewById(R.id.cancel_confirm_button)
        cancelButton.setOnClickListener() {
            val intent = Intent(this, SelectionFoodActivity::class.java)
            Ingredients.clear()
            startActivity(intent)
        }

        dialog.show()
    }

    private fun setIngredientsScrollList()
    {
        val listView: RecyclerView = findViewById(R.id.InredientList)
        listView.layoutManager = LinearLayoutManager(this)
        adapter = ChosenIngredientAdapter(Ingredients, this)
        listView.adapter = adapter

        adapter.onLongClick = { food, view ->
            Ingredients.remove(food)
            adapter.changeList(Ingredients)
        }
    }
}

private fun CreateDishFromIngredients(id:Int, name: String, ings : ArrayList<ChosenIngredient>): DishItem {
    var totalCarbons = 0.0
    var totalProteins = 0.0
    var totalFats = 0.0
    var totalCalories = 0.0
    val ingsNames = ArrayList<String>()
    for(ing in ings)
    {
        totalCarbons += ing.ingredient.carbons
        totalProteins += ing.ingredient.proteins
        totalFats += ing.ingredient.fats
        totalCalories += ing.ingredient.calories
        ingsNames.add(ing.ingredient.name)
    }
    totalCarbons = (totalCarbons * 100).roundToInt() / 100.0
    totalProteins = (totalProteins * 100).roundToInt() / 100.0
    totalFats = (totalFats * 100).roundToInt() / 100.0
    totalCalories = (totalCalories * 100).roundToInt() / 100.0

    var rightName = ""
    try {
        rightName = "${name[0].uppercase()}${name.substring(1, name.length)}"
    }
    catch (e: Exception) {
        rightName = name
    }

    return DishItem(id, rightName, totalCarbons, totalProteins, totalFats, totalCalories, ings)
}