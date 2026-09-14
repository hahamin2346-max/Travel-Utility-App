package com.example.travelutilityapp.ui.schedule

import androidx.compose.ui.graphics.Color
import com.example.travelutilityapp.ui.theme.YwAccentGold
import com.example.travelutilityapp.ui.theme.YwAccentGoldSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwPrimarySoft
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

enum class ClassType(val label: String) {
    GROUP("Group"),
    ONE_ON_ONE("1on1")
}

fun ClassType.badgeColors(): Pair<Color, Color> = when (this) {
    ClassType.GROUP -> YwPrimarySoft to YwPrimary
    ClassType.ONE_ON_ONE -> YwAccentGoldSoft to YwAccentGold
}

data class ScheduleEntry(
    val id: String = UUID.randomUUID().toString(),
    val type: ClassType = ClassType.GROUP,
    val startTime: LocalTime = LocalTime.of(9, 0),
    val endTime: LocalTime = LocalTime.of(9, 45),
    val room: String = "",
    val course: String = "",
    val teacher: String = ""
)

private val TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")

fun ScheduleEntry.timeRangeLabel(): String =
    "${startTime.format(TIME_FORMATTER)} ~ ${endTime.format(TIME_FORMATTER)}"
