package com.example.travelutilityapp.ui.budget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.example.travelutilityapp.R
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToLong

/** Fixed exchange rates against 1 KRW, used only until a live rate has been fetched at least once. */
private val FALLBACK_RATE_TO_KRW: Map<Currency, Double> = mapOf(
    Currency.KRW to 1.0,
    Currency.PESO to 22.0,
    Currency.JPY to 9.0,
    Currency.TWD to 43.0,
    Currency.CNY to 195.0,
    Currency.USD to 1400.0
)

/**
 * Holds the exchange rates ([Currency] to won) that [amountIn] converts with. Starts out on
 * [FALLBACK_RATE_TO_KRW] and is updated by [ExchangeRateService] once a live or cached fetch
 * succeeds. Backed by Compose state so screens reading totals recompose automatically.
 */
object ExchangeRates {
    var rateToKrw: Map<Currency, Double> by mutableStateOf(FALLBACK_RATE_TO_KRW)
        private set

    /** Epoch millis of the last successful live fetch, or null if only the fallback rate has ever been used. */
    var lastFetchedAt: Long? by mutableStateOf(null)
        private set

    fun applyCached(rates: Map<Currency, Double>) {
        rateToKrw = rates
    }

    fun applyLive(rates: Map<Currency, Double>) {
        rateToKrw = rates
        lastFetchedAt = System.currentTimeMillis()
    }
}

/** Converts [ExpenseEntry.amount] into [displayCurrency], leaving entries already in that currency untouched. */
fun ExpenseEntry.amountIn(displayCurrency: Currency): Long = when (currency) {
    displayCurrency -> amount
    else -> {
        val rates = ExchangeRates.rateToKrw
        (amount * rates.getValue(currency) / rates.getValue(displayCurrency)).roundToLong()
    }
}

fun List<ExpenseEntry>.totalIn(displayCurrency: Currency): Long = sumOf { it.amountIn(displayCurrency) }

private val GROUPED_NUMBER = NumberFormat.getNumberInstance(Locale.US)

fun formatAmount(amount: Long, currency: Currency): String = when (currency) {
    Currency.KRW -> "₩${GROUPED_NUMBER.format(amount)}"
    Currency.PESO -> "${GROUPED_NUMBER.format(amount)}p"
    Currency.JPY -> "¥${GROUPED_NUMBER.format(amount)}"
    Currency.TWD -> "NT$${GROUPED_NUMBER.format(amount)}"
    Currency.CNY -> "CN¥${GROUPED_NUMBER.format(amount)}"
    Currency.USD -> "$${GROUPED_NUMBER.format(amount)}"
}

/** Localized short name for [currency], e.g. "원화" / "KRW". */
@Composable
fun Currency.displayName(): String = when (this) {
    Currency.KRW -> stringResource(R.string.budget_currency_name_krw)
    Currency.PESO -> stringResource(R.string.budget_currency_name_peso)
    Currency.JPY -> stringResource(R.string.budget_currency_name_jpy)
    Currency.TWD -> stringResource(R.string.budget_currency_name_twd)
    Currency.CNY -> stringResource(R.string.budget_currency_name_cny)
    Currency.USD -> stringResource(R.string.budget_currency_name_usd)
}
