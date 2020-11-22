package com.example.corsiblocktappingtask

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.os.CountDownTimer
import android.os.Handler

class ReadyActivity: Activity() {

    lateinit var countDownTimer: CountDownTimer
    lateinit var countDownView: TextView

    companion object {
        val TAG = "Final Project"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)
        countDownView = findViewById<TextView>(R.id.countDown_view)
        Handler().postDelayed({
            startTimer()
        }, 1000)
    }

    fun startTimer() {
        countDownTimer = object: CountDownTimer(3000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                countDownView.text = if (time == 0L) "Go!" else (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                Log.i(TAG, "count down finished")
                // TODO: This is where TaskActivity should be called
            }
        }
        countDownTimer.start()
    }
}