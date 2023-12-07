package com.example.antidiabet1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.item_classes.FoodItem
import com.example.antidiabet1.item_classes.FoodItemAdapter

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food)

        setBackToMenu()
        setFoodSelecting()
    }

    private fun setFoodSelecting() {
        val foodTextEnter: EditText = findViewById(R.id.food_enter)

        val foodList = arrayListOf<FoodItem>()

        foodList.add(FoodItem(1, "Помидор с гречкой", 10, 20, 30, 112))
        foodList.add(FoodItem(2, "Арбуз жаренный", 69, 70, 45, 79))
        foodList.add(FoodItem(3, "Огурец с тыквой", 54, 200, 3, 1200))
        foodList.add(FoodItem(2, "Говно с морковкой", 228, 337, 45, 777))


        val adapter = FoodItemAdapter(foodList, this)

        setFoodScrollList(adapter)
      //  setAddFoodButton(foodTextEnter, adapter)
    }

    private fun setFoodScrollList(adapter: FoodItemAdapter) {
        val listView: RecyclerView = findViewById(R.id.foodList)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter

       // listView.setOnClickListener
/*
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val text = listView.getItemAtPosition(i).toString()
            adapter.remove(text)
        }*/
    }

    private fun setAddFoodButton(foodTextEnter: EditText, adapter: ArrayAdapter<String>) {
        val addFoodButton: Button = findViewById(R.id.add_food_button)

        addFoodButton.setOnClickListener() {
            val text = foodTextEnter.text.toString().trim()
            if (text != "")
                adapter.insert(text, 0)
            else {
                Toast.makeText(this, "Введите название еды", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_foodList)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}