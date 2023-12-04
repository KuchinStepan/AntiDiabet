package com.example.antidiabet1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class FoodActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food)

        val exitButton: TextView = findViewById(R.id.exit_foodList)
        val foodEnter: EditText = findViewById(R.id.food_enter)
        val listView: ListView = findViewById(R.id.foodList)
        val addFoodButton: Button = findViewById(R.id.add_food_button)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val foodList: MutableList<String> = mutableListOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, foodList)

        listView.adapter = adapter

        addFoodButton.setOnClickListener() {
            val text = foodEnter.text.toString().trim()
            if (text != "")
                adapter.insert(text, 0)
            else {
                Toast.makeText(this, "Введите название еды", Toast.LENGTH_SHORT).show()
            }
        }

        listView.setOnItemClickListener { adapterView, view, i, l ->
            val text = listView.getItemAtPosition(i).toString()
            adapter.remove(text)
        }
    }
}