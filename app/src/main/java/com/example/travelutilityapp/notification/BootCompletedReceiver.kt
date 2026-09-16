package com.example.travelutilityapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Re-arms the class-reminder alarm after a reboot, since AlarmManager alarms don't survive it. */
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            ClassReminderScheduler.reschedule(context)
        }
    }
}
