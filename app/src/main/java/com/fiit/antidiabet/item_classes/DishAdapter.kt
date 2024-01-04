package com.fiit.antidiabet.item_classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fiit.antidiabet.R

class DishAdapter(var items: List<DishItem>, var context: Context)
    : RecyclerView.Adapter<DishAdapter.MyViewHolder>() {

    var onClick : ((DishItem, View) -> Unit)? = null
    var onLongClick: ((DishItem, View) -> Unit)? = null
    var lastClickedName: String ?= null

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.event_name)
        val detailedData: TextView = view.findViewById(R.id.date)
        val calories: TextView = view.findViewById(R.id.food_calories)
    }

    public fun changeList(newList: List<DishItem>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_item_for_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]

        holder.name.text = item.name
        val detailedData = "Углеводы ${item.carbons}г " +
                "/ белки ${item.proteins}г " +
                "/ жиры ${item.fats}г"
        holder.detailedData.text = detailedData
        val calories = item.calories
        val kiloDz = String.format("%.2f", calories * 4.1868)
        holder.calories.text = "${calories} ккал / ${kiloDz} кДж"

        if (lastClickedName != null && lastClickedName == item.name) {
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background)
        }
        else {
            holder.itemView.setBackgroundResource(R.drawable.unselected_item_background)
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