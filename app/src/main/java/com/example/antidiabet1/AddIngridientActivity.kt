package com.example.antidiabet1

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.antidiabet1.data_base_classes.CsvReader
import com.example.antidiabet1.data_base_classes.IngredientDatabaseHelper
import com.example.antidiabet1.data_base_classes.IngredientsSaver
import com.example.antidiabet1.item_classes.ChosenIngredient
import com.example.antidiabet1.item_classes.FoodItemAdapter
import com.example.antidiabet1.item_classes.Ingredient


class AddIngridientActivity : AppCompatActivity() {
    private var lastClickedFoodView: View ?= null
    private var lastClickedIngredient: Ingredient ?= null

    lateinit var dbIngredient: IngredientDatabaseHelper
    lateinit var adapter: FoodItemAdapter
    lateinit var foodList: ArrayList<Ingredient>
    lateinit var foodListAsReversed: List<Ingredient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ingredient_to_dish)

        dbIngredient = IngredientDatabaseHelper(this, null)
        setBack()
        setCreateIngredientButton()
        setFoodSelecting()
    }

    private fun setCreateIngredientButton() {
        val createIngredientButton: Button = findViewById(R.id.create_new_ingredient_button)

        val dialog = Dialog(this)
        createIngredientButton.setOnClickListener() {
            showCreateIngredientDialog(dialog)
        }
    }

    private fun showCreateIngredientDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_create_ingredient)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val okButton: Button = dialog.findViewById(R.id.ok_button)
        val carbonsEnter: EditText = dialog.findViewById(R.id.carbons_enter)
        val fatsEnter: EditText = dialog.findViewById(R.id.fats_enter)
        val proteinsEnter: EditText = dialog.findViewById(R.id.proteins_enter)
        val nameEnter: EditText = dialog.findViewById(R.id.ingredient_name)

        okButton.setOnClickListener() {
            if (carbonsEnter.text.toString() != ""
                && fatsEnter.text.toString() != ""
                && proteinsEnter.text.toString() != ""
                && nameEnter.text.toString() != "")
            {
                createIngredient(nameEnter.text.toString(),
                    carbonsEnter.text.toString().toDouble(),
                    fatsEnter.text.toString().toDouble(),
                    proteinsEnter.text.toString().toDouble())
                dialog.cancel()
            }
            else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun createIngredient(name: String, carbons: Double,
                                 fats: Double, proteins: Double){
        val cal = 4 * carbons + 4 * proteins + 9 * fats
        val ingredient = Ingredient(name, carbons, fats, proteins, cal)

        dbIngredient.addIngredient(ingredient)
        foodList.add(ingredient)

        adapter.changeList(foodListAsReversed)
    }

    private fun setFoodSelecting() {
        val foodTextEnter = setSearchLine()

        if (IngredientsSaver.IngredientsArray == null)
        {
            CsvReader(this)
        }

        foodList = IngredientsSaver.getIngredients()
        foodList.reverse()
        foodList.sortBy{-it.name.count()}

        val dbIngrs = dbIngredient.getAllIngredients()
        for (ingr in dbIngrs) {
            foodList.add(ingr)
        }

        foodListAsReversed = foodList.asReversed()

        adapter = FoodItemAdapter(foodListAsReversed, this)

        // Обновление по поиску
        foodTextEnter.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = foodTextEnter.text.toString()
                if (text != "")
                    adapter.changeList(foodListAsReversed
                        .filter { foodItem ->  text.lowercase() in foodItem.name.lowercase() })
                else adapter.changeList(foodListAsReversed)
            }
        })

        setIngredientScrollList(adapter)
        setSelectIngredientButton()
    }

    private fun setSearchLine(): EditText {
        val foodNameEnter: EditText = findViewById(R.id.ingridient_enter)


        foodNameEnter.setOnFocusChangeListener() { view, b ->
            foodNameEnter.isCursorVisible = b
        }

        val constraintLayout: ConstraintLayout = findViewById(R.id.ingridient_constraintLayout)
        constraintLayout.setOnClickListener {
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(foodNameEnter.getWindowToken(), 0)

            foodNameEnter.clearFocus()
        }

        return foodNameEnter
    }

    private fun setIngredientScrollList(adapter: FoodItemAdapter) {
        val listView: RecyclerView = findViewById(R.id.ingridientList)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter


        adapter.onClick = { food, view ->
            if (lastClickedIngredient != null)
                if (lastClickedIngredient!!.id == -1)
                    lastClickedFoodView!!.setBackgroundResource(R.drawable.unselected_item_background)
                else lastClickedFoodView!!.setBackgroundResource(R.drawable.unselected_custom_item_background)
            if (food.id == -1)
                view.setBackgroundResource(R.drawable.selected_item_background)
            else view.setBackgroundResource(R.drawable.selected_custom_item_background)

            lastClickedFoodView = view
            lastClickedIngredient = food
            adapter.lastClickedName = food.name
        }


        adapter.onLongClick = { ingr, _ ->
            if (ingr.id == -1) {
                Toast.makeText(this, "Нельзя удалить данный ингредиент", Toast.LENGTH_SHORT).show()
            }
            else {
                showDeleteDialog(ingr)
            }
        }
    }


    private fun showDeleteDialog(ingr: Ingredient) {
        val dialog = Dialog(this)

        dialog.setContentView(R.layout.dialog_delete_confirm)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val cancelBtt: Button = dialog.findViewById(R.id.delete_cancel_button)
        cancelBtt.setOnClickListener() {
            dialog.cancel()
        }

        val deleteBtt: Button = dialog.findViewById(R.id.delete_confirm_button)
        deleteBtt.setOnClickListener() {
            dbIngredient.deleteIngredient(ingr)

            foodList.remove(ingr)
            adapter.changeList(foodListAsReversed)

            dialog.cancel()
        }
        dialog.show()
    }

    private fun setSelectIngredientButton() {
        val addFoodButton: Button = findViewById(R.id.select_ingredient_button)

        val dialog = Dialog(this)
        addFoodButton.setOnClickListener() {
            // && !Ingredients.contains(lastClickedIngredient) убрано из за несоответствия
            if(lastClickedIngredient != null ) {
                showCustomDialog(dialog)
            }
        }
    }

    private fun showCustomDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_gramm_select)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val intent = Intent(this, CreationFoodActivity::class.java)
            intent.putExtra("dish_id", "fromAddIngredient")

            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()
                val chIngredient = ChosenIngredient(lastClickedIngredient!!, grams)
                Ingredients.add(chIngredient)
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Введите вес", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun setBack() {
        val exitButton: TextView = findViewById(R.id.exit_ingridientList)

        exitButton.setOnClickListener() {
            super.onBackPressed()
        }
    }
}