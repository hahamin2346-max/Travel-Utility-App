package com.example.travelutilityapp.data

import android.content.Context

private const val PREFS_NAME = "notification_prefs"
private const val KEY_REMINDER_ENABLED = "class_reminder_enabled"

/** Persists whether the "class starts in 5 minutes" reminder notification is enabled. */
class NotificationPreferences(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isReminderEnabled(): Boolean = prefs.getBoolean(KEY_REMINDER_ENABLED, true)

    fun setReminderEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply()
    }
}
