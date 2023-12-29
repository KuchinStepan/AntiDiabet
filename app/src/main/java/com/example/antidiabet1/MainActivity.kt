package com.example.antidiabet1

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.antidiabet1.data_base_classes.EventHistoryDatabaseHelper
import com.example.antidiabet1.data_base_classes.EventType
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

        val xAxis = chart.xAxis
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
//        xAxis.enableGridDashedLine(10f,10f,0f)

        xAxis.valueFormatter = MyXAxisFormatter()



        val yAxis = chart.axisLeft
        yAxis.setDrawGridLines(false);

//        chart.axisRight.isEnabled = false
//        yAxis.enableGridDashedLine(10f, 10f, 0f)
        yAxis.axisMaximum = 20f
        yAxis.axisMinimum = 0f
////
////        val llXAxis = LimitLine(9f, "Index 10")
////        llXAxis.lineWidth = 4f
////        llXAxis.enableDashedLine(10f, 10f, 0f)
////        llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
////        llXAxis.textSize = 10f
////        //llXAxis.typeface = tfRegular
////
////        val ll1 = LimitLine(150f, "Upper Limit")
////        ll1.lineWidth = 4f
////        ll1.enableDashedLine(10f, 10f, 0f)
////        ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
////        ll1.textSize = 10f
////        //ll1.typeface = tfRegular
////
////        val ll2 = LimitLine(-30f, "Lower Limit")
////        ll2.lineWidth = 4f
////        ll2.enableDashedLine(10f, 10f, 0f)
////        ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
////        ll2.textSize = 10f
////        //ll2.typeface = tfRegular
////
////        // draw limit lines behind data instead of on top
////        yAxis.setDrawLimitLinesBehindData(true)
////        xAxis.setDrawLimitLinesBehindData(true)
////
////        // add limit lines
////        yAxis.addLimitLine(ll1)
////        yAxis.addLimitLine(ll2)
////
        setData()
////
////        chart.animateX(1500)
        val legend = chart.legend
        legend.form = Legend.LegendForm.LINE
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);


        addingFoodScreen()
    }

    override fun onBackPressed() {}

    private fun setData() {
        val values = ArrayList<Entry>()
        val dbHelper = EventHistoryDatabaseHelper(this, null)
        val events = dbHelper.getEventsByLastThreeDays()
        val set: LineDataSet

        var lastSugar = 10.0
        for (event in events){
            val dateValue = event.date
            if (event.type == EventType.SugarMeasure)
                lastSugar = event.sugar
            val entry = Entry(dateValue.time.toFloat(), lastSugar.toFloat())
            values.add(entry)
        }

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set = chart.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            set.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set = LineDataSet(values, "Уровень глюкозы в крови")

            set.setDrawIcons(false)

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set.color = Color.BLACK
            set.setCircleColor(Color.BLACK)

            // line thickness and point size
            set.lineWidth = 1f
            set.circleRadius = 3f

            // draw points as solid circles
            set.setDrawCircleHole(false)

            // customize legend entry
            set.formLineWidth = 1f
            set.formSize = 15f

            // text size of values
            set.valueTextSize = 15f

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set.setDrawFilled(false)

            // set color of filled area
            /*if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }*/

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }

    private fun generateFakeHistory() {

    }

    private fun addingFoodScreen() {
        val button: Button = findViewById(R.id.food_button)

        button.setOnClickListener() {
            val intent = Intent(this, SelectionFoodActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }

    override fun onNothingSelected() {
    }
}

class MyXAxisFormatter : ValueFormatter() {
    private val dateFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val millis = value.toLong()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = millis
        dateFormatter.format(calendar.time)

        return dateFormatter.format(calendar.time)
    }
}