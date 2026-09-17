package com.example.travelutilityapp.ui.budget

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

enum class Currency { KRW, PESO, JPY, TWD, CNY, USD }

private val EXPENSE_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("M.d")

/** A single expense entry, stored in whatever currency it was entered in. */
data class ExpenseEntry(
    val id: String = UUID.randomUUID().toString(),
    val dateLabel: String = LocalDate.now().format(EXPENSE_DATE_FORMATTER),
    val description: String,
    val amount: Long,
    val currency: Currency
)
