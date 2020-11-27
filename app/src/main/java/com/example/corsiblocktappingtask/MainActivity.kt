package com.example.corsiblocktappingtask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeBtn = findViewById<Button>(R.id.home_btn)

        homeBtn.setOnClickListener {
            val intent = Intent(this, ReadyActivity::class.java)
            intent.resolveActivity(packageManager)?.let { startActivity(intent) }
        }
    }
}