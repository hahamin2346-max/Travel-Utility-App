package com.example.travelutilityapp.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.NotificationPreferences
import com.example.travelutilityapp.data.ScheduleRepository
import com.example.travelutilityapp.ui.schedule.ScheduleEntry
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId

const val CLASS_REMINDER_CHANNEL_ID = "class_reminder"
private const val REQUEST_CODE = 1001
private const val MINUTES_BEFORE_CLASS = 5L

/**
 * Schedules a single exact alarm for the next upcoming "class starts in 5 minutes" moment
 * across the (daily, weekday) schedule, re-chaining to the following one each time it fires.
 */
object ClassReminderScheduler {

    fun nextTrigger(
        entries: List<ScheduleEntry>,
        now: LocalDateTime,
        minutesBefore: Long = MINUTES_BEFORE_CLASS
    ): LocalDateTime? {
        if (entries.isEmpty()) return null
        for (dayOffset in 0..7) {
            val date = now.toLocalDate().plusDays(dayOffset.toLong())
            if (date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY) continue
            val trigger = entries
                .map { LocalDateTime.of(date, it.startTime).minusMinutes(minutesBefore) }
                .filter { it.isAfter(now) }
                .minOrNull()
            if (trigger != null) return trigger
        }
        return null
    }

    fun ensureChannel(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            CLASS_REMINDER_CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }
        manager.createNotificationChannel(channel)
    }

    /** Recomputes and (re)schedules the next class-reminder alarm, or cancels it if disabled/unpermitted/none left. */
    fun reschedule(context: Context) {
        val appContext = context.applicationContext
        val alarmManager = appContext.getSystemService(AlarmManager::class.java) ?: return
        val pendingIntent = pendingIntent(appContext)

        val enabled = NotificationPreferences(appContext).isReminderEnabled()
        val hasPostPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

        if (!enabled || !hasPostPermission) {
            alarmManager.cancel(pendingIntent)
            return
        }

        val entries = ScheduleRepository(appContext).load()
        val trigger = nextTrigger(entries, LocalDateTime.now())
        if (trigger == null) {
            alarmManager.cancel(pendingIntent)
            return
        }

        ensureChannel(appContext)
        val triggerMillis = trigger.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val canScheduleExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()
        try {
            if (canScheduleExact) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            }
        } catch (e: SecurityException) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
        }
    }

    fun cancel(context: Context) {
        val appContext = context.applicationContext
        val alarmManager = appContext.getSystemService(AlarmManager::class.java) ?: return
        alarmManager.cancel(pendingIntent(appContext))
    }

    private fun pendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, ClassReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
