package com.example.screenlimit

import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper

class UsageMonitoringService : Service() {
    private lateinit var preferencesHelper: SharedPreferencesHelper
    private lateinit var appUsageHelper: AppUsageHelper

    private val usageStatsManager: UsageStatsManager by lazy {
        getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval: Long = 1000 * 60 // Check every minute

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        appUsageHelper = AppUsageHelper(this)
        preferencesHelper = SharedPreferencesHelper(this)

        if (appUsageHelper.hasUsageStatsPermission()) {
            monitorUsage()
        } else {
            // Handle lack of permission, e.g., notify the user to enable it
        }

        return START_STICKY
    }

    private fun monitorUsage() {
        val usedTimeInMinutes = appUsageHelper.getTotalUsageTime()

        // Retrieve the user-defined time limit from SharedPreferencesHelper
        val userDefinedLimit = preferencesHelper.getUserDefinedLimit()

        if (usedTimeInMinutes >= userDefinedLimit) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USED_TIME", usedTimeInMinutes)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            // Continue monitoring...
        }
    }

    private fun calculateUsedTime(): Int {
        return appUsageHelper.getTotalUsageTime()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the handler when the service is destroyed
        handler.removeCallbacksAndMessages(null)
    }
}
