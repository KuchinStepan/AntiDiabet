package com.example.antidiabet1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.antidiabet1.data_base_classes.DishDatabaseHelper
import com.example.antidiabet1.item_classes.DishItem

class CreationFoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation_food)

        val foodNameEnter = setNameEnterLine()

        setBackToMenu()
        setCreateButton(foodNameEnter)
        setAddIngredientButton()
    }

    private fun setAddIngredientButton() {
        val addButton: Button = findViewById(R.id.add_ingredient_button_from_creation)

        addButton.setOnClickListener() {
            val intent = Intent(this, AddIngridientActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setCreateButton(foodNameEnter: EditText) {
        val createButton: Button = findViewById(R.id.create_food_button)

        createButton.setOnClickListener() {
            if (foodNameEnter.text.toString() == "")
                Toast.makeText(this, "Введите свое название еды", Toast.LENGTH_SHORT).show()
            else {
                val db = DishDatabaseHelper(this, null)
                val dish = DishItem(db.get_free_id(), foodNameEnter.text.toString(),
                    0.0,0.0,0.0, 0.0, 0.0,
                    " ", " ")

                db.addDish(dish)

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
            super.onBackPressed()
        }
    }
}