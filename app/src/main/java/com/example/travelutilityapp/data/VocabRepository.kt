package com.example.travelutilityapp.data

import android.content.Context
import com.example.travelutilityapp.ui.vocab.VocabWord
import org.json.JSONArray
import org.json.JSONObject



private const val PREFS_NAME = "vocab_prefs"
//private const val KEY_WORDS = "words"  //이거 공통 조회중이었던 것...
private const val KEY_MY_WORDS = "my_words"
private const val KEY_DONT_KNOW_WORDS = "dont_know_words"

private const val KEY_CEFR_EXCLUDED_IDS = "cefr_excluded_ids"

/** Persists the user's vocabulary list across app restarts. */
class VocabRepository(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun loadMyWords(): List<VocabWord> {
        val json = prefs.getString(KEY_MY_WORDS, null) ?: return emptyList()
        val array = runCatching { JSONArray(json) }.getOrNull() ?: return emptyList()
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            runCatching {
                VocabWord(
                    id = obj.getString("id"),
                    word = obj.getString("word"),
                    meaning = obj.getString("meaning")
                )
            }.getOrNull()
        }
    }

    fun saveMyWords(words: List<VocabWord>) {
        val array = JSONArray()
        words.forEach { word ->
            val obj = JSONObject()
            obj.put("id", word.id)
            obj.put("word", word.word)
            obj.put("meaning", word.meaning)
            obj.put("isKnown", word.isKnown)
            array.put(obj)
        }
        prefs.edit().putString(KEY_MY_WORDS, array.toString()).apply()
    }

    fun loadDontKnowWords(): List<VocabWord> {
        val json = prefs.getString(KEY_DONT_KNOW_WORDS, null) ?: return emptyList()
        val array = runCatching { JSONArray(json) }.getOrNull() ?: return emptyList()
        return (0 until array.length()).mapNotNull { index ->
            val obj = array.optJSONObject(index) ?: return@mapNotNull null
            runCatching {
                VocabWord(
                    id = obj.getString("id"),
                    word = obj.getString("word"),
                    meaning = obj.getString("meaning")
                )
            }.getOrNull()
        }
    }

    fun saveDontKnowWords(words: List<VocabWord>) {
        val array = JSONArray()
        words.forEach { word ->
            val obj = JSONObject()
            obj.put("id", word.id)
            obj.put("word", word.word)
            obj.put("meaning", word.meaning)
            obj.put("isKnown", word.isKnown)
            array.put(obj)
        }
        prefs.edit().putString(KEY_DONT_KNOW_WORDS, array.toString()).apply()
    }

    /** Ids of CEFR words the user removed from the CEFR view (the bundled word set itself is never modified). */
    fun loadExcludedCefrIds(): Set<String> {
        val json = prefs.getString(KEY_CEFR_EXCLUDED_IDS, null) ?: return emptySet()
        val array = runCatching { JSONArray(json) }.getOrNull() ?: return emptySet()
        return (0 until array.length()).mapNotNull { array.optString(it, null) }.toSet()
    }

    fun saveExcludedCefrIds(ids: Set<String>) {
        val array = JSONArray()
        ids.forEach { array.put(it) }
        prefs.edit().putString(KEY_CEFR_EXCLUDED_IDS, array.toString()).apply()
    }
}
