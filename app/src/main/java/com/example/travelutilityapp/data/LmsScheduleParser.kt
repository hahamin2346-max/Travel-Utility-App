package com.example.travelutilityapp.data

import com.example.travelutilityapp.ui.schedule.ClassType
import com.example.travelutilityapp.ui.schedule.ScheduleEntry
import org.json.JSONObject
import java.time.LocalTime

/**
 * Parses the JSON returned by lms.jicportal.com's `schedule_ajax.html`
 * (`req_type=get_schedule_list`) into our [ScheduleEntry] model.
 *
 * The response has no explicit class-type field: a row is a Group class when
 * `ssc_sgp_id` is set, and a 1-on-1 class when `ssc_st_id` is set instead.
 */
fun parseLmsScheduleResponse(json: String): List<ScheduleEntry> {
    val root = runCatching { JSONObject(json) }.getOrNull() ?: return emptyList()
    if (root.optString("code") != "0") return emptyList()

    val schList = root.optJSONArray("sch_list") ?: return emptyList()
    return (0 until schList.length()).mapNotNull { index ->
        val row = schList.optJSONObject(index) ?: return@mapNotNull null
        runCatching {
            val type = if (row.optLong("ssc_sgp_id", 0L) != 0L) ClassType.GROUP else ClassType.ONE_ON_ONE
            val (start, end) = row.getString("stb_name").split("~").map { it.trim() }
                .let { LocalTime.parse(it[0]) to LocalTime.parse(it[1]) }

            ScheduleEntry(
                type = type,
                startTime = start,
                endTime = end,
                room = row.optString("ssc_room"),
                course = row.optString("ssc_course"),
                teacher = row.optString("te_name")
            )
        }.getOrNull()
    }
}
