package com.example.travelutilityapp.ui.budget

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong

/** 1 peso = 22 won, hardcoded per the current fixed exchange rate. */
const val PESO_TO_KRW_RATE = 22

/** Converts [ExpenseEntry.amount] into [displayCurrency], leaving entries already in that currency untouched. */
fun ExpenseEntry.amountIn(displayCurrency: Currency): Long = when {
    currency == displayCurrency -> amount
    currency == Currency.PESO && displayCurrency == Currency.KRW -> amount * PESO_TO_KRW_RATE
    currency == Currency.KRW && displayCurrency == Currency.PESO -> (amount / PESO_TO_KRW_RATE.toDouble()).roundToLong()
    else -> amount
}

fun List<ExpenseEntry>.totalIn(displayCurrency: Currency): Long = sumOf { it.amountIn(displayCurrency) }

private val GROUPED_NUMBER = NumberFormat.getNumberInstance(Locale.US)

fun formatAmount(amount: Long, currency: Currency): String = when (currency) {
    Currency.KRW -> "₩${GROUPED_NUMBER.format(amount)}"
    Currency.PESO -> "${GROUPED_NUMBER.format(amount)}p"
}
