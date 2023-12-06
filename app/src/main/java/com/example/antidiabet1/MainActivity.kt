package com.example.antidiabet1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addingFoodScreen()
    }

    private fun addingFoodScreen() {
        val button: Button = findViewById(R.id.food_button)

        button.setOnClickListener() {
            val intent = Intent(this, FoodActivity::class.java)
            startActivity(intent)
        }
    }
}