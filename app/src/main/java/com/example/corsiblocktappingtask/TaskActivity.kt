package com.example.corsiblocktappingtask

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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


    lateinit var dialog: Dialog
    lateinit var gameOverScore: TextView
    lateinit var restartBtn: Button

    private var userSequence = ArrayList<Int>()
    private var sequence = ArrayList<Int>()
    private var level: Int = 2
    private var score: Int = 0
    private var highlightColor: Int = 0
    private var boxColor: Int = 0
    private var enableOnTouch: Boolean = false
    private var tacoBellSound: MediaPlayer? = null
    private var correctSound: MediaPlayer? = null

    var scorePref: SharedPreferences? = null
    val value = "key"


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
        tacoBellSound = MediaPlayer.create(this, R.raw.dong)
        correctSound = MediaPlayer.create(this, R.raw.correct)
        scorePref = getPreferences(Context.MODE_PRIVATE)

   
        var i: Int = 0 // used to map box views to indices

        // initialize box TextViews, ArrayList, and HashMap
        for (row_index in 0 until tableLayout.childCount) {
            val tableRow: TableRow = tableLayout.getChildAt(row_index) as TableRow
            for (box_index in 0 until tableRow.childCount) {
                val box: TextView = tableRow.getChildAt(box_index) as TextView
                boxes.add(box)
                hash[box] = i++
            }
        }

        helpView.setOnClickListener { showHelpDialog() } // shows dialog with game instructions

        startGame()

        doneBtn.setOnClickListener { gameLoop() }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun highScoreDialog(): Dialog {
        // set up dialog
        dialog = Dialog(this)
        dialog.setContentView(R.layout.highscore_popup)
        Objects.requireNonNull(dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))

        gameOverScore = dialog.findViewById(R.id.gameOverScore)
        gameOverScore.setText("Best: $score\nScore: $score")

        restartBtn = dialog.findViewById(R.id.restartButton)

        restartBtn.setOnClickListener{
            // reset fields
            score = 0
            level = 2
            scoreView.text = "Score: $score"
            // kill dialog
            dialog.dismiss()
            // restart game
            startGame()
        }
        return dialog
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun gameOverDialog(): Dialog {
        // retrieve highscore
        val highScore = scorePref?.getInt(KEY, 0)
        // set up dialog
        dialog = Dialog(this)
        dialog.setContentView(R.layout.game_over_popup)
        Objects.requireNonNull(dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)))

        gameOverScore = dialog.findViewById(R.id.gameOverScore)
        gameOverScore.setText("Best: $highScore\nScore: $score")

        restartBtn = dialog.findViewById(R.id.restartButton)

        restartBtn.setOnClickListener{
            // reset fields
            score = 0
            level = 2
            scoreView.text = "Score: $score"
            // kill dialog
            dialog.dismiss()
            // restart game
            startGame()
        }

        return dialog
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun showCustomDialog(newHighScore: Boolean) {
        // show dialog after 2s delay -- requires delaying main thread
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            // if there is a new highscore, then a highscore dialog is shown.
            // Otherwise, a game over dialog is shown
            dialog = if (newHighScore) highScoreDialog() else gameOverDialog()
            dialog.show()

        }, 2000)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun gameLoop() {
        // check if user input is correct
        val isCorrect: Boolean = checkUserResponse()

        // update score on view
        scoreView.text = "Score: $score"

        if (isCorrect) {

            // play sound
            correctSound?.start()

            // update level field
            level++
            // show animated check mark
            imageView.setBackgroundResource(R.drawable.avd_correct)
            animateResult()
            // restart game after 2s delay
            Timer().schedule(timerTask { startGame() }, 2000)

        } else {

            // play sound
            tacoBellSound?.start()

            var newHighScore = false

            // Updating high score if necessary
            val highScore = scorePref?.getInt(KEY, -1)
            if (score > highScore!!) {
                val e = scorePref?.edit()
                e?.putInt(KEY, score)
                e?.apply()
                newHighScore = true
            }

            // show animated "X" to user
            imageView.setBackgroundResource(R.drawable.avd_incorrect)
            animateResult()

            // show restart dialog
            showCustomDialog(newHighScore)
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
        // disable onTouch of box TextViews and done button
        enableOnTouch = false
        doneBtn.isClickable = false

        val job = GlobalScope.launch {
            // present random sequence for user to replicate
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
        /* wait for sequence to finish at which point onTouch of box views
            and done button are enabled again */
        job.join()
        enableOnTouch = true
        doneBtn.isClickable = true
    }

    private fun captureUserResponse() {
        // clear any previous inputs
        userSequence.clear()
        // setup listener for each TextView
        boxes.forEach { it.setOnTouchListener(this) }
    }

    private fun checkUserResponse(): Boolean {
        var i: Int = 0

        if (sequence.size != userSequence.size) return false

        while (i < sequence.size) {
            if (sequence[i] != userSequence[i]) return false
            score++; i++
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
        // set up help dialog to aid user
        val dialogBuilder = AlertDialog.Builder(this).apply {
            this.setTitle(R.string.helpDialogTitle)
            this.setMessage(R.string.helpDialogMessage)
        }
        val dialog = dialogBuilder.create()
        // show dialog
        dialog.show()
    }

    /*
        Overriding onTouch so that when the user presses down on a box TextView it is colored
        and when the user releases, the box is returned to its original color
     */
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
        private val KEY = "key"
    }
}
