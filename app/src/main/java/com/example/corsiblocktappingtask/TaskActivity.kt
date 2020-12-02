package com.example.corsiblocktappingtask

import android.app.Activity
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class TaskActivity: Activity(), View.OnTouchListener {

    var level: Int = 2
    var score = 0

    lateinit var helpView: TextView
    lateinit var scoreView: TextView
    lateinit var doneBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var random: Random
    lateinit var boxes: ArrayList<TextView>
    lateinit var hash: HashMap<TextView, Int?>


    private var userSequence = ArrayList<Int>()
    private var sequence = ArrayList<Int>()

    private var highlightColor: Int = 0
    private var boxColor: Int = 0

    private var userIsRight = true

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


        scoreView.setText("Score: $score")


        var i=0

        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                boxes.add(box)
                hash[box] = i
                i+=1
            }
        }

        helpView.setOnClickListener { showHelpDialog() }

        startGame()

        doneBtn.setOnClickListener { gameLoop() }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun gameLoop(){
        val bool = checkUserResponse()

        if (bool){
            score += level
            scoreView.setText("Score: $score")
            level += 1
            startGame()
            Toast.makeText(applicationContext, "Good Work, next level", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "You dishonor da famiry", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun startGame(){
        startSequence()
        captureUserResponse()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun startSequence() {
        GlobalScope.launch(Dispatchers.Main) {

            sequence.clear()
            for (i in 1..level) {
                val index :Int = (0..8).random()
                val box: TextView = boxes[index]

                withContext(Dispatchers.Default) { delay(800) }

                highlightBox(box)

                withContext(Dispatchers.Default) { delay(800) }

                unHighlightBox(box)

                sequence.add(index)
            }
        }
    }

    private fun captureUserResponse() {
        userSequence.clear()
        for (box in boxes) {
            box.setOnTouchListener(this)
        }
    }

    private fun passOrFail(){
        val bool = checkUserResponse()
        if (bool){
            Toast.makeText(applicationContext, "Good Work", Toast.LENGTH_SHORT).show()
            level+=1
        }
        else {
            Toast.makeText(applicationContext, "DUMMY", Toast.LENGTH_SHORT).show()
            userIsRight=false
        }
    }

    private fun checkUserResponse(): Boolean {
        var i=0
        if (sequence.size != userSequence.size){
            return false
        }
        while (i < sequence.size){
            if (sequence[i] != userSequence[i]){
                return false
            }
            i+=1
        }
        return true
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
                val i = hash[box]
                if (i != null){
                    userSequence.add(i)
                }
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
