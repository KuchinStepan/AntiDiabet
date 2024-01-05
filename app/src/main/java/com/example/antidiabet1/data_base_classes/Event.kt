package com.example.antidiabet1.data_base_classes

import com.example.antidiabet1.item_classes.DishItem
import java.util.Date

enum class EventType {
    Eating, SugarMeasure, InsulinInjection
}

class Event(
    val date: Date,
    val type: EventType,
    var dishItem: DishItem?,
    var insulin: Double,
    var sugar: Double,
    var id: Int = -1
) {}

