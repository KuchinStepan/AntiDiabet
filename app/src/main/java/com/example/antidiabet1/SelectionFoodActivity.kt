package com.example.antidiabet1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antidiabet1.data_base_classes.DishDatabaseHelper
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.example.antidiabet1.item_classes.ChosenIngredientAdapter
import com.example.antidiabet1.item_classes.DishAdapter
import com.example.antidiabet1.item_classes.DishItem
import java.util.Date


class SelectionFoodActivity : AppCompatActivity() {
    private var lastClickedFoodView: View? = null
    private var lastClckedDish: DishItem? = null
    lateinit var dbHelper: DishDatabaseHelper
    lateinit var adapter: DishAdapter
    private var idFromExtras: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_food)
        idFromExtras = intent.getStringExtra("event_id")

        dbHelper = DishDatabaseHelper(this, null)
        setBackToMenu()
        setFoodSelecting()
    }

    private fun setFoodSelecting() {
        val foodTextEnter = setSearchLine()

        val db = DishDatabaseHelper(this, null)

        val foodList = db.getAllDishes()
        var showingList = foodList.toList()

        adapter = DishAdapter(showingList, this)

        // Обновление по поиску
        foodTextEnter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = foodTextEnter.text.toString()
                showingList =
                    foodList.filter { foodItem -> text.lowercase() in foodItem.name.lowercase() }
                adapter.changeList(showingList)
            }
        })

        setFoodScrollList(adapter)
        setSelectFoodButton()
        setAddFoodButton()
    }

    private fun setSearchLine(): EditText {
        val foodNameEnter: EditText = findViewById(R.id.food_enter)


        foodNameEnter.setOnFocusChangeListener() { view, b ->
            foodNameEnter.isCursorVisible = b
        }

        val constraintLayout: ConstraintLayout = findViewById(R.id.constraintLayout)
        constraintLayout.setOnClickListener {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(foodNameEnter.getWindowToken(), 0)

            foodNameEnter.clearFocus()
        }

        return foodNameEnter
    }

    private fun setFoodScrollList(adapter: DishAdapter) {
        val listView: RecyclerView = findViewById(R.id.foodList)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter


        adapter.onClick = { food, view ->
            lastClickedFoodView?.setBackgroundResource(R.drawable.unselected_item_background)
            view.setBackgroundResource(R.drawable.selected_item_background)
            lastClickedFoodView = view
            lastClckedDish = food
            adapter.lastClickedName = food.name
        }

        val dialog = Dialog(this)
        adapter.onLongClick = { dish, view ->
            showCustomDialog(dialog, dish)
        }
    }

    private fun showCustomDialog(dialog: Dialog, dish: DishItem) {
        dialog.setContentView(R.layout.dialog_food_ingredients)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val view: RecyclerView = dialog.findViewById(R.id.ingredients)

        view.layoutManager = LinearLayoutManager(this)
        view.adapter = ChosenIngredientAdapter(dish.ingredients, this)

        val name: TextView = dialog.findViewById(R.id.name)
        name.text = dish.name

        val deleteButton: Button = dialog.findViewById(R.id.dialog_food_ingredients_delete)

        deleteButton.setOnClickListener() {
            dialog.cancel()
            showDeleteConfirmationDialog(dialog, dish)
        }

        val editButton: Button = dialog.findViewById(R.id.dialog_food_ingredients_edit)

        editButton.setOnClickListener() {
            val activity = CreationFoodActivity::class.java
            val intent = Intent(this, activity)
            intent.putExtra("dish_id", dish.id.toString())
            startActivity(intent)
        }

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(dialog: Dialog, dish: DishItem) {
        dialog.setContentView(R.layout.dialog_delete_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val cancelBtt: Button = dialog.findViewById(R.id.delete_cancel_button)
        cancelBtt.setOnClickListener() {
            dialog.cancel()
        }

        val deleteBtt: Button = dialog.findViewById(R.id.delete_confirm_button)
        deleteBtt.setOnClickListener() {
            dbHelper.deleteDish(dish)
            adapter.changeList(dbHelper.getAllDishes())
            dialog.cancel()
        }

        dialog.show()
    }


    private fun setAddFoodButton() {
        val addFoodButton: Button = findViewById(R.id.add_food_button)

        addFoodButton.setOnClickListener() {
            val intent = Intent(this, CreationFoodActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setSelectFoodButton() {
        val addFoodButton: Button = findViewById(R.id.select_food_button)

        addFoodButton.setOnClickListener() {
            if (lastClckedDish != null) {
                val dialog = Dialog(this)
                showGramDialog(this, dialog)
            } else {
                Toast.makeText(this, "Выберите еду", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun _createEventNoteFun(context: Context, dish: DishItem) {
        val dbHelper = EventHistoryDatabaseHelper(context, null)

        Log.d("--> yo", idFromExtras.toString())

        if (idFromExtras == null) {
            dbHelper.addEvent(
                Event(
                    Date(), EventType.Eating, dish,
                    0.0, 0.0
                )
            )
            val events = dbHelper.getAllEvents()
            Log.d("--> MEOW", events.size.toString())
            for (i in 0 until events.size) {
                val event = events[i]
                Log.d("--> MEOW", event.date.toString())
                Log.d("--> MEOW", event.dishItem.toString())
            }
        }
        else {
            val event = dbHelper.getEventById(idFromExtras!!.toInt())
            event!!.dishItem = dish
            dbHelper.updateEvent(event)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showGramDialog(context: Context, dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_gramm_select)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()

                val dish = lastClckedDish!!.changeWeight(grams)

                _createEventNoteFun(context, dish)
            } else {
                Toast.makeText(this, "Введите вес", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun setBackToMenu() {
        val exitButton: TextView = findViewById(R.id.exit_dateHistory)

        exitButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed()
    {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}