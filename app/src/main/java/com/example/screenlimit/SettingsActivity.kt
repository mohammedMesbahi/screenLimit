package com.example.screenlimit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var hourPicker: NumberPicker
    private lateinit var minutePicker: NumberPicker
    private lateinit var saveButton: Button
    private lateinit var preferencesHelper: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        preferencesHelper = SharedPreferencesHelper(this)

        hourPicker = findViewById(R.id.hourPicker)
        minutePicker = findViewById(R.id.minutePicker)
        saveButton = findViewById(R.id.saveButton)

        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        minutePicker.minValue = 0
        minutePicker.maxValue = 59

        // Load existing time limit from SharedPreferences
        val userDefinedLimit = preferencesHelper.getUserDefinedLimit()

        // Convert userDefinedLimit back to hours and minutes
        val hours = userDefinedLimit / 60
        val minutes = userDefinedLimit % 60

        hourPicker.value = hours
        minutePicker.value = minutes

        // Save the selected time limit when the button is clicked
        saveButton.setOnClickListener {
            val selectedHours = hourPicker.value
            val selectedMinutes = minutePicker.value
            val totalTimeInMinutes = selectedHours * 60 + selectedMinutes

            preferencesHelper.saveUserDefinedLimit(totalTimeInMinutes)
            finish() // Close the settings activity after saving
        }
    }

    private fun saveUserDefinedLimit(limitInMinutes: Int) {
        val sharedPreferences = getSharedPreferences("ScreenLimitPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("user_defined_limit", limitInMinutes)
            apply()
        }
    }
}
