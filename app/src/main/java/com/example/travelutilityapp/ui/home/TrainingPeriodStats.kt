package com.example.travelutilityapp.ui.home

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * [remainingDays] is left un-formatted (positive/zero/negative) so the caller can render its
 * D-Day and "weeks left" labels via localized string resources.
 */
data class TrainingPeriodStats(
    val dateRangeLabel: String,
    val remainingDays: Long,
    val progress: Float
)

private val DATE_LABEL_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("M.d")

/**
 * Derives the Home screen's D-Day stats from a start/end date, counting down to [end].
 * [today] is injectable for testing; defaults to the device's current date.
 */
fun computeTrainingPeriodStats(
    start: LocalDate,
    end: LocalDate,
    today: LocalDate = LocalDate.now()
): TrainingPeriodStats {
    val totalDays = ChronoUnit.DAYS.between(start, end).coerceAtLeast(1)
    val elapsedDays = ChronoUnit.DAYS.between(start, today).coerceIn(0, totalDays)
    val remainingDays = ChronoUnit.DAYS.between(today, end)
    val progress = (elapsedDays.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)

    return TrainingPeriodStats(
        dateRangeLabel = "${start.format(DATE_LABEL_FORMATTER)} - ${end.format(DATE_LABEL_FORMATTER)}",
        remainingDays = remainingDays,
        progress = progress
    )
}
