package com.example.screenlimit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker

// MainActivity.kt
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hourPicker = findViewById<NumberPicker>(R.id.hourPicker)
        val minutePicker = findViewById<NumberPicker>(R.id.minutePicker)
        val extendButton = findViewById<Button>(R.id.extendButton)

        // Retrieve the used time from the Intent
        val usedTimeInMinutes = intent.getIntExtra("USED_TIME", 0)

        // Calculate hours and minutes
        val hours = usedTimeInMinutes / 60
        val minutes = usedTimeInMinutes % 60

        // Set the NumberPickers to match the used time
        hourPicker.value = hours
        minutePicker.value = minutes

        extendButton.setOnClickListener {
            val selectedHours = hourPicker.value
            val selectedMinutes = minutePicker.value

            // Calculate the total duration in minutes
            val totalDuration = selectedHours * 60 + selectedMinutes

            // Now you can use totalDuration to extend the time
        }
    }
}

