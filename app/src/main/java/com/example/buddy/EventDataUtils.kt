package com.example.buddy

import android.content.Context
import android.content.SharedPreferences

object EventDataUtils {
    private const val PREFS_NAME = "CalendarEvents"

    fun clearEventData(context: Context) {
        // Clear events stored in SharedPreferences
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}