package com.example.travelutilityapp.data

import android.content.Context
import com.example.travelutilityapp.ui.budget.Currency
import com.example.travelutilityapp.ui.budget.ExpenseEntry
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS_NAME = "budget_prefs"
private const val KEY_ENTRIES = "entries"
private const val KEY_CURRENCY_A = "currency_pair_a"
private const val KEY_CURRENCY_B = "currency_pair_b"
private const val KEY_EXCHANGE_RATES = "exchange_rates"
private val DEFAULT_CURRENCY_PAIR = Currency.PESO to Currency.KRW

/** Persists the user's expense entries across app restarts. */
class BudgetRepository(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<ExpenseEntry> {
        val json = prefs.getString(KEY_ENTRIES, null) ?: return emptyList()
        val array = runCatching { JSONArray(json) }.getOrNull() ?: return emptyList()
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            runCatching {
                ExpenseEntry(
                    id = obj.getString("id"),
                    dateLabel = obj.getString("dateLabel"),
                    description = obj.getString("description"),
                    amount = obj.getLong("amount"),
                    currency = Currency.valueOf(obj.getString("currency"))
                )
            }.getOrNull()
        }
    }

    fun save(entries: List<ExpenseEntry>) {
        val array = JSONArray()
        entries.forEach { entry ->
            val obj = JSONObject()
            obj.put("id", entry.id)
            obj.put("dateLabel", entry.dateLabel)
            obj.put("description", entry.description)
            obj.put("amount", entry.amount)
            obj.put("currency", entry.currency.name)
            array.put(obj)
        }
        prefs.edit().putString(KEY_ENTRIES, array.toString()).apply()
    }

    /** The two currencies pinned to the Budget screen's display toggle. */
    fun loadCurrencyPair(): Pair<Currency, Currency> {
        val a = prefs.getString(KEY_CURRENCY_A, null)?.let { runCatching { Currency.valueOf(it) }.getOrNull() }
        val b = prefs.getString(KEY_CURRENCY_B, null)?.let { runCatching { Currency.valueOf(it) }.getOrNull() }
        return if (a != null && b != null && a != b) a to b else DEFAULT_CURRENCY_PAIR
    }

    fun saveCurrencyPair(pair: Pair<Currency, Currency>) {
        prefs.edit()
            .putString(KEY_CURRENCY_A, pair.first.name)
            .putString(KEY_CURRENCY_B, pair.second.name)
            .apply()
    }

    /** The last successfully fetched won-per-unit exchange rates, kept for offline use. */
    fun loadExchangeRates(): Map<Currency, Double>? {
        val json = prefs.getString(KEY_EXCHANGE_RATES, null) ?: return null
        val obj = runCatching { JSONObject(json) }.getOrNull() ?: return null
        return runCatching {
            Currency.entries.associateWith { obj.getDouble(it.name) }
        }.getOrNull()
    }

    fun saveExchangeRates(rates: Map<Currency, Double>) {
        val obj = JSONObject()
        rates.forEach { (currency, rate) -> obj.put(currency.name, rate) }
        prefs.edit().putString(KEY_EXCHANGE_RATES, obj.toString()).apply()
    }
}
