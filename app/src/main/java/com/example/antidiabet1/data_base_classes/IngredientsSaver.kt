package com.example.antidiabet1.data_base_classes

import com.example.antidiabet1.item_classes.FoodItem
import org.apache.commons.csv.CSVFormat
import java.io.File
import java.io.FileReader

object IngredientsSaver {
    var IngredientsArray : ArrayList<FoodItem>

    init {
        val res = arrayListOf<FoodItem>()
        val file = File("raw/food.csv")
        val lines = CSVFormat.DEFAULT.parse(FileReader(file))
        for(record in lines)
        {
            val itemName = record.get(1)
            val itemCarbons = record.get(2).toInt()
            val itemProteins = record.get(3).toInt()
            val itemFats = record.get(4).toInt()
            val itemCalories = record.get(5).toInt()
            res.add(FoodItem(itemName, itemCarbons, itemProteins, itemFats, itemCalories))
        }
        IngredientsArray = res
    }
}