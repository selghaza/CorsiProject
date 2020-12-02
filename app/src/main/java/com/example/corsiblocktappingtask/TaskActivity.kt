package com.example.corsiblocktappingtask

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TaskActivity: Activity(), View.OnTouchListener {

    var level: Int = 0
    var sequence: Int = 2

    lateinit var helpView: TextView
    lateinit var scoreView: TextView
    lateinit var doneBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var random: Random
    lateinit var boxes: ArrayList<TextView>
    lateinit var hash: HashMap<TextView, Int?>

    private var highlightColor: Int = 0
    private var boxColor: Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        helpView = findViewById(R.id.helpView)
        scoreView = findViewById(R.id.scoreView)
        doneBtn = findViewById(R.id.done)
        tableLayout = findViewById(R.id.tablelayout)
        random = Random()
        boxes = ArrayList<TextView>()
        hash = HashMap<TextView, Int?>()
        highlightColor = resources.getColor(R.color.colorHighlightBox)
        boxColor = resources.getColor(R.color.colorPrimaryDark)

        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                boxes.add(box)
                hash[box] = 0
            }
        }

        startSequence()

        captureUserResponse()

        doneBtn.setOnClickListener{ checkUserResponse() }

        helpView.setOnClickListener { showHelpDialog() }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun startSequence() {
        GlobalScope.launch(Dispatchers.Main) {
            for (i in 1..sequence) {
                val index :Int = (0..8).random()
                val box: TextView = boxes[index]

                withContext(Dispatchers.Default) { delay(800) }

                highlightBox(box)

                withContext(Dispatchers.Default) { delay(800) }

                unHighlightBox(box)

                hash[box] = hash[box]?.inc()
            }
        }
    }

    private fun captureUserResponse() {
        for (box in boxes) {
            box.setOnTouchListener(this)
        }
    }

    private fun checkUserResponse() {
        val sum: Int = hash.values.fold(0) { acc, i -> if (i != null) acc.plus(i) else acc }
        if (sum == 0) {
            Log.i(TAG, "correct!")
        } else {
            Log.i(TAG, "not correct!")
        }
    }

    private fun highlightBox(box: TextView) {
        box.setBackgroundColor(highlightColor)
    }

    private fun unHighlightBox(box: TextView) {
        box.setBackgroundColor(boxColor)
    }

    private fun showHelpDialog() {
        val dialogBuilder = AlertDialog.Builder(this).apply {
            this.setTitle(R.string.helpDialogTitle)
            this.setMessage(R.string.helpDialogMessage)
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        val box: TextView = v as TextView

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                highlightBox(box)
                hash[box] = hash[v]?.dec() // only want to decrement once
            }

            MotionEvent.ACTION_UP -> {
                unHighlightBox(box)
            }
        }

        return true
    }

    companion object {
        private val TAG = "TaskActivity"
    }
}
