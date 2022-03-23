package com.chiru.tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.util.*


class MainActivity : AppCompatActivity() {
    private val days = mutableListOf<Day>()
    private var selectedDay = LocalDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dayNameView = findViewById<RecyclerView>(R.id.rv_dayNames)
        val dayNameAdapter = DayNameAdapter(DayOfWeek.values().map { toFirstUpper(it.name).substring(0 .. 1) + "." })
        dayNameView.layoutManager = GridLayoutManager(this, 7)
        dayNameView.adapter = dayNameAdapter

        updateDaysInView(selectedDay)
        val view = findViewById<RecyclerView>(R.id.rv_days)
        val adapter = DayAdapter(days, {
            Toast.makeText(this, days[it].nr.toString(), Toast.LENGTH_SHORT).show()
        }, true)

        // Disable scrolling: https://www.geeksforgeeks.org/how-to-disable-recyclerview-scrolling-in-android/
        view.layoutManager = object: GridLayoutManager(this, 7){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        view.adapter = adapter

        val btnBefore = findViewById<Button>(R.id.btn_monthBefore)
        val btnAfter = findViewById<Button>(R.id.btn_monthAfter)
        val tv = findViewById<TextView>(R.id.tv_month)

        tv.setOnClickListener {
            selectedDay = LocalDateTime.now()
            updateDaysInView(selectedDay)
            adapter.notifyDataSetChanged()
        }

        btnBefore.setOnClickListener {
            selectedDay = selectedDay.minusMonths(1)
            updateDaysInView(selectedDay)
            adapter.notifyDataSetChanged()
        }

        btnAfter.setOnClickListener {
            selectedDay = selectedDay.plusMonths(1)
            updateDaysInView(selectedDay)
            adapter.notifyDataSetChanged()
        }
    }

    fun toFirstUpper(str: String) = str.lowercase().replaceFirstChar {it.uppercase()}

    fun updateDaysInView(dt: LocalDateTime) {
        days.clear()

        val tv = findViewById<TextView>(R.id.tv_month)
        tv.text = toFirstUpper(dt.month.name) + " " + dt.year.toString()

        val firstDay = dt.with(ChronoField.DAY_OF_MONTH, 1).toLocalDate()
        val lastDay = firstDay.plusMonths(1).minusDays(1)

        val idxFirst = firstDay.dayOfWeek.value - 1

        for (i in 1 .. idxFirst)
            days.add(Day(-1, true))

        for (i in 1 .. lastDay.dayOfMonth)
            days.add(Day(i))

        for (i in 1 .. 6*7 - lastDay.dayOfMonth - idxFirst)
            days.add(Day(-1, true))
    }
}