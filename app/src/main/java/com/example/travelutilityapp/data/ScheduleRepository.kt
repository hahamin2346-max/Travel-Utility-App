package com.example.travelutilityapp.data

import android.content.Context
import com.example.travelutilityapp.ui.schedule.ClassType
import com.example.travelutilityapp.ui.schedule.ScheduleEntry
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalTime

private const val PREFS_NAME = "schedule_prefs"
private const val KEY_ENTRIES = "entries"

/** Persists the user's class schedule entries across app restarts. */
class ScheduleRepository(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): List<ScheduleEntry> {
        val json = prefs.getString(KEY_ENTRIES, null) ?: return emptyList()
        val array = runCatching { JSONArray(json) }.getOrNull() ?: return emptyList()
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            runCatching {
                ScheduleEntry(
                    id = obj.getString("id"),
                    type = ClassType.valueOf(obj.getString("type")),
                    startTime = LocalTime.parse(obj.getString("startTime")),
                    endTime = LocalTime.parse(obj.getString("endTime")),
                    room = obj.getString("room"),
                    course = obj.optString("course", ""),
                    teacher = obj.optString("teacher", "")
                )
            }.getOrNull()
        }
    }

    fun save(entries: List<ScheduleEntry>) {
        val array = JSONArray()
        entries.forEach { entry ->
            val obj = JSONObject()
            obj.put("id", entry.id)
            obj.put("type", entry.type.name)
            obj.put("startTime", entry.startTime.toString())
            obj.put("endTime", entry.endTime.toString())
            obj.put("room", entry.room)
            obj.put("course", entry.course)
            obj.put("teacher", entry.teacher)
            array.put(obj)
        }
        prefs.edit().putString(KEY_ENTRIES, array.toString()).apply()
    }
}
