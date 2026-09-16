package com.example.travelutilityapp.data

import android.content.Context
import com.example.travelutilityapp.ui.budget.Currency
import com.example.travelutilityapp.ui.budget.ExpenseEntry
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS_NAME = "budget_prefs"
private const val KEY_ENTRIES = "entries"

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
}
