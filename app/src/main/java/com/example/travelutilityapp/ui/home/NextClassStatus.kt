package com.example.travelutilityapp.ui.home

import com.example.travelutilityapp.ui.schedule.ScheduleEntry
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

private const val IMMINENT_THRESHOLD_MINUTES = 20L

sealed class NextClassStatus {
    data object Weekend : NextClassStatus()
    data object NoClassesRegistered : NextClassStatus()
    data object FinishedForToday : NextClassStatus()
    data class InClass(val entry: ScheduleEntry) : NextClassStatus()
    data class Upcoming(val entry: ScheduleEntry, val minutesUntilStart: Long) : NextClassStatus()
}

/**
 * Derives the Home screen's "next class" status from the user's (daily, weekday) schedule.
 * If a class is in session but the next one starts within [IMMINENT_THRESHOLD_MINUTES],
 * that next class is surfaced instead so the user can prepare to move.
 */
fun computeNextClassStatus(
    entries: List<ScheduleEntry>,
    now: LocalDateTime = LocalDateTime.now()
): NextClassStatus {
    if (now.dayOfWeek == DayOfWeek.SATURDAY || now.dayOfWeek == DayOfWeek.SUNDAY) {
        return NextClassStatus.Weekend
    }
    if (entries.isEmpty()) {
        return NextClassStatus.NoClassesRegistered
    }

    val time = now.toLocalTime()
    val current = entries.firstOrNull { time >= it.startTime && time < it.endTime }
    val next = entries.filter { it.startTime > time }.minByOrNull { it.startTime }

    if (current != null) {
        if (next != null) {
            val minutesUntilNext = ChronoUnit.MINUTES.between(time, next.startTime)
            if (minutesUntilNext <= IMMINENT_THRESHOLD_MINUTES) {
                return NextClassStatus.Upcoming(next, minutesUntilNext)
            }
        }
        return NextClassStatus.InClass(current)
    }

    return if (next != null) {
        NextClassStatus.Upcoming(next, ChronoUnit.MINUTES.between(time, next.startTime))
    } else {
        NextClassStatus.FinishedForToday
    }
}
