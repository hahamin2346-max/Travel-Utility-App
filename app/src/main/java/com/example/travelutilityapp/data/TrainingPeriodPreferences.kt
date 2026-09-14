package com.example.travelutilityapp.data

import android.content.Context
import java.time.LocalDate

private const val PREFS_NAME = "training_period_prefs"
private const val KEY_START_DATE = "start_date"
private const val KEY_END_DATE = "end_date"

/** Persists the user-selected study-abroad start/end dates across app restarts. */
class TrainingPeriodPreferences(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): Pair<LocalDate, LocalDate>? {
        val startText = prefs.getString(KEY_START_DATE, null) ?: return null
        val endText = prefs.getString(KEY_END_DATE, null) ?: return null
        return try {
            LocalDate.parse(startText) to LocalDate.parse(endText)
        } catch (e: java.time.format.DateTimeParseException) {
            null
        }
    }

    fun save(start: LocalDate, end: LocalDate) {
        prefs.edit()
            .putString(KEY_START_DATE, start.toString())
            .putString(KEY_END_DATE, end.toString())
            .apply()
    }
}
