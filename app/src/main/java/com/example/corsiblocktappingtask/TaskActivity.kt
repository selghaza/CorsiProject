package com.example.corsiblocktappingtask

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.timerTask

class TaskActivity: AppCompatActivity(), View.OnTouchListener {

    lateinit var helpView: TextView
    lateinit var scoreView: TextView
    lateinit var doneBtn: Button
    lateinit var tableLayout: TableLayout
    lateinit var random: Random
    lateinit var boxes: ArrayList<TextView>
    lateinit var hash: HashMap<TextView, Int?>
    lateinit var imageView: ImageView
    lateinit var animatedVectorDrawableCompat: AnimatedVectorDrawableCompat
    lateinit var animatedVectorDrawable: AnimatedVectorDrawable


//    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


    var scorePref: SharedPreferences? = null
    val MyPREFERENCES = "myprefs"
    val value = "key"

    private var userSequence = ArrayList<Int>()
    private var sequence = ArrayList<Int>()
    private var level: Int = 2
    private var score: Int = 0
    private var highlightColor: Int = 0
    private var boxColor: Int = 0
    private var enableOnTouch: Boolean = false



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
        imageView = findViewById(R.id.imageView)
        highlightColor = resources.getColor(R.color.colorHighlightBox)
        boxColor = resources.getColor(R.color.colorPrimaryDark)
        scoreView.text = "Score: $score"



        scorePref = getPreferences(Context.MODE_PRIVATE)


        var i: Int = 0 // index for hashing box views

        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                boxes.add(box)
                hash[box] = i++
            }
        }

        helpView.setOnClickListener { showHelpDialog() }

        startGame()

        doneBtn.setOnClickListener { gameLoop() }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun gameLoop() {
        val isCorrect: Boolean = checkUserResponse()

        if (isCorrect){
            // update fields
            score++
            scoreView.text = "Score: $score"
            level++
            // show result to user
            imageView.setBackgroundResource(R.drawable.avd_correct)
            animateResult()
            // restart game after 2s delay
            Timer().schedule(timerTask { startGame() }, 2000)

        } else {

//            Updating high score
            val highScore = scorePref?.getInt(KEY, -1)
            if (score > highScore!!){
                val e = scorePref?.edit()
                e?.putInt(KEY, score)
                e?.apply()
            }

            // show result to user
            imageView.setBackgroundResource(R.drawable.avd_incorrect)
            animateResult()

            // show restart dialog after 2s delay
            showRestartDialog()
        }
    }

    private fun animateResult() {

        if (imageView.visibility == View.INVISIBLE) {
            imageView.visibility = View.VISIBLE
        }

        // conditional is for backward compatibility
        if (imageView.background is AnimatedVectorDrawableCompat) {
            animatedVectorDrawableCompat = imageView.background as AnimatedVectorDrawableCompat
            animatedVectorDrawableCompat.start()
        } else {
            animatedVectorDrawable = imageView.background as AnimatedVectorDrawable
            animatedVectorDrawable.start()
        }

        // hide animation after 2s delay
        Timer().schedule(timerTask { imageView.visibility = View.INVISIBLE }, 2000)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun startGame() {
        GlobalScope.launch { startSequence() }
        captureUserResponse()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun startSequence() {
        enableOnTouch = false
        doneBtn.isClickable = false
        val job = GlobalScope.launch {
            sequence.clear()
            for (i in 1..level) {
                val index: Int = (0..8).random()
                val box: TextView = boxes[index]

                delay(800)

                highlightBox(box)

                delay(800)

                unHighlightBox(box)

                sequence.add(index)
            }
        }
        /* wait for sequence to finish during which onTouch of box views
            and clickable of done button are disabled */
        job.join()
        enableOnTouch = true
        doneBtn.isClickable = true
    }

    private fun captureUserResponse() {
        userSequence.clear()
        boxes.forEach { it.setOnTouchListener(this) }
    }

    private fun checkUserResponse(): Boolean {
        var i: Int = 0
        if (sequence.size != userSequence.size){
            return false
        }
        while (i < sequence.size){
            if (sequence[i] != userSequence[i]){
                return false
            }
            i++
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showRestartDialog() {
        // show dialog after 2s delay -- requires delaying main thread
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            val dialogBuilder = AlertDialog.Builder(this).apply {
                this.setTitle("Restart")
                this.setMessage("Would you like to try again?")
                this.setPositiveButton("Yes") { _, _ ->
                    // reset fields
                    score = 0
                    level = 2
                    scoreView.text = "Score: $score"
                    startGame()
                }
                this.setNegativeButton("No") { _, _ ->
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.resolveActivity(packageManager)?.let { startActivity(intent) }
                }
            }
            val dialog: AlertDialog = dialogBuilder.create()
            dialog.show()
        }, 2000)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        if (!enableOnTouch) return false

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
        private val KEY = "key"
    }
}
