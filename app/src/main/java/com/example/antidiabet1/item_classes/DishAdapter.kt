package com.example.antidiabet1.item_classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.R

class DishAdapter(var items: List<newDishItem>, var context: Context)
    : RecyclerView.Adapter<DishAdapter.MyViewHolder>() {

    var onClick : ((newDishItem, View) -> Unit)? = null
    var onLongClick: ((newDishItem, View) -> Unit)? = null

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.food_name)
        val detailedData: TextView = view.findViewById(R.id.food_carbons)
        val calories: TextView = view.findViewById(R.id.food_calories)
    }

    public fun changeList(newList: List<newDishItem>) {
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
        holder.calories.text = "${calories} ккал / ${kiloDz}"

        holder.itemView.setBackgroundResource(R.drawable.unselected_item_background)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item, holder.itemView)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(item, holder.itemView)
            true
        }
    }
}