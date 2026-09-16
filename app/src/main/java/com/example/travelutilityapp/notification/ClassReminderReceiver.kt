package com.example.travelutilityapp.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.travelutilityapp.MainActivity
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.NotificationPreferences

private const val NOTIFICATION_ID = 2001

/** Fires 5 minutes before the next class starts, then chains the following alarm. */
class ClassReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (NotificationPreferences(context).isReminderEnabled()) {
            showNotification(context)
        }
        ClassReminderScheduler.reschedule(context)
    }

    private fun showNotification(context: Context) {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CLASS_REMINDER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_class_reminder)
            .setContentTitle(context.getString(R.string.notification_class_reminder_title))
            .setContentText(context.getString(R.string.notification_class_reminder_body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        runCatching {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
        }
    }
}
