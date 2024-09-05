package com.example.screenlimit

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "ScreenLimitPrefs"
        private const val KEY_USER_DEFINED_LIMIT = "user_defined_limit"
    }

    /**
     * Save the user-defined time limit in minutes.
     */
    fun saveUserDefinedLimit(limitInMinutes: Int) {
        with(preferences.edit()) {
            putInt(KEY_USER_DEFINED_LIMIT, limitInMinutes)
            apply()
        }
    }

    /**
     * Retrieve the user-defined time limit in minutes.
     * Returns the default value if no limit has been set.
     */
    fun getUserDefinedLimit(defaultLimit: Int = 5): Int {
        return preferences.getInt(KEY_USER_DEFINED_LIMIT, defaultLimit)
    }

    /**
     * Clear all saved preferences (optional, if you need to clear data).
     */
    fun clearPreferences() {
        with(preferences.edit()) {
            clear()
            apply()
        }
    }
}
