package com.example.travelutilityapp.ui.checklist

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

/** The two independent checklists shown on the Checklist screen's segmented control. */
enum class ChecklistType {
    TODAY,
    AFTER_RETURN
}

data class ChecklistItem(
    val id: String = UUID.randomUUID().toString(),
    val content: String = "",
    val dateLabel: String = LocalDate.now().format(CHECKLIST_DATE_FORMATTER),
    val isChecked: Boolean = false
)

private val CHECKLIST_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("M.d")
