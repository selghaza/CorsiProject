package com.example.corsiblocktappingtask

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

class TaskActivity: Activity() {

    var score: Int = 0
    var sequence: Int = 2

    lateinit var helpView: TextView
    lateinit var scoreView: TextView
    lateinit var doneBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var random: Random
    lateinit var boxes: ArrayList<TextView>

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        helpView = findViewById(R.id.helpView)
        scoreView = findViewById(R.id.scoreView)
        doneBtn = findViewById(R.id.done)
        tableLayout = findViewById(R.id.tablelayout)
        random = Random()
        boxes = ArrayList<TextView>()

        start()
    }

    // function designed to start the sequence
    private fun start() {
        // initialize 9 boxes
        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                boxes.add(box)
            }
        }

        // create random sequence that user must replicate
        for (seq in 1..sequence) {
            val i: Int = random.nextInt(8)
            val colorDrawables: Array<ColorDrawable> =
                arrayOf(ColorDrawable(resources.getColor(R.color.colorSecondaryDark)), ColorDrawable(resources.getColor(R.color.colorPrimaryDark)))
            val transitionDrawable: TransitionDrawable = TransitionDrawable(colorDrawables)
            boxes[i].background = transitionDrawable
            transitionDrawable.startTransition(1000)
        }
    }

    // function designed to check the user's sequence
    fun check() {

    }

    companion object {
        private val TAG = "TaskActivity"
    }
}