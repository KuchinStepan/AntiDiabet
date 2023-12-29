package com.example.antidiabet1

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener


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


        addingFoodScreen()
    }

    override fun onBackPressed() {}

    private fun setData(count: Int, range: Float) {

        val values = ArrayList<Entry>()

        for (i in 0 until count) {

            val value = (Math.random() * range).toFloat() - 30
            values.add(Entry(i.toFloat(), value/*, resources.getDrawable(R.drawable.star)*/))
        }

        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")

            set1.setDrawIcons(false)

            // draw dashed line
//            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
//            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area
            /*if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }*/

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

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
//        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
//        TODO("Not yet implemented")
    }
}