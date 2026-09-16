package com.example.travelutilityapp.ui.vocab

import android.content.Context
import org.json.JSONArray

/**
 * Standard CEFR word set, bundled at build time (assets/cefr_words.json) and parsed once.
 * Fixed set: no add/delete logic beyond the user's per-word CEFR-view exclusions.
 */
object CefrVocabulary {
    @Volatile
    private var cache: List<VocabWord>? = null

    fun words(context: Context): List<VocabWord> {
        cache?.let { return it }
        val loaded = runCatching {
            context.applicationContext.assets.open("cefr_words.json").use { input ->
                val json = input.bufferedReader(Charsets.UTF_8).readText()
                val array = JSONArray(json)
                (0 until array.length()).map { index ->
                    val obj = array.getJSONObject(index)
                    VocabWord(
                        id = "cefr_$index",
                        word = obj.getString("word"),
                        meaning = obj.getString("meaning")
                    )
                }
            }
        }.getOrDefault(emptyList())
        cache = loaded
        return loaded
    }
}
