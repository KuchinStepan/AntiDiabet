package com.example.antidiabet1.item_classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.R


class FoodItemAdapter(var items: List<FoodItem>, var context: Context)
    : RecyclerView.Adapter<FoodItemAdapter.MyViewHolder>() {

    var onClick : ((FoodItem, View) -> Unit)? = null
    var onLongClick: ((FoodItem, View) -> Unit)? = null

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.food_name)
        val detailedData: TextView = view.findViewById(R.id.food_carbons)
        val calories: TextView = view.findViewById(R.id.food_calories)
    }

    public fun changeList(newList: List<FoodItem>) {
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
        val detailedData = "Углеводы ${item.carbons}г / белки ${item.proteins}г / жиры ${item.fats}г"
        holder.detailedData.text = detailedData
        val kiloDz = String.format("%.2f", item.calories * 4.1868)
        holder.calories.text = "${item.calories} ккал / ${kiloDz} кДж (на 100 грамм)"

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