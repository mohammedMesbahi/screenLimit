package com.example.screenlimit

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import java.util.*

class AppUsageHelper(private val context: Context) {

    private val usageStatsManager: UsageStatsManager by lazy {
        context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    /**
     * Get total usage time for the current day (in minutes) for a specific app.
     * @param packageName The package name of the app to check.
     * @return Total usage time in minutes.
     */
    fun getAppUsageTime(packageName: String): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val stats = usageStatsManager.queryAndAggregateUsageStats(startTime, endTime)
        val usageStats = stats[packageName]

        return usageStats?.totalTimeInForeground?.let {
            (it / (1000 * 60)).toInt() // Convert from milliseconds to minutes
        } ?: 0
    }

    /**
     * Get total usage time for all apps for the current day (in minutes).
     * @return Total usage time in minutes.
     */
    fun getTotalUsageTime(): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        val stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime)
        var totalTime = 0L

        stats?.forEach { usageStat ->
            totalTime += usageStat.totalTimeInForeground
        }

        return (totalTime / (1000 * 60)).toInt() // Convert from milliseconds to minutes
    }

    /**
     * Check if the app has the necessary permission to retrieve usage statistics.
     * @return True if permission is granted, false otherwise.
     */
    fun hasUsageStatsPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
            val mode = appOps.checkOpNoThrow(
                android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), context.packageName
            )
            return mode == android.app.AppOpsManager.MODE_ALLOWED
        }
        return false
    }
}
