package com.example.antidiabet1.data_base_classes

import com.example.antidiabet1.item_classes.HistoryDishItem
import com.example.antidiabet1.item_classes.Ingredient
import org.jetbrains.annotations.Nullable
import java.util.Date

enum class EventType {
    Eating, SugarMeasure, InsulinInjection
}

class Event(
    val date: Date,
    val type: EventType,
    val historyDishItem: HistoryDishItem,
    val insulin: Double,
    val sugar: Double
) {}

