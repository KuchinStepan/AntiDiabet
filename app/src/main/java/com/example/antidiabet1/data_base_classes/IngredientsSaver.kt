package com.example.antidiabet1.data_base_classes

import android.R
import android.app.usage.ExternalStorageStats
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.antidiabet1.item_classes.FoodItem
import org.apache.commons.csv.CSVFormat
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths



object IngredientsSaver {
    var IngredientsArray: ArrayList<FoodItem>? = null
    init {

    }
}