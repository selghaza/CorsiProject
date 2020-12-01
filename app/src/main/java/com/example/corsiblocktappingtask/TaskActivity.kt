package com.example.corsiblocktappingtask

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList




class TaskActivity: Activity() {


    var level: Int = 0
    var sequence: Int = 2

    lateinit var helpView: TextView
    lateinit var scoreView: TextView
    lateinit var doneBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var random: Random
    lateinit var boxes: ArrayList<TextView>

    val highlightColor = Color.argb(160, 255, 255, 255)
    lateinit var box: View


    private var mHandler: Handler = Handler()


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        helpView = findViewById(R.id.helpView)
        scoreView = findViewById(R.id.scoreView)
        doneBtn = findViewById(R.id.done)
        tableLayout = findViewById(R.id.tablelayout)
        random = Random()
        boxes = ArrayList<TextView>()

        val tableRow = tableLayout.getChildAt(1) as TableRow
        box = tableRow.getChildAt(1) as TextView

        var i=0

        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                boxes.add(box)
            }
        }

        start()

        doneBtn.setOnClickListener{
                // check()
        }
    }


    private fun start() {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                delay(1000)
            }
            boxes[0].setBackgroundColor(Color.argb(160, 255, 255, 255))
            withContext(Dispatchers.Default) {
                delay(1000)
            }
            boxes[0].setBackgroundColor(Color.parseColor("#7c4dff"))
        }
//            val boxes = with(tableLayout.getChildAt(0) as TableRow) {
//                (0..2).map { getChildAt(it) }
//            }
//
//            delay(1000L)
//            boxes.forEach { it.setBackgroundColor(Color.argb(160, 255, 255, 255)) }
//
//            delay(1000L)
//            boxes.forEach { it.setBackgroundColor(Color.parseColor("#7c4dff")) }
//        }
    }

    /*
    // function designed to start the sequence
    private fun start() {

        val r1: Runnable = object : Runnable {
            override fun run() {
                box.setBackgroundColor(highlightColor)
            }
        }

        val r2: Runnable = object : Runnable {
            override fun run() {
                box.setBackgroundColor(Color.parseColor("#7c4dff"))
            }
        }

//        // initialize 9 boxes
//        for (row_index in 0 until tableLayout.childCount) {
//            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
//            for (box_index in 0 until tableRow.childCount) {
//                val box: TextView = tableRow.getChildAt(box_index) as TextView
//                boxes.add(box)
//            }

        var i=0
        var randomInt=0
        var row=0
        var col=0
        val seqList = arrayListOf<Int>()



//        Limit should be level

//            randomInt = random.nextInt(8)
//
//            println("Curr iteration: $i")
//            println("Random int: $randomInt")
////            Random integer from 0-8
//
//            row = randomInt/3
//            col = randomInt%3
//            seqList.add(randomInt)


        while (i<3) {
            val tableRow = tableLayout.getChildAt(0) as TableRow
            box = tableRow.getChildAt(i) as TextView

            val handler = Handler(Looper.getMainLooper())

            handler.postDelayed(r1, 1000)
            handler.postDelayed(r2, 2000)
            i+=1
        }


//            val handler = Handler(Looper.getMainLooper())
//            handler.post {
//                box.setBackgroundColor(highlightColor)
//    //            box.setBackgroundColor(Color.parseColor("#7c4dff"))
//            }
//
//            handler.postDelayed(rt, 1000)

//            box.setBackgroundColor(highlightColor)
//            mHandler.postDelayed(rt, 1000)
//            mHandler.post { box.setBackgroundColor(Color.parseColor("#7c4dff")) }

//            box.setBackgroundColor(Color.parseColor("#7c4dff"))

//        doneBtn.setOnClickListener {
//            Thread.sleep(1000)
//            box.setBackgroundColor(highlightColor)
//            mHandler.postDelayed(rt, 1000)
//        }



//        for (row_index in 0 until tableLayout.childCount) {
//            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
//            for (box_index in 0 until tableRow.childCount) {
//                val box: TextView = tableRow.getChildAt(box_index) as TextView
//                Log.i(TAG, box.tag.toString())
//            }
//        }
    }
*/



//    fun startRepeating(v: View?) {
//        println("Entered start repeating")
//        mHandler.postDelayed(mToastRunnable, 50000);
//        mToastRunnable.run()
//        println("End of start repeating")
//        stopRepeating(v)
//    }
//
//    fun stopRepeating(v: View?) {
//        mHandler.removeCallbacks(mToastRunnable)
//    }
//
//    private val mToastRunnable: Runnable = object : Runnable {
//        override fun run() {
//            var color1 = Color.argb(160, 255, 0, 0)
//            box.setBackgroundColor(color1)
//            mHandler.postDelayed(this, 50000)
//
////            color1 = Color.argb(160, 0, 255, 0)
////            box.setBackgroundColor(color1)
//        }
//    }



    // function designed to check the user's sequence
    fun check() {

    }

    companion object {
        private val TAG = "TaskActivity"
    }
}