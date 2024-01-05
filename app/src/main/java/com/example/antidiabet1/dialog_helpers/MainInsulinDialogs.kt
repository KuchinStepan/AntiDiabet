package com.example.antidiabet1.dialog_helpers

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.antidiabet1.R
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.example.antidiabet1.item_classes.EventAdapter
import java.util.Date

class MainInsulinDialogs (val context: Context, val eventDB: EventHistoryDatabaseHelper) {

    fun creationEventFun(updateFun: () -> Unit ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choice_insulin)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()

                val event = Event(Date(), EventType.InsulinInjection, null, grams, 0.0)
                eventDB.addEvent(event)
                updateFun()
                dialog.cancel()
            } else {
                Toast.makeText(context, "Введите количество ЕД", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun changingEventFun(event: Event, adapter: EventAdapter) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choice_insulin)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()

                event.insulin = grams
                val evs = eventDB.updateEvent(event)

                adapter.changeList(evs)

                dialog.cancel()
            } else {
                Toast.makeText(context, "Введите количество ЕД", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}