package com.example.antidiabet1.data_base_classes


import com.example.antidiabet1.item_classes.Ingredient



object IngredientsSaver {
    var IngredientsArray: ArrayList<Ingredient>? = null

    fun getIngredients() : ArrayList<Ingredient> {
        val res = ArrayList<Ingredient>()

        if (IngredientsArray != null)
            for (ing in IngredientsArray!!) {
                res.add(ing)
            }

        return res
    }
}