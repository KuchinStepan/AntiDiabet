package com.example.antidiabet1.item_classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.R
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventType
import java.text.SimpleDateFormat
import java.util.Locale


class EventAdapter(var items: List<Event>, var context: Context)
    : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {

    var onClick : ((Event, View) -> Unit)? = null
    var onLongClick: ((Event, View) -> Unit)? = null

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.event_name)
        val date: TextView = view.findViewById(R.id.date)
        val data: TextView = view.findViewById(R.id.data)
    }

    public fun changeList(newList: List<Event>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item_for_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.date.text = SimpleDateFormat("HH:mm EEEE, dd MMM yyyy", Locale("ru")).format(item.date)

        if (item.type == EventType.Eating)
        {
            holder.name.text = "\uD83C\uDF7D Приём пищи (${item.dishItem?.name})"
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background)
            holder.data.text = "Углеводов: " + item.dishItem!!.carbons.toString()
        }
        else if (item.type == EventType.InsulinInjection)
        {
            holder.name.text = "\uD83D\uDC89 Инъекция инсулина"
            holder.itemView.setBackgroundResource(R.drawable.insulin_background)
            holder.data.text = "ЕД: " + item.insulin.toString()
        }
        else {
            holder.name.text = "\uD83E\uDE78 Измерение глюкозы"
            holder.itemView.setBackgroundResource(R.drawable.sugarmeasure_background)
            holder.data.text = "Ммоль/л: " + item.sugar.toString()
        }

        holder.itemView.setOnClickListener {
            onClick?.invoke(item, holder.itemView)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(item, holder.itemView)
            true
        }
    }
}