package com.example.antidiabet1.item_classes

class DishItem(
    val id: Int,
    val name: String,
    val carbons: Double,
    val proteins: Double,
    val fats: Double,
    val calories: Double,
    val weight: Double,
    val ingridients: String,
    val pub_time: String
) {
    public fun TranslateToFood(): Ingredient{
        return Ingredient(name, carbons, proteins, fats, calories)
    }

}