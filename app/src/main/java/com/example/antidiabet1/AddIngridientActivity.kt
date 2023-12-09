package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.CsvReader
import com.example.antidiabet1.data_base_classes.IngredientsSaver
import com.example.antidiabet1.item_classes.FoodItem
import com.example.antidiabet1.item_classes.FoodItemAdapter


class AddIngridientActivity : AppCompatActivity() {
    private var lastClickedFoodView: View ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingridient)

        setBack()
        setFoodSelecting()
    }

    private fun setFoodSelecting() {
        val foodTextEnter = setSearchLine()

        if (IngredientsSaver.IngredientsArray == null)
        {
            CsvReader(this)
        }
        val foodList = IngredientsSaver.IngredientsArray ?: arrayListOf<FoodItem>()

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

        setFoodScrollList(adapter)
        setSelectFoodButton()
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

    private fun setFoodScrollList(adapter: FoodItemAdapter) {
        val listView: RecyclerView = findViewById(R.id.ingridientList)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter


        adapter.onClick = { food, view ->
            lastClickedFoodView?.setBackgroundResource(R.drawable.unselected_item_background)
            view.setBackgroundResource(R.drawable.selected_item_background)
            lastClickedFoodView = view
        }
    }

    private fun setAddIngredientButton() {

    }

    private fun setSelectFoodButton() {
        val addFoodButton: Button = findViewById(R.id.select_ingridient_button)

        addFoodButton.setOnClickListener() {
            val intent = Intent(this, CreationFoodActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setBack() {
        val exitButton: TextView = findViewById(R.id.exit_ingridientList)

        exitButton.setOnClickListener() {
            super.onBackPressed()
        }
    }
}