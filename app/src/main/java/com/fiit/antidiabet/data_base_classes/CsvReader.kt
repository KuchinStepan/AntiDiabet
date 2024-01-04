package com.fiit.antidiabet.data_base_classes

import android.content.Context
import com.fiit.antidiabet.R
import com.fiit.antidiabet.item_classes.Ingredient
import org.apache.commons.csv.CSVFormat

class CsvReader(context: Context) {

    init {
        val temp = context.resources.openRawResource(R.raw.ru).bufferedReader()
        val res = arrayListOf<Ingredient>()
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
                res.add(Ingredient(itemName, itemCarbons, itemProteins, itemFats, itemCalories))
            }
            else
            {
                notHeaderFlag = true
            }
        }
        IngredientsSaver.IngredientsArray = res
    }
}