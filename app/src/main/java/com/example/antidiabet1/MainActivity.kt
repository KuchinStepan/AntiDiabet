package com.example.antidiabet1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.antidiabet1.data_base_classes.Event
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.util.Date


class MainActivity : AppCompatActivity(), OnChartValueSelectedListener {
    lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chart = findViewById(R.id.chart1)
        chart.setBackgroundColor(Color.WHITE)
        chart.description.isEnabled = false
        chart.setOnChartValueSelectedListener(this)
        chart.setDrawGridBackground(false)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)

        /*val xAxis = chart.xAxis
//        xAxis.enableGridDashedLine(10f,10f,0f)

        val yAxis = chart.axisLeft
        chart.axisRight.isEnabled = false
//        yAxis.enableGridDashedLine(10f, 10f, 0f)
//        yAxis.setAxisMaximum(200f)
//        yAxis.setAxisMinimum(-50f)
//
//        val llXAxis = LimitLine(9f, "Index 10")
//        llXAxis.lineWidth = 4f
//        llXAxis.enableDashedLine(10f, 10f, 0f)
//        llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
//        llXAxis.textSize = 10f
//        //llXAxis.typeface = tfRegular
//
//        val ll1 = LimitLine(150f, "Upper Limit")
//        ll1.lineWidth = 4f
//        ll1.enableDashedLine(10f, 10f, 0f)
//        ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
//        ll1.textSize = 10f
//        //ll1.typeface = tfRegular
//
//        val ll2 = LimitLine(-30f, "Lower Limit")
//        ll2.lineWidth = 4f
//        ll2.enableDashedLine(10f, 10f, 0f)
//        ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
//        ll2.textSize = 10f
//        //ll2.typeface = tfRegular
//
//        // draw limit lines behind data instead of on top
//        yAxis.setDrawLimitLinesBehindData(true)
//        xAxis.setDrawLimitLinesBehindData(true)
//
//        // add limit lines
//        yAxis.addLimitLine(ll1)
//        yAxis.addLimitLine(ll2)
//
        setData(45, 180f)
//
//        chart.animateX(1500)
//        val legend = chart.legend
//        legend.form = Legend.LegendForm.LINE
*/

        setEventButton()
        setHistoryButton()

    }

    override fun onBackPressed() {}


//    private fun setData(count: Int, range: Float) {
//        val values = ArrayList<Entry>()
//
//        for (i in 0 until count) {
//
//            val value = (Math.random() * range).toFloat() - 30
//            values.add(Entry(i.toFloat(), value/*, resources.getDrawable(R.drawable.star)*/))
//        }
//
//        val set1: LineDataSet
//
//        if (chart.data != null && chart.data.dataSetCount > 0) {
//            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
//            set1.values = values
//            set1.notifyDataSetChanged()
//            chart.data.notifyDataChanged()
//            chart.notifyDataSetChanged()
//        } else {
//            // create a dataset and give it a type
//            set1 = LineDataSet(values, "DataSet 1")
//
//            set1.setDrawIcons(false)
//
//            // draw dashed line
////            set1.enableDashedLine(10f, 5f, 0f)
//
//            // black lines and points
//            set1.color = Color.BLACK
//            set1.setCircleColor(Color.BLACK)
//
//            // line thickness and point size
//            set1.lineWidth = 1f
//            set1.circleRadius = 3f
//
//            // draw points as solid circles
//            set1.setDrawCircleHole(false)
//
//            // customize legend entry
//            set1.formLineWidth = 1f
//            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
//            set1.formSize = 15f
//
//            // text size of values
//            set1.valueTextSize = 9f
//
//            // draw selection line as dashed
////            set1.enableDashedHighlightLine(10f, 5f, 0f)
//
//            // set the filled area
//            set1.setDrawFilled(true)
//            set1.fillFormatter =
//                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }
//
//            // set color of filled area
//            /*if (Utils.getSDKInt() >= 18) {
//                // drawables only supported on api level 18 and above
//                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
//                set1.fillDrawable = drawable
//            } else {
//                set1.fillColor = Color.BLACK
//            }*/
//
//            val dataSets = ArrayList<ILineDataSet>()
//            dataSets.add(set1) // add the data sets
//
//            // create a data object with the data sets
//            val data = LineData(dataSets)
//
//            // set data
//            chart.data = data
//        }
//    }

    private fun generateFakeHistory() {

    }

    private fun setEventButton() {
        val button: Button = findViewById(R.id.event_button)

        button.setOnClickListener() {
            showEventChoiceDialog(this)
        }
    }

    private fun setHistoryButton() {
        val button: Button = findViewById(R.id.history_button)

        button.setOnClickListener() {
            showCalendarDialog(this)
        }
    }

    private fun showCalendarDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_calendar_choice)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        val textView: TextView = dialog.findViewById(R.id.textView3)
        val dateSelectButton: Button = dialog.findViewById(R.id.selectDate)
        dateSelectButton.setOnClickListener() {
            val intent = Intent(context, DateHistoryActivity::class.java)
            startActivity(intent)
        }
        val calendarView: CalendarView = dialog.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            textView.text = "$dayOfMonth.${month + 1}.$year"
        }


        dialog.show()
    }

    private fun showEventChoiceDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_event_choice)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val foodBtt: Button = dialog.findViewById(R.id.get_food)

        foodBtt.setOnClickListener() {
            val intent = Intent(context, SelectionFoodActivity::class.java)
            startActivity(intent)
        }

        val db = EventHistoryDatabaseHelper(context, null)

        val insulinBtt: Button = dialog.findViewById(R.id.get_insulin)
        insulinBtt.setOnClickListener() {
            dialog.cancel()
            setInsulinFun(context, db)
        }

        val sugarBtt: Button = dialog.findViewById(R.id.measure_sugar)
        sugarBtt.setOnClickListener() {
            dialog.cancel()
            setSugarFun(context, db)
        }
        
        dialog.show()
    }

    private fun setInsulinFun(context: Context, db: EventHistoryDatabaseHelper) {
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
                db.addEvent(event)

                dialog.cancel()
            }
            else {
                Toast.makeText(context, "Введите количество ЕД", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun setSugarFun(context: Context, db: EventHistoryDatabaseHelper) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_choice_sugar)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val ok_button: Button = dialog.findViewById(R.id.ok_button)
        val gramm_enter_text: EditText = dialog.findViewById(R.id.gramm_enter)

        ok_button.setOnClickListener() {
            val text = gramm_enter_text.text.toString()
            if (text != "") {
                val grams = text.toDouble()

                val event = Event(Date(), EventType.SugarMeasure, null, 0.0, grams)
                db.addEvent(event)

                dialog.cancel()
            }
            else {
                Toast.makeText(context, "Введите количество ммоль/л", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
//        val dbHelper = EventHistoryDatabaseHelper(this, null)
//        val events = dbHelper.getAllEvents()
//        Log.d("--> MEOW", events.size.toString())
//        for (i in 0 until events.size) {
//            val event = events[i]
//            Log.d("--> MEOW", event.date.toString())
//            Log.d("--> MEOW", event.dishItem.toString())
//        }
    }

    override fun onNothingSelected() {
//            val dbHelper = EventHistoryDatabaseHelper(this, null)
//            val events = dbHelper.getAllEvents()
//            Log.d("--> MEOW", events.size.toString())
//            for (i in 0 until events.size) {
//                val event = events[i]
//                Log.d("--> MEOW", event.date.toString())
//                Log.d("--> MEOW", event.dishItem.toString())
//            }
    }
}