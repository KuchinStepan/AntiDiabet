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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.DHistDatabaseHelper
import com.example.antidiabet1.item_classes.FoodItem
import com.example.antidiabet1.item_classes.FoodItemAdapter

import java.text.SimpleDateFormat
import java.util.Date


class VizualizerDairyActivity : AppCompatActivity() {
    private var lastClickedFoodView: View ?= null
    private var lastClckedFoodItem: FoodItem ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vizyalize_dairy)

        setBackToMenu()
        setFoodSelecting()
    }

    private fun setFoodSelecting() {
        val foodTextEnter = setSearchLine()

        val foodList =  arrayListOf<FoodItem>()
        val db = DHistDatabaseHelper(this, null)

        foodList.add(FoodItem("Помидор с гречкой", 10.0, 20.0, 30.0, 112.0))
        foodList.add(FoodItem("Арбуз жаренный", 69.0, 70.0, 45.0, 79.0))
        foodList.add(FoodItem("Огурец с тыквой", 54.0, 200.0, 3.0, 1200.0))
        foodList.add(FoodItem("Курица с пюрешкой", 17.0, 94.0, 3.0, 345.0))
        foodList.add(FoodItem("Камень граненый", 17.0, 94.0, 3.0, 345.0))
        foodList.add(FoodItem(getStrTime(), 228.0, 337.0, 45.0, 777.0))
        //for (food in foodList){
        //    db.addDish(food, foodList.indexOf(food))
        //}
        foodList.add(FoodItem(db.getAllDishes().toString() , 10.0, 20.0, 30.0, 112.0))

        for (food in db.getAllDishes()){
            foodList.add(FoodItem(food.toString(),228.0, 337.0, 45.0, 777.0))
        }
        var showingList = foodList.toList()
        val adapter = FoodItemAdapter(showingList, this)

        // Обновление по поиску
        foodTextEnter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = foodTextEnter.text.toString()
                showingList = foodList.filter { foodItem ->  text.lowercase() in foodItem.name.lowercase() }
                adapter.changeList(showingList)
            }
        })

        setFoodScrollList(adapter)
        setSelectFoodButton()
        setAddFoodButton()
    }
    private fun setSearchLine(): EditText {
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
    private fun setFoodScrollList(adapter: FoodItemAdapter) {
        val listView: RecyclerView = findViewById(R.id.foodList)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter


        adapter.onClick = { food, view ->
            lastClickedFoodView?.setBackgroundResource(R.drawable.unselected_item_background)
            view.setBackgroundResource(R.drawable.selected_item_background)
            lastClickedFoodView = view
            lastClckedFoodItem = food
        }
    }

    private fun setAddFoodButton() {
        val addFoodButton: Button = findViewById(R.id.add_food_button)

        addFoodButton.setOnClickListener() {
            val intent = Intent(this, CreationFoodActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelectFoodButton() {
        val addFoodButton: Button = findViewById(R.id.select_food_button)

        addFoodButton.setOnClickListener() {
            if (lastClckedFoodItem != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Выберите еду", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_foodList)
        //exitButton.text = getStrTime();

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getStrTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")

        val currentDate = sdf.format(Date())
        //System.out.println(" C DATE is  "+currentDate)
        return currentDate
    }
}