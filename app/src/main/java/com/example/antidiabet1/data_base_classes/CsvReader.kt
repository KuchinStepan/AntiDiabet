package com.example.antidiabet1.data_base_classes

import android.content.Context
import com.example.antidiabet1.R
import com.example.antidiabet1.item_classes.FoodItem
import org.apache.commons.csv.CSVFormat

class CsvReader(context: Context) {

    init {
        val temp = context.resources.openRawResource(R.raw.food).bufferedReader()
        val res = arrayListOf<FoodItem>()
        val lines = CSVFormat.DEFAULT.parse(temp)
        var notHeaderFlag = false
        for(record in lines)
        {
            if (notHeaderFlag)
            {
                val itemName = record.get(1)
                val itemCarbons = record.get(2).toDouble()
                val itemProteins = record.get(3).toDouble()
                val itemFats = record.get(4).toDouble()
                val itemCalories = record.get(5).toDouble()
                res.add(FoodItem(itemName, itemCarbons, itemProteins, itemFats, itemCalories))
            }
            else
            {
                notHeaderFlag = true
            }
        }
        IngredientsSaver.IngredientsArray = res
    }
}