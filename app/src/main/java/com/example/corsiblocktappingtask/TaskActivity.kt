package com.example.corsiblocktappingtask

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import java.util.*

class TaskActivity: Activity() {

    var score: Int = 0
    var sequence: Int = 2

    lateinit var helpView: TextView
    lateinit var scoreView: TextView
    lateinit var doneBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var random: Random

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        helpView = findViewById(R.id.helpView)
        scoreView = findViewById(R.id.scoreView)
        doneBtn = findViewById(R.id.done)
        tableLayout = findViewById(R.id.tablelayout)
        random = Random()

        start()
    }

    // function designed to start the sequence
    private fun start() {
        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                Log.i(TAG, box.tag.toString())
            }
        }
    }

    // function designed to check the user's sequence
    fun check() {

    }

    companion object {
        private val TAG = "TaskActivity"
    }
}