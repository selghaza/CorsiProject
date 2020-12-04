package com.example.corsiblocktappingtask

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class ReadyActivity: AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer
    lateinit var countDownView: TextView

    companion object {
        val TAG = "ReadyActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ready)
        countDownView = findViewById<TextView>(R.id.countDown_view)
        Handler(Looper.myLooper()!!).postDelayed({
            startTimer()
        }, 1000)
    }

    private fun startTimer() {
        countDownTimer = object: CountDownTimer(3000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                countDownView.text = if (time == 0L) "Go!" else (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                Log.i(TAG, "count down finished")
                val intent = Intent(applicationContext, TaskActivity::class.java)
                intent.resolveActivity(packageManager)?.let { startActivity(intent) }
            }
        }
        countDownTimer.start()
    }
}