package com.example.travelutilityapp.ui.home

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.ceil

data class TrainingPeriodStats(
    val dateRangeLabel: String,
    val dDayLabel: String,
    val weeksLeftLabel: String,
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

    val dDayLabel: String
    val weeksLeftLabel: String
    when {
        remainingDays > 0 -> {
            dDayLabel = "D-$remainingDays"
            weeksLeftLabel = "${ceil(remainingDays / 7.0).toInt()}주 남음"
        }
        remainingDays == 0L -> {
            dDayLabel = "D-DAY"
            weeksLeftLabel = "오늘 종료"
        }
        else -> {
            dDayLabel = "연수 종료"
            weeksLeftLabel = "연수 종료"
        }
    }

    return TrainingPeriodStats(
        dateRangeLabel = "${start.format(DATE_LABEL_FORMATTER)} - ${end.format(DATE_LABEL_FORMATTER)}",
        dDayLabel = dDayLabel,
        weeksLeftLabel = weeksLeftLabel,
        progress = progress
    )
}
