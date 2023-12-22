package com.example.antidiabet1.item_classes

class newDishItem(
    val id: Int,
    val name: String,
    val carbons: Double,
    val proteins: Double,
    val fats: Double,
    val calories: Double,
    val ingredients: ArrayList<ChosenIngredient>
) {}