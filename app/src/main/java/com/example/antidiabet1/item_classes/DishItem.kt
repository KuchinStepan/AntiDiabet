package com.example.antidiabet1.item_classes

class DishItem(
    var id: Int,
    var name: String,
    var carbons: Double,
    var proteins: Double,
    var fats: Double,
    var calories: Double,
    var ingredients: ArrayList<ChosenIngredient>
) {
    public fun getWeight(): Double{
        var result = 0.0;
        for (i in 0 until ingredients.size){
            result+=ingredients[i].grams
        }
        return result
    }

    public fun changeWeight(newWeight : Double): DishItem{
        var currWeight = getWeight()
        val koef = newWeight / currWeight
        var newIngredients = ArrayList<ChosenIngredient>()
        for (i in 0 until ingredients.size){
            newIngredients.add(ChosenIngredient(ingredients[i].ingredient, ingredients[i].grams * koef))
        }
        var a:DishItem = DishItem(id, name, carbons, proteins, fats, calories, newIngredients)
        return a
    }

}