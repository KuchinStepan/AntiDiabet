package com.fiit.antidiabet.data_base_classes

import com.fiit.antidiabet.item_classes.DishItem
import java.util.Date

enum class EventType {
    Eating, SugarMeasure, InsulinInjection
}

class Event(
    val date: Date,
    val type: EventType,
    val dishItem: DishItem?,
    val insulin: Double,
    val sugar: Double
) {}

