package com.example.antidiabet1

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.CsvReader
import com.example.antidiabet1.data_base_classes.IngredientsSaver
import com.example.antidiabet1.item_classes.ChosenIngredient
import com.example.antidiabet1.item_classes.FoodItemAdapter
import com.example.antidiabet1.item_classes.Ingredient


class AddIngridientActivity : AppCompatActivity() {
    private var lastClickedFoodView: View ?= null
    private var lastClickedIngredient: Ingredient ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient)

        setBack()
        setFoodSelecting()
    }

    private fun setFoodSelecting() {
        val foodTextEnter = setSearchLine()

        if (IngredientsSaver.IngredientsArray == null)
        {
            CsvReader(this)
        }
        val foodList = IngredientsSaver.IngredientsArray ?: arrayListOf<Ingredient>()

        var showingList = foodList.toList()
        val adapter = FoodItemAdapter(showingList, this)

        // Обновление по поиску
        foodTextEnter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = foodTextEnter.text.toString()
                showingList = foodList
                    .filter { foodItem ->  text.lowercase() in foodItem.name.lowercase() }
                    .sortedBy { foodItem -> foodItem.name.length }
                adapter.changeList(showingList)
            }
        })

        setIngredientScrollList(adapter)
        setSelectIngredientButton()
    }

    private fun setSearchLine(): EditText {
        val foodNameEnter: EditText = findViewById(R.id.ingridient_enter)


        foodNameEnter.setOnFocusChangeListener() { view, b ->
            foodNameEnter.isCursorVisible = b
        }

        val constraintLayout: ConstraintLayout = findViewById(R.id.ingridient_constraintLayout)
        constraintLayout.setOnClickListener {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(foodNameEnter.getWindowToken(), 0)

            foodNameEnter.clearFocus()
        }

        return foodNameEnter
    }

    private fun setIngredientScrollList(adapter: FoodItemAdapter) {
        val listView: RecyclerView = findViewById(R.id.ingridientList)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter


        adapter.onClick = { food, view ->
            lastClickedFoodView?.setBackgroundResource(R.drawable.unselected_item_background)
            view.setBackgroundResource(R.drawable.selected_item_background)
            lastClickedFoodView = view
            lastClickedIngredient = food
            adapter.lastClickedName = food.name
        }
    }


    private fun setSelectIngredientButton() {
        val addFoodButton: Button = findViewById(R.id.select_ingredient_button)

        val dialog = Dialog(this)
        addFoodButton.setOnClickListener() {
            // && !Ingredients.contains(lastClickedIngredient) убрано из за несоответствия
            if(lastClickedIngredient != null ) {
                showCustomDialog(dialog)
            }
        }
    }

    private fun showCustomDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_gramm_select)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val intent = Intent(this, CreationFoodActivity::class.java)
            intent.putExtra("dish_id", "fromAddIngredient")

            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()
                val chIngredient = ChosenIngredient(lastClickedIngredient!!, grams)
                Ingredients.add(chIngredient)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Введите вес", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun setBack() {
        val exitButton: TextView = findViewById(R.id.exit_ingridientList)

        exitButton.setOnClickListener() {
            super.onBackPressed()
        }
    }
}