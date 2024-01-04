package com.fiit.antidiabet.item_classes

class _uselessDishItem(
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