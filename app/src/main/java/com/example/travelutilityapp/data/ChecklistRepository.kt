package com.example.travelutilityapp.data

import android.content.Context
import com.example.travelutilityapp.ui.checklist.ChecklistItem
import com.example.travelutilityapp.ui.checklist.ChecklistType
import org.json.JSONArray
import org.json.JSONObject

private const val PREFS_NAME = "checklist_prefs"
private const val KEY_TODAY = "today_items"
private const val KEY_AFTER_RETURN = "after_return_items"

/** Persists the two independent checklists (today / after-return) across app restarts. */
class ChecklistRepository(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(type: ChecklistType): List<ChecklistItem> {
        val json = prefs.getString(keyFor(type), null) ?: return emptyList()
        val array = runCatching { JSONArray(json) }.getOrNull() ?: return emptyList()
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            runCatching {
                ChecklistItem(
                    id = obj.getString("id"),
                    content = obj.getString("content"),
                    dateLabel = obj.getString("dateLabel"),
                    isChecked = obj.getBoolean("isChecked")
                )
            }.getOrNull()
        }
    }

    fun save(type: ChecklistType, items: List<ChecklistItem>) {
        val array = JSONArray()
        items.forEach { item ->
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("content", item.content)
            obj.put("dateLabel", item.dateLabel)
            obj.put("isChecked", item.isChecked)
            array.put(obj)
        }
        prefs.edit().putString(keyFor(type), array.toString()).apply()
    }

    private fun keyFor(type: ChecklistType): String = when (type) {
        ChecklistType.TODAY -> KEY_TODAY
        ChecklistType.AFTER_RETURN -> KEY_AFTER_RETURN
    }
}
